package main;

import image.TypeImage;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import javax.swing.JFrame;

import placement.Placement;
import vue.Affichage;

public class GenerateRandomSolution {
	private final List<TypeImage> tImages;	
	private int nbMaxTryPlacePattern;
	private int nbMaxTryGenerateSolution;
	
	public GenerateRandomSolution(List<TypeImage> tImages) {
		this.tImages = tImages;
		this.nbMaxTryPlacePattern = 1000;
		this.nbMaxTryGenerateSolution = 1000;		
	}
	
	public GenerateRandomSolution(List<TypeImage> tImages, int maxTryPlacePattern, int maxTryGenerateSolution) {
		this(tImages);
		
		this.nbMaxTryPlacePattern = maxTryPlacePattern;
		this.nbMaxTryGenerateSolution = maxTryGenerateSolution;
	}
	
	public Solution generate(int nbMaxPat) {
		long timeStart = System.currentTimeMillis();
		
		Pattern[] listPattern =  new Pattern[nbMaxPat];
		//création d'une première solution aléatoire
		Random rand = new Random();
		
		//System.out.println("#GenerateSolution# liste image size = "+tImages.size()+", maxTypePerPattern size = "+maxTypePerPattern.length);
		int nbTryGenSol = 0;
		for (;;)
		{
			int[] cptTypeImage = new int[tImages.size()]; //To check if every type of picture has been place at least once
			
			for(int i=0; i<nbMaxPat; ++i)
			{
				//System.out.println("#GenerateSolution# Debut "+i+" liste pattern size="+listPattern.length);
				
				Pattern p = null;
				int nbTryPlacePattern = 0; //compter le nombre d'essai pour creer le pattern
				
				do
				{
					//random nb image par type image dans pattern
					Map<TypeImage, Integer> imgsNb = new LinkedHashMap<TypeImage, Integer>();
					int cptAtLeastOne = tImages.size()-1;
					double spaceFree = Pattern.getSurface();
					if (nbMaxPat == 1)
					{ //Cas special generaton avec 1 pattern => tous les types d'images doivent être dans ce pattern
						for (TypeImage ti : tImages)
							spaceFree -= ti.getSurface();
					}
					
					for (TypeImage ti : tImages)
					{	
						int incr = 0;
						if (cptAtLeastOne == 0 || nbMaxPat == 1) incr = 1; //at least one image place must be place
						
						int max = (int)(spaceFree/ti.getSurface()); //nb image max de ce type dans l'espace libre
						int r = incr;
						if (max > 0)
						{
							r =  rand.nextInt(max+1-incr) + incr;
						}
						
						if (r == 0) cptAtLeastOne--; //aucune image => decrement compteur
						
						imgsNb.put(ti, r);
						spaceFree -= ti.getSurface() * (double)r;
						if (spaceFree < 0) spaceFree = 0;
					}
					
					// Vérifier qu'existe pas déjà 
					boolean exist = false;
					for (Pattern pa : listPattern)
					{	
						if (pa == null)
							break;
						
						//Pas la même taille
						if (imgsNb.size() != pa.getImgsNb().size())
							continue;
						
						//Chaque type d'image a le même nombre d'images
						exist = true;
						for(Entry<TypeImage, Integer> entry : pa.getImgsNb().entrySet())  
						{
							Integer v = imgsNb.get(entry.getKey());
							if (v != entry.getValue())
							{
								exist = false;
								break;
							}
						}
						if (!exist) continue;
						
						//System.out.println("#GenerateSolution# Ce pattern existe déjà !");
						break; //exist !
					}				
					if (exist) continue; //re generation aleatoire d'un autre pattern
					
					//System.out.println("#GenerateSolution# Exist boolean = "+exist);
								
					Placement pl = new Placement(imgsNb);
					p = pl.place();
					
					nbTryPlacePattern++; 
				} while(p == null && nbTryPlacePattern < nbMaxTryPlacePattern);
				
				if (nbTryPlacePattern >= nbMaxTryPlacePattern)
				{
					System.out.println("#GenerateSolution# Place pattern Maximum of "+nbMaxTryPlacePattern+" iteration reached");
					return null;
				}
				
				listPattern[i] = p;
				//System.out.println("#GenerateSolution# New pattern add nbTry = "+nbTryPlacePattern); 
				
				//Incrémenter compteur type d'image pour ce pattern
				int k = 0;
				for (Entry<TypeImage, Integer> e : p.getImgsNb().entrySet())
					cptTypeImage[k++] += e.getValue();
			}
			//System.out.println("#GenerateSolution# End -  creating solution");
			
			boolean checkNbTypeImage = true;
			
			for (int i=0; i<cptTypeImage.length; ++i)
				if (cptTypeImage[i] == 0)
				{
					checkNbTypeImage = false;
					break;
				}
			
			if(checkNbTypeImage) break;
			
			System.out.println("#GenerateSolution# Bad solution");
			nbTryGenSol++;
			if (nbTryGenSol >= nbMaxTryGenerateSolution) 
			{
				System.out.println("#GenerateSolution# Maximum of "+nbTryGenSol+" iteration reached");
				return null;
			}
		}
		
		//Solution
		Solution s = new Solution(tImages, listPattern, System.currentTimeMillis()-timeStart);
		return s;
	}

	public static void main(String[] args) {
		/**
		 * TEST GENERATE RANDOM SOLUTION
		 */
		
		/*
		 * ----1--------------------------
		 * Sans contrainte au moins une image dans un pattern
		 * Et un type d'image pouvais ne pas être dans la solution 
		 * FIXED
		 * 
		 * ----2----------------
		 * Probleme avec la generation quand 1 pattern : tourne en boucle sans trouver de solution
		 *=> ajout compteur nb iteration (placement pattern et solution) pour stopper
		 * 
		 * ----3-----------------
		 * La borne max lors du tirage aleatoire est trop grande pour les petites images alors que des grandes images ont déjà été choisi
		 * ce qui augmente de façon conséquente le nombre d'itération empêchant même de trouvé des solutions avec 1 pattern par exemple
		 * => adapter la generation en fonction du nombre d'image déjà généré (implique liste triée!!)
		 * en fonction de lespace deja occupé
		 * 
		 * ---4---------------
		 * Pour map trié dans l'ordre d'insertion : LinkedHashMap
		 */
		Pattern.setSize(40, 60); 
		final int nbMaxPat = 1;
		
		//list types images
		List<TypeImage> tImages = new ArrayList<>();
		tImages.add(new TypeImage(1, 13, 56, 562));
		tImages.add(new TypeImage(0, 24, 30, 246));
		tImages.add(new TypeImage(2, 14, 22, 1000));
		tImages.add(new TypeImage(3, 9, 23, 3498));	
		
		//trier les images
		List<TypeImage> tempListTi = new ArrayList<TypeImage>(tImages);
		List<TypeImage> tit = new ArrayList<TypeImage>();
		while (tit.size() <= tImages.size())
		{
			if (tempListTi.size() == 0) break;
			
			TypeImage bigest = tempListTi.get(0);
			for (TypeImage ti : tempListTi)
				if (bigest.getSurface() < ti.getSurface())
					bigest = ti;
			
			tempListTi.remove(bigest);
			tit.add(bigest);
		}
		
		Solution s = new GenerateRandomSolution(tit).generate(nbMaxPat);
		if (s != null)
		{	
			//Affichage
			System.out.println(s);
			JFrame frame = new Affichage(s,new Dimension((int)Pattern.getWidth(),(int)Pattern.getHeight()));
			Affichage.affiche(frame);
		} else 
			System.out.println("Aucune solution");
	}

}
