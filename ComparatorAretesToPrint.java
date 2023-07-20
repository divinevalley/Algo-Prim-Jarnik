import java.util.Comparator;

/**
 * Pour le fichier de sortie final, on veut l'ordre alphanumérique des arêtes : 
 * Enoncé :  
 * afficher les arêtes en ordre croissant alphanumérique des nœuds de départs. 
 * Si plusieurs arêtes ont le même nœud de départ, vous devez afficher ces arêtes 
 * selon l’ordre croissant alphanumérique des nœuds d’arrivés
 *
 */
public class ComparatorAretesToPrint implements Comparator<Arete>{

	@Override
	public int compare(Arete o1, Arete o2) {
		int compareSommet1 = o1.getSommet1().getNomSommet().compareTo(o2.getSommet1().getNomSommet());
		int compareSommet2 = o1.getSommet2().getNomSommet().compareTo(o2.getSommet2().getNomSommet());

		if  (compareSommet1 != 0) { // comparer nom sommet1
			return compareSommet1;
		} else {
			return compareSommet2; // si nom sommet 1 identique, comparer nom sommet 2
		}	
	}

}