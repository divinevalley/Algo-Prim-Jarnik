import java.util.TreeMap;
/**
 * les Sommets représentent les "places publiques" à relier dans l'énoncé. 
 *   
 * @author Deanna Wung
 *
 */
public class Sommet {
		private final String nomSommet;
		private TreeMap<String, Arete> aretesSortantes;
		
		public Sommet(String nom) {
			nomSommet = nom;
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
		

	}
