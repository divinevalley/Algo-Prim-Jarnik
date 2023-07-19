
public class Tp3 {

	
	
	public static void main(String[] args) {

		// C:\Users\Deanna\Documents\UdeM\IFT2015 structures de donnees\Devoir3\Tp3\Tp3\src
		
		// test: nom fichier : 
		String args0 = "carte3.txt";
		
		
		// construire le path pour chercher le nom du fichier desiré
		String path = System.getProperty("user.dir"); 
		
		path = "C:\\Users\\Deanna\\Documents\\UdeM\\IFT2015 structures de donnees\\Devoir3\\testsTP3E19";
		path += "/" + args0;
		
		
		Graphe graphe = new Graphe();
		
		Utils.lireFichierInstancierGraphe(path, graphe);
		
//		System.out.println(graphe.toString());
		
		Utils.algoPrim(graphe);
		
		
	}
	

}
