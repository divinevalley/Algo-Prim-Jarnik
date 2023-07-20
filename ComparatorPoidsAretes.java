import java.util.Comparator;

/**
 * Pour un tri qui se base sur POIDS des arêtes, si égal, ensuite selon énoncé :
 *  
 * Les arêtes de mêmes poids doivent être traitées selon l’ordre 
 * alphanumérique des nœuds de départs et dans le cas d’égalité des poids et des nœuds
 * de départs, utiliserez l’ordre alphanumérique des nœuds d’arrivés.
 */

public class ComparatorPoidsAretes implements Comparator<Arete> {

	@Override
	public int compare(Arete o1, Arete o2) {// pour que min Heap se base sur poids 
		int comparePoids =Integer.compare(o1.getPoids(), o2.getPoids()); 
		int compareSommet1 = o1.getSommet1().getNomSommet().compareTo(o2.getSommet1().getNomSommet());
		int compareSommet2 = o1.getSommet2().getNomSommet().compareTo(o2.getSommet2().getNomSommet());
		
		if (comparePoids != 0) { // si poids differents, d'abord comparer poids
			return comparePoids;
		} else if  (compareSommet1 != 0) { // si poids identiques, comparer nom sommet1, si ceci non identique
			return compareSommet1;
		} else {
			return compareSommet2; // si nom sommet 1 identique, comparer nom sommet 2
		}	
	}

}
