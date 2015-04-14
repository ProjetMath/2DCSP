package main;

import image.TypeImage;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import javax.swing.JFrame;

import placement.Placement;
import vue.Affichage;

public class GenerateRandomSolution {
	private List<TypeImage> tImages;	
	private int[] maxTypePerPattern;
	
	public GenerateRandomSolution(List<TypeImage> tImages) {
		this.tImages = tImages;
		
		this.init();
	}
	
	private void init() {
		//Maximum pour un type image dans un pattern
		maxTypePerPattern = new int[tImages.size()];
		for(int i=0; i<tImages.size(); ++i)
			maxTypePerPattern[i] = (int) (Pattern.getSurface() / tImages.get(i).getSurface());
	}
	
	public Solution generate(int nbMaxPat) {
		long timeStart = System.currentTimeMillis();
		
		List<Pattern> listPattern =  new ArrayList<>();
		//création d'une première solution aléatoire
		Random rand = new Random();
		
		//System.out.println("#GenerateSolution# liste image size = "+tImages.size()+", maxTypePerPattern size = "+maxTypePerPattern.length);
		for (;;)
		{
			Pattern.resetCpt();
			listPattern.clear();
			int[] cptTypeImage = new int[tImages.size()]; //To check if every type of picture has been place at least once
			
			for(int i=0; i<nbMaxPat; ++i)
			{
				//System.out.println("#GenerateSolution# Debut liste pattern size="+listPattern.size());
				Pattern p = null;
				do
				{
					//random nb image par type image dans pattern
					Map<TypeImage, Integer> imgsNb = new HashMap<TypeImage, Integer>();
					int cptAtLeastOne = tImages.size()-1;
					for(int j=0; j<tImages.size(); ++j)
					{
						int incr = 0;
						if (cptAtLeastOne == 0) incr = 1; //at least one image place must be place
						int r =  rand.nextInt(maxTypePerPattern[j]+1-incr) + incr;
						if (r == 0) cptAtLeastOne--;
						imgsNb.put(tImages.get(j), r);
					}
					
					//System.out.println("#GenerateSolution# imgsNb size = "+imgsNb.size());
					
					// Vérifier qu'existe pas déjà 
					boolean exist = false;
					for (Pattern pa : listPattern)
					{
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
								
					//checker placement
					Placement pl = new Placement(imgsNb);
					p = pl.place();
				} while(p == null);
				
				listPattern.add(p);
				
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
		}
		
		//Solution
		Solution s = new Solution(tImages, listPattern, System.currentTimeMillis()-timeStart);
		return s;
	}

	public static void main(String[] args) {
		/**
		 * TEST GENERATE RANDOM SOLUTION
		 */
		Pattern.setSize(1400, 700); //pattern size
		final int nbMaxPat = 3;
		
		//list types images
		List<TypeImage> tImages = new ArrayList<>();
		tImages.add(new TypeImage(0, 415, 372, 179));
		tImages.add(new TypeImage(1, 312, 307, 192));
		tImages.add(new TypeImage(2, 220, 333, 121));		
		
		Solution s = new GenerateRandomSolution(tImages).generate(nbMaxPat);
		s.calculFitness();
		
		//Affichage
		System.out.println(s);
		JFrame frame = new Affichage(s,new Dimension((int)Pattern.getWidth(),(int)Pattern.getHeight()));
		Affichage.affiche(frame);
	}

}
