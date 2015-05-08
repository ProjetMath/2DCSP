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
	
	public GenerateRandomSolution(List<TypeImage> tImages) {
		this.tImages = tImages;	
	}
	
	public GenerateRandomSolution(List<TypeImage> tImages, int maxTryPlacePattern, int maxTryGenerateSolution) {
		this(tImages);
	}
	
	public Solution generate(int nbMaxPat) {
		long timeStart = System.currentTimeMillis();
		
		Pattern[] listPattern =  new Pattern[nbMaxPat];
		//création d'une première solution aléatoire
		Random rand = new Random();
	
		//Modification du compteur d'image, utilisation d'une map //TODO
		List<TypeImage> verifTIListe = new ArrayList<TypeImage>();
		for (TypeImage tp : tImages)
			verifTIListe.add(tp);
		
		int cptFailExist = 0; //TODO
		for(int iP=0; iP<nbMaxPat; )
		{
			//System.out.println("#GenerateSolution# Debut "+i+" liste pattern size="+listPattern.length);
			
			Pattern p = null;
			
			//random nb image par type image dans pattern
			Map<TypeImage, Integer> imgsNb = new LinkedHashMap<TypeImage, Integer>();
			int cptFail = 0; //TODO
			do 
			{
				for (TypeImage t : tImages)
					imgsNb.put(t, 0); //remplir la map typeImage => nb 
				
				int cptAtLeastOne = tImages.size()-1; 
				double spaceFree = Pattern.getSurface();
			
				TypeImage tiChecked = null; //num du type d'image dans le tbl cptTypeImage que l'on va placé en premier pour être sûre de le placer
		
				//Checker si un type d'images n'est pas placés au moins 1 fois
				if (!verifTIListe.isEmpty())
				{
					tiChecked = verifTIListe.get(0);
					imgsNb.put(verifTIListe.get(0), 1); //init à 1
					spaceFree -= verifTIListe.get(0).getSurface();
				}
				
				for (TypeImage ti : tImages)
				{	
					int incr = 0;
					
					/*
					 * au moins 1 type d'image doit être placer dans le pattern (si nbPat == 1 on doit placer tout les types images)
					 * ou bien le type d'image selectionné (pour être sûr de le placer dans la solution) doit être forcement placé
					 */
					if (cptAtLeastOne == 0 || nbMaxPat == 1 ||
							ti.equals(tiChecked) || (iP+1 == nbMaxPat && verifTIListe.contains(ti))) incr = 1; 
					
					int max = (int)(spaceFree/ti.getSurface()); //nb image max de ce type dans l'espace libre
					int r = incr;
					if (max > 0)
						r =  rand.nextInt(max+1-incr) + incr;
					
					if (r == 0) cptAtLeastOne--; //aucune image => decrement compteur
					else if (r > 0) 
					{ //Placement
						imgsNb.put(ti, r);
						Placement pl = new Placement(imgsNb);
						p = pl.place();
						if (p == null) 
						{
							imgsNb.put(ti, incr);
							continue;
						}
					}
					
					spaceFree -= ti.getSurface() * (double)r;
					if (spaceFree < 0) spaceFree = 0;
				}
			} while(p == null && (cptFail++) < nbMaxPat*10000); //TODO
			
			if (p == null) return null; //Impossible de former un pattern //TODO
			
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
			if (exist && (cptFailExist++) < 10000 * nbMaxPat) continue; //re generation aleatoire d'un autre pattern //TODO
			if (exist) return null; //TODO
			
			//System.out.println("#GenerateSolution# Exist boolean = "+exist);
			
			listPattern[iP++] = p;
			//System.out.println("#GenerateSolution# New pattern add"); 
						
			//Mettre dans la liste cpt si type d'image present dans ce pattern
			for (Entry<TypeImage, Integer> e : p.getImgsNb().entrySet())
				if (e.getValue() > 0)
					verifTIListe.remove(e.getKey());
		}
		//System.out.println("#GenerateSolution# End -  creating solution");
		
		//Solution
		Solution s = new Solution(tImages, listPattern, System.currentTimeMillis()-timeStart);
		return s;
	}
	
	public static void main(String[] args) {
		/**
		 * TEST GENERATE RANDOM SOLUTION
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
