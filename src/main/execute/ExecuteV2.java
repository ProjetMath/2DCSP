package main.execute;

import image.TypeImage;

import java.awt.Dimension;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.swing.JFrame;

import main.Execute;
import main.Pattern;
import main.Solution;
import vue.Affichage;

public class ExecuteV2 {
	public static void main(String[] args) throws FileNotFoundException {
		PrintStream stdout = System.out;
		System.out.println("Recherche d'une solution avec plusieurs tabou (en commencant avec un tabou rapide) en reprenant la meilleure solution ...\r\n");
		try {
			/*
			 * Ask parameter 
			 */
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			int tailleBoucle = 0;
			do {
				try {
					System.out.print("Nombre de tabou [1-Inf] : ");
					String line = br.readLine();
					tailleBoucle = Integer.valueOf(line);
				} catch (Exception e) 
				{
					tailleBoucle = 0;
				}
			} while(tailleBoucle <= 0);
			
			int tailleTabou = 0;
			do {
				try {
					System.out.print("Taille de la liste tabou (pourcentage du nombre total de transformation max) [1;100] : ");
					String line = br.readLine();
					tailleTabou = Integer.valueOf(line);
				} catch (Exception e) 
				{
					tailleTabou = 0;
				}
			} while(tailleTabou <= 0  || tailleTabou > 100);
			 
			
			int nbIteration = 0;
			do {
				try {
					System.out.print("Nombre d'itération [1;Inf] : ");
					String line = br.readLine();
					nbIteration = Integer.valueOf(line);
				} catch (Exception e) 
				{
					nbIteration = 0;
				}
			} while(nbIteration <= 0);
		
			Execute exec = new Execute();
		
			File f = exec.chooseFile();
			if (f == null) return;
			
			new File("dataOutput").mkdir(); //dossier dataOut
			String nameFile = f.getName().substring(0, f.getName().length()-4);
			new File("dataOutput/"+nameFile).mkdir(); //dossier avec nom fichier
			
			SimpleDateFormat filePattern = new SimpleDateFormat("ddMMyyyy_HHmmss");
			String filename = filePattern.format(new Date()) + "__"+nameFile+".txt";
			
			File file = new File("dataOutput/"+nameFile+"/"+filename); //fichier avec date
			System.out.println("\r\nLe fichier "+f.getName()+" est en traitement");
			System.out.println("Wait ..");
			PrintStream printStream = new PrintStream(file);
			System.setOut(printStream);
	
			long startTime = System.currentTimeMillis();
			
			System.out.println("\r\n");
			System.out.println("Le fichier "+f.getName()+" va etre traité\r\n");
		
			List<TypeImage> imagesToPlace = exec.setup(f); //chargement fichier, ..
			
			Solution sRandom = exec.firstSolution(imagesToPlace);
			
			System.out.println("\r\nRecherche de solution ..");
			
			Solution sBest = exec.lookup(sRandom, 100, 1, 10000).get(0).reconstruct();;
			for (int i=0; i<tailleBoucle; ++i)
			{
				Solution s = exec.lookup(sBest, tailleTabou, 1, nbIteration).get(0).reconstruct();
				
				if (s.getFitness() <= sBest.getFitness())
				{
					sBest = s;
				}
			}
			
			System.out.println("\r\n");
			System.out.println("Meilleure solution !");
			System.out.println(sBest);
			System.out.println("Price = "+sBest.calculPrice());		
			
			System.out.println("\r\nTotal time : "+(System.currentTimeMillis()-startTime));
			System.setOut(stdout); //reset
			System.out.println("\r\nTotal time : "+(System.currentTimeMillis()-startTime));
			System.out.println("File in 'dataOut/"+nameFile+"/"+filename+"'");
			
			JFrame frame = new Affichage(sBest,new Dimension((int)Pattern.getWidth(),(int)Pattern.getHeight()));
			Affichage.affiche(frame);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
}


