import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Map.Entry;
import java.util.PriorityQueue;
import java.util.TreeMap;
import java.util.TreeSet;

public class Utils {

	public static void lireFichierInstancierGraphe(String nomFichier, Graphe graphe) {

		boolean section1 = true; // on est a la section 1 (=noms des sommets), sinon on est au noms des arretes  


		try{
			FileReader fileReader = new FileReader(nomFichier);
			BufferedReader br = new BufferedReader(fileReader);
			String line;

			int linenb = 0; //pour deboguage 


			while((line = br.readLine()) != null) {
				line=line.trim();
				linenb++; //pour deboguage 


				if (line.equals("---")) {
					section1 = false; // signale qu on passe a la prochaine section
					continue; // recommencer boucle
				}

				if (section1) { // dans section 1, on instancie les sommets 

					// instancier sommet et mettre dans graphe
					Sommet sommet = new Sommet(line);
					graphe.getTousSommets().put(line, sommet);

				} else { // dans section 2 (aretes)  eg. rue0 : a b 9 ;
					line=line.replace(";", "");
					String[] infosAretes = line.split(":");

					String nomArete = infosAretes[0].trim(); // prendre partie avant ":" (eg. rue0)

					// apres le ":"  (eg. a b 9 ; ), split encore une fois 
					String[] infosSommets = infosAretes[1].trim().split("\s");

					String nomSommetA = infosSommets[0]; // eg. a
					String nomSommetB = infosSommets[1]; // eg. b
					int poids = Integer.parseInt(infosSommets[2]); // eg. 9

					Sommet sommetA = graphe.getTousSommets().get(nomSommetA);
					Sommet sommetB = graphe.getTousSommets().get(nomSommetB);

					// mettre comme arete sortante 
					Arete areteAB = new Arete(nomArete, sommetA, sommetB, poids); // a-b  
					sommetA.getAretesSortantes().put(nomArete+":"+nomSommetA+"-"+nomSommetB, areteAB); // dans a comme arete sortante

					// inverser l'ordre (a-b devient b-a)
					Arete areteBA = new Arete (nomArete, sommetB, sommetA, poids); // b-a 
					sommetB.getAretesSortantes().put(nomArete+":"+nomSommetB+"-"+nomSommetA, areteBA); // dans b comment sortante
				}
			}
		} catch (IOException e) {
			System.err.println("Error reading file: " + e.getMessage());
			System.out.println("Absolute path:" + new File(nomFichier).getAbsolutePath());
		}
	}

	public static void algoPrim(Graphe graphe) {
		StringBuilder toPrint = new StringBuilder();

		//convertir en hashset pour marquer somme comme visité 
		TreeMap<String, Sommet> tousSommets = graphe.getTousSommets();
		HashSet<String> sommetsAExplorer = new HashSet<>(tousSommets.keySet());

		// initialiser sommets visités (vide au début)
		HashSet<String> sommetsVisites = new HashSet<>(); 

		// initialiser priority queue
		PriorityQueue<Arete> queue = new PriorityQueue<>();

		TreeMap<String,Arete> aretesVisites = new TreeMap<>();

		// commencer : 
		Sommet sommet = tousSommets.firstEntry().getValue();//"premier" sommet (alphanumerique)
		sommetsVisites.add(sommet.getNomSommet());	// marquer sommet comme visité

		int coutTotal = 0;

		System.out.println("start");
		System.out.println("sommetsAExplorer:"+sommetsAExplorer);

		// tant qu on n'a pas tout visité
		while(!sommetsAExplorer.equals(sommetsVisites)){

			// marquer sommet comme visité
			sommetsVisites.add(sommet.getNomSommet());

			System.out.println("mnt sommet = " + sommet);
			System.out.println("sommets Visites:" + sommetsVisites);

			// pour toutes les aretes (aux sommets non visités), mettre dans PQ
			for (Arete arete : sommet.getAretesSortantes().values()) {
				if (!sommetsVisites.contains(arete.getSommet2().getNomSommet())) { // non visité 
					queue.add(arete);// ajouter si non visite
				}
			}

			System.out.println("queue:" + queue);

			// parcourir queue jusqua ce qu on arrive à un sommet non visité 

			Sommet dernierSommet = sommet;
			// mnt que la PQ est à jour, prendre le min
			int poidsAAjouter = 0;
			Arete areteMin = queue.poll(); // enlever premier		
			String sommet1 = areteMin.getSommet1().getNomSommet();
			String sommet2 = areteMin.getSommet2().getNomSommet();

			// mais on veut s'assurer qu'on est sur la bonne arete (qui va nous servir)
			// tant qu'on est sur une arete amenant à un sommet déjà visite, dequeue (enlever) 
			// jusqu'à ce qu'on arrive sur une arete amenant à un sommet non visité OU queue vide
			while (sommetsVisites.contains(sommet2) && !queue.isEmpty()) { 
				areteMin = queue.poll(); // dequeue: prendre prochaine arete 
				System.out.println("...enlever + " + areteMin);
				sommet2 = areteMin.getSommet2().getNomSommet();	
			}


			// une fois qu'on est sur la BONNE arete 
			if(!sommetsVisites.contains(sommet2)) { // si arete amene a sommet non visité
				aretesVisites.put(areteMin.getNomArete() + areteMin.getSommet1() + areteMin.getSommet2(), areteMin); // eg. key: rue0ab 

				System.out.println("arete min : " + areteMin);
				sommet = areteMin.getSommet2(); // pour passer au prochain sommet
				poidsAAjouter = areteMin.getPoids();
				coutTotal += poidsAAjouter;
				System.out.println("-------");
			}




		}

		// preparer toprint (une fois triés)
		for (String s : sommetsVisites) {
			toPrint.append(s+"\n");
		}

		for (Entry<String, Arete> a : aretesVisites.entrySet()) {
			toPrint.append(a.getValue().toString()+"\n");
		}

		toPrint.append("---\n");

		toPrint.append(coutTotal);
		System.out.println("cout total = " + coutTotal);
		System.out.println(toPrint.toString());
	}
}	
