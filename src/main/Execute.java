package main;

import image.TypeImage;

import java.awt.Dimension;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.JFrame;

import tabou.ListElite;
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
	private File chooseFile()
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
	 * Generation de la première solution
	 */
	private Solution generateFirst(List<TypeImage> imagesToPlace) {
		//Generer une solution aleatoire
		int nbPattern = imagesToPlace.size();
		GenerateRandomSolution generator = new GenerateRandomSolution(imagesToPlace);
		Solution sRandom = generator.generate(nbPattern);
		
		sRandom = sRandom.reconstruct();
		
		return sRandom;
	}
	
	/*
	 * Recherche de la meilleure solution
	 */
	private List<Solution> lookup(Solution first, int level, int sizeListElite) {
		if (level <= 0) level = 1;
		if (sizeListElite <= 0) sizeListElite = 1;
		
		//Toutes les transactions possibles au level 1 pour avoir une recherche rapide, 
		//plus le level augmente plus la taille de la liste tabou diminue (/level) et la recherche deviens précise
		
		int nbTransPossible = first.getPatterns().length*first.getTypesImages().size()*2;
		
		Tabou algoTabou = new Tabou(nbTransPossible / level, 100000, sizeListElite); //taille liste tabou, nombre max de level, taille liste elite
		
		List<Solution> listElit = algoTabou.generatedTabou(first);
		
		System.out.println("#Tabou# nbIteration = "+algoTabou.getNbIteration());
				
		return listElit;
	}
	
	private Solution lookup(Solution first)
	{
		return this.lookup(first, 1, 1).get(0);
	}
	
	/*
	 * Fonctionnement global
	 */
	public Solution execute() throws Exception {
		File f = this.chooseFile();
		if (f == null) return null;
	
		System.out.println("Le fichier "+f.getName()+" va etre traité");
	
		List<TypeImage> imagesToPlace = this.setup(f); //chargement fichier, ..
		
		System.out.println("\nPress enter to continue ...");
		System.in.read();
		System.out.println();
		
		Solution sRandom = this.generateFirst(imagesToPlace); //generation première solution 
		
		System.out.println("Solution aléatoire : ");
		System.out.println(sRandom);
		System.out.println("prix sRandom = "+sRandom.calculPrice());
				
		Solution bestSol = this.lookup(sRandom); //1, 1
		
		System.out.println(bestSol);
		System.out.println();
		System.out.println("Price = "+bestSol.calculPrice());
		
		return bestSol;
	}
	
	public static void main(String[] args) {
		System.out.println("Welcome on board ! .. Someone !\r\n");
		
		try {
			Solution bestSol = new Execute().execute();			
			
			//JFrame frame = new Affichage(bestSol,new Dimension((int)Pattern.getWidth(),(int)Pattern.getHeight()));
			//Affichage.affiche(frame);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

}
