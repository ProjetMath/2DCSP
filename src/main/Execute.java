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
public abstract class Execute {
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
	protected List<TypeImage> setup(File f) {
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
	protected List<Solution> lookup(Solution first, int ltPercent, int sizeListElite, int nbIteration) {
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
	 * Execute
	 */
	public abstract Solution execute(File f);
}
