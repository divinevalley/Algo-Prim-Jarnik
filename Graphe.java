import java.util.TreeMap;

public class Graphe {
	private TreeMap<String, Sommet> tousSommets;
	
	
	// constructeur
	public Graphe() {
		tousSommets = new TreeMap<String,Sommet>();
	}

	public TreeMap<String, Sommet> getTousSommets() {
		return tousSommets;
	}

	@Override
	public String toString() {
		return "Tous Sommets: " + tousSommets.toString();
	}
	
	

}
