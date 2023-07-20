import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Map.Entry;
import java.util.PriorityQueue;
import java.util.TreeMap;
import java.util.TreeSet;

public class Utils {

	/**Complexité : pour instancier le graphe, on parcourt tous les v sommets et toutes les e arêtes. O(v+e)
	 *  
	 *  Lire le fichier txt et instancie un graphe avec ses sommets et ses arêtes 
	 *  
	 * @param nomFichier
	 * @param objet Graphe instancié
	 */
	
	public static void lireFichierInstancierGraphe(String nomFichier, Graphe graphe) {

		boolean section1 = true; // indique si on est a la section 1 (=noms des sommets), sinon on est au noms des arretes  

		try{
			FileReader fileReader = new FileReader(nomFichier);
			BufferedReader br = new BufferedReader(fileReader);
			String line;

			while((line = br.readLine()) != null) {
				line=line.trim();

				if (line.equals("---")) {
					section1 = false; // signale qu on passe a la prochaine section
					continue; // recommencer boucle
				}

				if (section1) { // dans section 1, on instancie les sommets 
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
			br.close();
		} catch (IOException e) {
			System.err.println("Error reading file: " + e.getMessage());
			System.out.println("Absolute path:" + new File(nomFichier).getAbsolutePath());
		}
	}

	/**
	 * Applique l'algorithme Prim Jarnik ARM sur le graphe rentré en paramètre et renvoie un String conforme à l'output demandé 
	 * 
	 * Complexité : O((V+E)log(E)+V)
	 * (+V est pour la partie print)  
	 * 
	 * @param graphe
	 * @return String conforme à l'output demandé (tous les sommets et aretes visités dans l'ordre) 
	 * pour écrire dans fichier final
	 */
	public static String algoPrim(Graphe graphe) {
		StringBuilder toPrint = new StringBuilder();

		//convertir en hashset pour marquer sommet visité 
		TreeMap<String, Sommet> tousSommets = graphe.getTousSommets();
		HashSet<String> sommetsAExplorer = new HashSet<>(tousSommets.keySet());

		// initialiser sommets et aretes visités (vide au début)
		TreeSet<String> sommetsVisites = new TreeSet<>(); 
		TreeSet<Arete> aretesVisites = new TreeSet<>(new ComparatorAretesToPrint()); // trié alphanumérique
		
		// initialiser priority queue
		PriorityQueue<Arete> queue = new PriorityQueue<>(new ComparatorPoidsAretes());

		// commencer par le premier sommet, et marquer visité: 
		Sommet sommet = tousSommets.firstEntry().getValue();
		sommetsVisites.add(sommet.getNomSommet());

		int coutTotal = 0;

		System.out.println("start");
		System.out.println("sommetsAExplorer:"+sommetsAExplorer);

		// Complexité ici : O(V+E) pour parcourir tous les sommets (O(V)) et chaque arête 2 fois (dans chaque sens) : O(E)
		while(!sommetsAExplorer.equals(sommetsVisites)){ // tant qu'on n'a pas tout visité

			// marquer sommet comme visité
			sommetsVisites.add(sommet.getNomSommet());

			System.out.println("mnt sommet = " + sommet);
			System.out.println("sommets Visites:" + sommetsVisites);

			// pour toutes les aretes, mettre dans PQ si ça amene à un sommet non visité
			// Complexité : pour ajouter à la PQ, O(logE) 
			for (Arete arete : sommet.getAretesSortantes().values()) {
				if (!sommetsVisites.contains(arete.getSommet2().getNomSommet())) { // non visité 
					queue.add(arete);// ajouter si non visite // Complexité O(logN)
				}
			}

			System.out.println("queue:" + queue);

			// Complexité : O(logE) pour l'operation poll (dequeue)
			// mnt que la PQ est à jour, prendre le min
			int poidsAAjouter = 0;
			Arete areteMin = queue.poll(); // enlever premier // Complexité O(logE)
			String sommet2 = areteMin.getSommet2().getNomSommet();

			// mais on veut s'assurer qu'on est sur la bonne arete (pas de cycles)
			// tant qu'on est sur une arete amenant à un sommet déjà visite, dequeue (enlever) 
			// jusqu'à ce qu'on arrive sur une arete amenant à un sommet non visité OU queue vide
			// Complexité : ici on est sur toutes les aretes, et on appelle poll (O(logN)), 
			// au maximum, on peut avoir 2*E (une fois dans les deux sens) => O(logE)
			while (sommetsVisites.contains(sommet2) && !queue.isEmpty()) { 
				areteMin = queue.poll(); // dequeue: prendre prochaine arete // O(logN)
				System.out.println("...enlever + " + areteMin);
				sommet2 = areteMin.getSommet2().getNomSommet();	
			}

			// une fois qu'on est sur la BONNE arete 
			if(!sommetsVisites.contains(sommet2)) { // si arete amene à un sommet non visité
				// Complexité de l'opération put TreeMap O(logV) (V car le nombre d'aretes retenus = V-1)
				aretesVisites.add(areteMin); // sera trié 

				System.out.println("arete min : " + areteMin);
				sommet = areteMin.getSommet2(); // pour passer au prochain sommet
				poidsAAjouter = areteMin.getPoids();
				coutTotal += poidsAAjouter;
				System.out.println("-------");
			}
		}
		
		// Complexité : Pour toPrint, parcourir tous les sommets visités dans ARM : O(V)
		// preparer toPrint (une fois triés)
		for (String s : sommetsVisites) {
			toPrint.append(s+"\n");
		}

		// Complexité : parcourir tous les arêtes visités O(V)
		// (V parce que le nombre d'aretes retenues sera V-1)
		for (Arete a : aretesVisites) {
			toPrint.append(a.toString()+"\n");
		}

		toPrint.append("---\n");

		toPrint.append(coutTotal);
		System.out.println("cout total = " + coutTotal);
		System.out.println(toPrint.toString());
		
		return toPrint.toString();
	}
	
	public static void creerFichier(String nomFichier) {
		// file writer ... 
		// TODO 
	}
}	
