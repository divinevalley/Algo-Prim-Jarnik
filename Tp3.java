
/**
 * TP3 : Planification d’un réseau de panneaux publicitaires électroniques
 * IFT2015 Structures de données
 * Eté 2023
 * (Remise 31 juillet 2023)
 * 
 * Lit des fichiers txt contenant des sommets et des arêtes (des "sites" publics ou des "rues" dans une ville),
 * instancie un graphe, en fait un arbre de recouvrement minimal Prim-Jarnik, et écrit un fichier de sortie 
 * conforme au format demandé : 
 * liste de tous les sommets visités (ordre alphanumérique)
 * toutes les arêtes visitées (ordre alphanumérique du sommet de départ, ou sommet d'arrivée si somme de départ identique)
 * coût total 
 * 
 * 
 * 
 * @args args0 représente le nom du fichier d'entrée, args1 représente le nom du fichier de sortie
 * @author Deanna
 *
 */
public class Tp3 {
	public static void main(String[] args) {

		// C:\Users\Deanna\Documents\UdeM\IFT2015 structures de donnees\Devoir3\Tp3\Tp3\src
		
		// test: nom fichier : 
		String args0 = "carte6.txt";
		String args1 = "fichiersortie.txt";
		
		// construire le path pour chercher le nom du fichier desiré
		String path = System.getProperty("user.dir"); 
		
		path = "C:\\Users\\Deanna\\Documents\\UdeM\\IFT2015 structures de donnees\\Devoir3\\testsTP3E19";
		path += "/" + args0;
		
		
		Graphe graphe = new Graphe();
		
		Utils.lireFichierInstancierGraphe(path, graphe);
		
//		System.out.println(graphe.toString());
		
		String toPrint = Utils.algoPrim(graphe);
		Utils.creerFichierFinal(args1, toPrint);
		
	}
	

}
