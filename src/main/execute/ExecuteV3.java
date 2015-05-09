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

public class ExecuteV3 extends Execute  {
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
		

		Solution sBest = sRandom;
		for (Solution sNiv1 : lookup(sRandom, 80, 50, 100000))
		{ // Prendre les 3 meilleures solutions et refaire un tabou dessus
			sNiv1.reconstruct();
			for (Solution sNiv2 : lookup(sNiv1, 65, 25,100000))
			{ // Prendre les 3 meilleures solutions et refaire un tabou dessus
				sNiv2.reconstruct();
			for (Solution sNiv3 : lookup(sNiv2, 45, 3,178*2))
			{ // Prendre les 3 meilleures solutions et refaire un tabou dessus
				sNiv2.reconstruct();
				
					Solution s = lookup(sNiv3, 25, 3, 178*4).get(0);
					s.reconstruct();

					if (s.getFitness() < sBest.getFitness())
						sBest = s;
			}
			}
		}
		
		System.out.println("\r\n");
		System.out.println("Meilleure solution !");
		System.out.println(sBest);
		System.out.println("Price = "+sBest.calculPrice());
		
		return sBest;
	}
	
	public static void main(String[] args) throws FileNotFoundException {
		Execute exec = new ExecuteV3();
		
		File f = exec.chooseFile();
		if (f == null) return;
		
		new File("dataOut").mkdir(); //dossier dataOut
		new File("dataOut/executeV3").mkdir(); //dossier execute v2
		new File("dataOut/executeV3/"+f.getName()).mkdir(); //dossier avec nom fichier
		
		for (int i = 0; i < 3; ++i)
		{	
			SimpleDateFormat filePattern = new SimpleDateFormat("ddMMyyyy_HHmmss");
			String filename = filePattern.format(new Date()) + ".txt";
			
			File file = new File("dataOut/executeV3/"+f.getName()+"/"+filename); //fichier avec date
			
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
