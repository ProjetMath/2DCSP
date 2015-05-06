package main;

import image.TypeImage;

import java.awt.Dimension;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.JFrame;

import tabou.Tabou;
import vue.Affichage;

/**
 * Execution general
 *
 */
public class Execute {
	public Execute() {
		// TODO Auto-generated constructor stub
	}
	
	/*
	 * Interface pour charger le fichier
	 */
	public File chooseFile()
	{
		JFileChooser jfc = new JFileChooser("./data");
		int returnB = jfc.showOpenDialog(null);
		if (returnB != JFileChooser.APPROVE_OPTION) return null;
		
		return jfc.getSelectedFile();
	}
	
	/*
	 * Chargement du fichier et trie des images
	 */
	private List<TypeImage> setup(File f) {
		List<TypeImage> imagesToPlace = LoadFile.lectureFichier(f.getAbsolutePath());
		
		//trier les images
		List<TypeImage> tempListTi = new ArrayList<TypeImage>(imagesToPlace);
		List<TypeImage> tit = new ArrayList<TypeImage>();
		while (tit.size() <= imagesToPlace.size())
		{
			if (tempListTi.size() == 0) break;
			
			TypeImage bigest = tempListTi.get(0);
			for (TypeImage ti : tempListTi)
				if (bigest.getSurface() < ti.getSurface())
					bigest = ti;
			
			tempListTi.remove(bigest);
			tit.add(bigest);
		}
		
		System.out.println("Pattern x = "+Pattern.getWidth()+" / Pattern y = "+Pattern.getHeight());
		
		for (TypeImage ti : imagesToPlace) 
		{
			System.out.println(ti);
		}
		
		Pattern.setPrice(20);
		
		return tit;
	}
		
	/*
	 * Recherche de la meilleure solution
	 */
	private List<Solution> lookup(Solution first, int level, int sizeListElite, int nbIteration) {
		if (level <= 0) level = 1;
		if (sizeListElite <= 0) sizeListElite = 1;
		
		//Toutes les transactions possibles au level 1 pour avoir une recherche rapide, 
		//plus le level augmente plus la taille de la liste tabou diminue (/level) et la recherche deviens précise
		
		int nbTransPossible = first.getPatterns().length*first.getTypesImages().size()*2;
		int sizeListTabou = nbTransPossible / level;
		
		System.out.println("#Tabou demarrage : nbTransPossible = "+nbTransPossible+", taille liste tabou = "+sizeListTabou+", max iteration = "+nbIteration);
		Tabou algoTabou = new Tabou(nbTransPossible / level, nbIteration, sizeListElite); //taille liste tabou, nombre max de level, taille liste elite
		
		List<Solution> listElit = algoTabou.generatedTabou(first);
		
		System.out.println("#Tabou fin : nbIteration = "+algoTabou.getNbIteration()+", timeEllapsed = "+algoTabou.getTimeEllapsed());
				
		return listElit;
	}
	
	/*
	 * Fonctionnement global
	 */
	public Solution execute(File f) throws Exception {	
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
		/*for (int nbPattern =  sRandom.getPatterns().length; ; nbPattern++)
		{
			System.out.println("Nombre pattern = "+nbPattern);
			
			
			Solution sb = lookup(sRandom, nbPattern, 1, 1000).get(0).reconstruct();
			
			System.out.println(sb);
			if (sBest == null || (sBest != null && sb.calculPrice() < sBest.calculPrice()))
			{
				sBest = sb;
				if (sb.getPatterns().length < nbPattern)
					nbPattern = sb.getPatterns().length-1;
			} else 
				break;
		}*/
		
		for (Solution sNiv1 : lookup(sRandom, 1, 3, 10000))
		{
			sNiv1.reconstruct();
			
			Solution s = lookup(sNiv1, sNiv1.getPatterns().length, 1, 2000).get(0);
			s.reconstruct();
			
			if (s.getFitness() < sBest.getFitness())
				sBest = s;
		}
		
		System.out.println("\r\n");
		System.out.println("Meilleure solution !");
		System.out.println(sBest);
		System.out.println("Price = "+sBest.calculPrice());
		
		return sBest;
	}
	
	public static void main(String[] args) throws FileNotFoundException {
		Execute exec = new Execute();
		
		File f = exec.chooseFile();
		if (f == null) return;
		
		new File("dataOut").mkdir(); //dossier dataOut
		new File("dataOut/"+f.getName()).mkdir(); //dossier avec nom fichier
		
		for (int i = 0; i < 5; ++i)
		{
			SimpleDateFormat filePattern = new SimpleDateFormat("ddMMyyyy_HHmmss");
			String filename = filePattern.format(new Date()) + ".txt";
			
			File file = new File("dataOut/"+f.getName()+"/"+filename); //fichier avec date
			
			PrintStream printStream = new PrintStream(file);
			System.setOut(printStream);
	
			try {
				long startTime = System.currentTimeMillis();
				
				Solution bestSol = exec.execute(f);			
				
				System.out.println("\r\nTotal time : "+(System.currentTimeMillis()-startTime));
				//143 a 4 p
				
				//JFrame frame = new Affichage(bestSol,new Dimension((int)Pattern.getWidth(),(int)Pattern.getHeight()));
				//Affichage.affiche(frame);
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
	}

}
