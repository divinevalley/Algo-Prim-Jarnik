public class Arete {
		private final String nomArete; // eg. rue0
		private final Sommet sommet1;
		private final Sommet sommet2;
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
		
		// methode
		// comme les aretes sont bidirectionnelle, ça permet de récupérer le sommet qui est différent  
		// supposition que sommet passe en param est inclu dans sommet1 ou sommet2
		public Sommet prendreSommetVoisin(Sommet sommet) {
			if (sommet == sommet1) { // on veut passer a L'AUTRE sommet qui est différent
				return sommet2;
			} else {
				return sommet1;
			}
		}

		@Override
		public String toString() {
			return nomArete + "\t" + sommet1.getNomSommet() + "\t" + sommet2.getNomSommet() + "\t" + poids;
		}


	}