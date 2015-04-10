package algoPlacement;

import image.TypeImage;

import java.util.List;

import p.Solution;

public class AlgoPlacement {
	private Solution solution;
	private List<TypeImage> baseImages; //liste d'image � placer
	private int nbPatternMax; //nombre maximum de pattern � utiliser
	
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
		 * Placer une image (premi�re de la liste quand liste tri� ordre d�croissant ?)
		 * Decouper en stockant les rectangles qui repr�sentent l'espace vide
		 */
		int cptPattern = 0; //compteur de pattern
		
		while (imgsLeft.size() > 0)
		{ //tant qu'il reste de la demande en image
			//recherche pattern
			//enlever de la liste les images dont la demande a �t� atteinte
		}
		
		return solution;
	}
}
