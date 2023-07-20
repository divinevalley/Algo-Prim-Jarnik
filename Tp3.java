
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
 * @args args0 représente le nom du fichier d'entrée, args1 représente le nom du fichier de sortie
 * @author Deanna
 *
 */
public class Tp3 {
	public static void main(String[] args) {
		
		// construire le path pour chercher le nom du fichier desiré
		String path = System.getProperty("user.dir"); 
		path += "/" + args[0];
		
		// initialiser
		Graphe graphe = new Graphe();
		
		// lire fichier, faire algo Prim, creer fichier final
		graphe = Utils.lireFichierInstancierGraphe(path, graphe);
		String toPrint = Utils.algoPrim(graphe);
		Utils.creerFichierFinal(args[1], toPrint);
		
	}

}
