/**
 * Représente les arêtes ("rues") avec des noms, des poids (coût), et leurs neouds (sites publics) de départ/d'arrivée. 
 * (Il n'y a pas réellement de notion de départ/d'arrivée vu que les rues sont à deux sens.) 
 *  
 * @author Deanna Wung
 *
 */
public class Arete {
		private final String nomArete; // eg. rue0
		private final Sommet sommet1; // sommet "de départ"
		private final Sommet sommet2; // sommet "d'arrivée"
		private final int poids;


		public Arete(String nomArete, Sommet sommet1, Sommet sommet2, int poids) {
			this.nomArete = nomArete;
			this.sommet1 = sommet1;
			this.sommet2 = sommet2;
			this.poids = poids;
		}

		// getters
		public String getNomArete() {
			return nomArete;
		}

		public Sommet getSommet1() {
			return sommet1;
		}

		public Sommet getSommet2() {
			return sommet2;
		}

		public int getPoids() {
			return poids;
		}
		

		@Override
		public String toString() {
			return nomArete + "\t" + sommet1.getNomSommet() + "\t" + sommet2.getNomSommet() + "\t" + poids;
		}


	}