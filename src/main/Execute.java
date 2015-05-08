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
	private List<Solution> lookup(Solution first, int ltPercent, int sizeListElite, int nbIteration) {
		if (ltPercent <= 0) ltPercent = 1;
		if (sizeListElite <= 0) sizeListElite = 1;
		
		int nbTransPossible = first.getPatterns().length*first.getTypesImages().size()*2;
		int sizeListTabou = nbTransPossible * ltPercent / 100;
		
		System.out.println("#Tabou demarrage : fSBase = "+first.getFitness()+", nb pattern = "+first.getPatterns().length+", nbTransPossible = "+nbTransPossible+", taille liste tabou = "+sizeListTabou+" ("+ltPercent+"%), max iteration = "+nbIteration);
		Tabou algoTabou = new Tabou(sizeListTabou, nbIteration, sizeListElite); //taille liste tabou, nombre max de level, taille liste elite
		
		List<Solution> listElit = algoTabou.generatedTabou(first);
		
		System.out.println("#Tabou fin : fSResult = "+listElit.get(0).getFitness()+", nbIteration = "+algoTabou.getNbIteration()+", timeEllapsed = "+algoTabou.getTimeEllapsed());
				
		return listElit;
	}
	
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
		
		//Solution sBest = sRandom;
		/*for (Solution sNiv1 : lookup(sRandom, ltPercentNiv1, 3, nbIteNiv1))
		{ //Prendre les 3 meilleures solutions et refaire un tabou dessus
			sNiv1.reconstruct();
			
			Solution s = lookup(sNiv1, ltPercentNiv2, 1, nbIteNiv2).get(0);
			s.reconstruct();
			
			if (s.getFitness() < sBest.getFitness())
				sBest = s;
		}*/
		
		//Solution sBest = lookup(sRandom, 100, 1, 10000).get(0);
		//sBest.reconstruct();
		
		/*for (int i = 1; i <= 2; ++i)
		{
			Solution s = lookup(sBest, 25-i*10, 1, 2500*i).get(0);
			s.reconstruct();
			
			if (s.getFitness() < sBest.getFitness())
				sBest = s;
		}*/
		
		Solution s1 = lookup(sRandom, 100, 1, 10000).get(0).reconstruct();
		Solution sBest = s1;
		for (int i=0; i<5; ++i)
		{
			Solution s2 = lookup(s1, 10, 1, 500).get(0).reconstruct();
			
			for (int j=0; j<3; ++j)
			{
				Solution s = lookup(s2, 10, 1, 500).get(0).reconstruct();
				
				if (s.getFitness() < sBest.getFitness())
					sBest = s;
			}
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
		
		//for (int i = 0; i < 3; ++i)
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
				
				//JFrame frame = new Affichage(bestSol,new Dimension((int)Pattern.getWidth(),(int)Pattern.getHeight()));
				//Affichage.affiche(frame);
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
	}

}
