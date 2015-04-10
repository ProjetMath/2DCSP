package algoPlacement;

import image.TypeImage;

import java.util.List;

import p.Solution;

public class AlgoPlacement {
	private Solution solution;
	private List<TypeImage> baseImages; //liste d'image à placer
	private int nbPatternMax; //nombre maximum de pattern à utiliser
	
	private List<TypeImage> imgsLeft; //images restante (demande > 0)
	
	public AlgoPlacement(List<TypeImage> images, int nbPatternMax) {
		this.baseImages = images;
		this.nbPatternMax = nbPatternMax;
		
		//copie de la demande dans les compteurs
		for (TypeImage i : baseImages)
			imgsLeft.add(i);
	}

	public Solution execute()
	{
		//solution = new Solution(patterns, nbPrintPattern, elapsedTime);
		/*
		 * Placer une image (première de la liste quand liste trié ordre décroissant ?)
		 * Decouper en stockant les rectangles qui représentent l'espace vide
		 */
		int cptPattern = 0; //compteur de pattern
		
		while (imgsLeft.size() > 0)
		{ //tant qu'il reste de la demande en image
			//recherche pattern
			//enlever de la liste les images dont la demande a été atteinte
		}
		
		return solution;
	}
}
