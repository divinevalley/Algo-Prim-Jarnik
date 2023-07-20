import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 * La classe Utils sert à refactor toutes les fonctions (toutes static) dont on a besoin, 
 * et ne sera jamais instanciée. 
 *  
 * @author Deanna Wung
 */
public class Utils {

	/** 
	 * Lire le fichier txt et instancie un graphe avec tous ses sommets et ses arêtes 
	 * Complexité : pour instancier le graphe, on parcourt tous les v sommets et toutes les e arêtes. O(v+e)
	 * 
	 * @param nomFichier
	 * @param objet Graphe instancié
	 */
	public static void lireFichierInstancierGraphe(String nomFichier, Graphe graphe) {

		boolean section1 = true; // indique si on est a la section 1 (sommets), sinon ça veut dire qu'on est à la section 2 (arretes)  

		try{
			FileReader fileReader = new FileReader(nomFichier);
			BufferedReader br = new BufferedReader(fileReader);
			String line;

			while((line = br.readLine()) != null) {
				line=line.trim();

				if (line.equals("---")) {
					section1 = false; // signale qu'on passe à la prochaine section
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
	 * Applique l'algorithme Prim Jarnik ARM sur le graphe passé en paramètre et renvoie un String conforme à l'output demandé
	 * (d'abord tous les sommets visités en ordre alphanumérique, ensuite toutes les arêtes visitées en ordre alpha selon 
	 * sommet de départ, ou selon sommet d'arrivée si égal)  
	 * 
	 * Complexité : O(ElogE+VlogV+V)
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
		TreeSet<Arete> aretesVisites = new TreeSet<>(new ComparatorAretesToPrint()); // trié alphanumérique sommet de départ, sommet d'arrivée
		
		// initialiser priority queue
		PriorityQueue<Arete> queue = new PriorityQueue<>(new ComparatorPoidsAretes()); // trié par poids, ensuite selon l'énoncé

		// commencer par le premier sommet, et marquer visité: 
		Sommet sommet = tousSommets.firstEntry().getValue();
		sommetsVisites.add(sommet.getNomSommet());

		int coutTotal = 0;

		// Complexité ici : O(V+E) pour parcourir tous les sommets (O(V)) et chaque arête 2 fois (dans chaque sens) : O(E)
		while(sommetsAExplorer.size()!=(sommetsVisites.size())){ // tant qu'on n'a pas tout visité

			// marquer sommet comme visité
			sommetsVisites.add(sommet.getNomSommet());

			// pour toutes les aretes, mettre dans PQ si ça amene à un sommet non visité. 
			// Complexité : Parcourt toutes les E arêtes 2 fois (2 sens)  
			// et pour chacune, ajouter à la PQ, O(logE). donc globale : O(E*logE) 
			for (Arete arete : sommet.getAretesSortantes().values()) {
				if (!sommetsVisites.contains(arete.getSommet2().getNomSommet())) { // non visité 
					queue.add(arete);// ajouter si non visite // Complexité O(logN)
				}
			}

			// mnt que la PQ est à jour, prendre le min
			// Complexité : O(logE) pour l'operation dequeue (poll)
			int poidsAAjouter = 0;
			Arete areteMin = queue.poll(); // enlever premier // Complexité O(logE)
			String sommet2 = areteMin.getSommet2().getNomSommet();

			// s'assurer qu'on est sur la bonne arete (pas de cycles)
			// tant qu'on est sur une arete amenant à un sommet déjà visite, dequeue
			// Complexité : Parcourt toutes les aretes, et (possiblement) appelle poll (O(logN)), 
			// au maximum, on peut avoir 2*E (une fois dans les deux sens) => O(ElogE)
			while (sommetsVisites.contains(sommet2) && !queue.isEmpty()) { 
				areteMin = queue.poll(); // dequeue: prendre prochaine arete // O(logN)
				sommet2 = areteMin.getSommet2().getNomSommet();	
			}

			// une fois qu'on est sur la BONNE arete, ajouter sur TreeMap  
			// Complexité O(VlogV) pour put TreeMap (V au lieu de E car le nombre d'aretes retenus = V-1)
			if(!sommetsVisites.contains(sommet2)) { // si arete amene à un sommet non visité
				aretesVisites.add(areteMin); // sera trié 

				sommet = areteMin.getSommet2(); // pour passer au prochain sommet
				poidsAAjouter = areteMin.getPoids();
				coutTotal += poidsAAjouter;
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
		System.out.println(toPrint.toString());
		
		return toPrint.toString();
	}

	
	/***
	 * Cette fonction crée le fichier .txt final. 
	 * 
	 * @param nomFichierTxt
	 * @param contenuFichier
	 */
	public static void creerFichierFinal(String nomFichierTxt, String contenuFichier) {
		try {
			File fichierACreer = new File(nomFichierTxt); //pour créer fichier
			if (fichierACreer.createNewFile()) {
			}
		} catch (IOException e) {
			System.out.println("Erreur creation du fichier");
			e.printStackTrace();
		}

		try {
			FileWriter fileWriter = new FileWriter(nomFichierTxt); //pour écrire dans le fichier
			fileWriter.write(contenuFichier);
			fileWriter.close();
		} catch (IOException e) {
			System.out.println("Erreur chargement du contenu fichier");
			e.printStackTrace();
		}
	}

}	
