package main;

import image.TypeImage;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFileChooser;

import tabou.Tabou;

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
		JFileChooser jfc = new JFileChooser("./dataInput");
		jfc.setRequestFocusEnabled(true);
		int returnB = jfc.showOpenDialog(null);
		if (returnB != JFileChooser.APPROVE_OPTION) return null;
		
		return jfc.getSelectedFile();
	}
	
	/*
	 * Chargement du fichier et trie des images
	 */
	public List<TypeImage> setup(File f) {
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
	 * Generation de la premiere solution
	 */
	public Solution firstSolution(List<TypeImage> imagesToPlace) {
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
		
		return sRandom;
	}
	
	/*
	 * Recherche de la meilleure solution
	 */
	public List<Solution> lookup(Solution first, int ltPercent, int sizeListElite, int nbIteration) {
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
}
