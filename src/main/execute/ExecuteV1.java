package main.execute;

import image.TypeImage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import main.Execute;
import main.GenerateRandomSolution;
import main.Solution;

public class ExecuteV1 extends Execute {
	/*
	 * Fonctionnement global
	 */
	public Solution execute(File f) {//, int ltPercentNiv1, int nbIteNiv1, int ltPercentNiv2, int nbIteNiv2) throws Exception {	
		System.out.println("\r\n");
		System.out.println("Le fichier "+f.getName()+" va etre traité\r\n");
	
		List<TypeImage> imagesToPlace = this.setup(f); //chargement fichier, ..
		
		/* 
		 * Generation d'une solution aléatoire
		 * Incrementer le nombre de pattern jusqu'à trouver la première 
		 * solution qui fonctionne aléatoire
		 */
		Solution sRandom = null;
		GenerateRandomSolution generator = new GenerateRandomSolution(imagesToPlace);
		for (int nbPattern = 1;; nbPattern++)
		{
			System.out.println("Gen sol alea nb pattern = "+nbPattern);
			Solution sR = generator.generate(nbPattern);
			if (sR != null)
			{
				sRandom = sR;
				break;
			}
		}
		sRandom.reconstruct(); //recontruire la solution pour réduire le nombre de pattern
		System.out.println("Solution aléatoire : \r\n");
		System.out.println(sRandom);
		System.out.println("prix sRandom = "+sRandom.calculPrice());
		System.out.println("\r\nRecherche de solution ..");
		
		Solution sBest = lookup(sRandom, 50, 1, 10000).get(0);
		
		System.out.println("\r\n");
		System.out.println("Meilleure solution !");
		System.out.println(sBest);
		System.out.println("Price = "+sBest.calculPrice());
		
		return sBest;
	}
	
	public static void main(String[] args) throws FileNotFoundException {
		Execute exec = new ExecuteV1();
		
		File f = exec.chooseFile();
		if (f == null) return;
		
		new File("dataOut").mkdir(); //dossier dataOut
		new File("dataOut/executeV1").mkdir(); //dossier execute v2
		new File("dataOut/executeV1/"+f.getName()).mkdir(); //dossier avec nom fichier
		
		for (int i = 0; i < 3; ++i)
		{	
			SimpleDateFormat filePattern = new SimpleDateFormat("ddMMyyyy_HHmmss");
			String filename = filePattern.format(new Date()) + ".txt";
			
			File file = new File("dataOut/executeV1/"+f.getName()+"/"+filename); //fichier avec date
			
			PrintStream printStream = new PrintStream(file);
			System.setOut(printStream);
	
			try {
				long startTime = System.currentTimeMillis();
				
				Solution bestSol = exec.execute(f);			
				
				System.out.println("\r\nTotal time : "+(System.currentTimeMillis()-startTime));
				
				//JFrame frame = new Affichage(bestSol,new Dimension((int)Pattern.getWidth(),(int)Pattern.getHeight()));
				//Affichage.affiche(frame);
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
	}
}
