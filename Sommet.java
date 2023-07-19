import java.util.TreeMap;



public class Sommet {
		private String nomSommet;
		private TreeMap<String, Arete> aretesSortantes;
//		private TreeMap<String, Arete> aretesEntrantes;  
		
		public Sommet(String nom) {
			nomSommet = nom;
//			aretesEntrantes = new TreeMap<>();
			aretesSortantes = new TreeMap<>();
		}

		public String getNomSommet() {
			return nomSommet;
		}

		public TreeMap<String, Arete> getAretesSortantes() {
			return aretesSortantes;
		}

		@Override
		public String toString() {
			return nomSommet + ", aretesSortantes=" + aretesSortantes;
		}

//		public TreeMap<String, Arete> getAretesEntrantes() {
//			return aretesEntrantes;
//		}
		
		
		

	}
