package main;

import image.TypeImage;

import java.util.ArrayList;
import java.util.List;

import tabou.Tabou;
import vue.FenetreChoix;

/**
 * Execution general
 *
 */
public class Execute {
	public static void main(String[] args) {
		System.out.println("Welcome on board ! .. Someone !\r\n");
		
		try {
			// chemin [0] nom [1]
			String [] infoFichier=new FenetreChoix().choisir();
			String cheminFichier=infoFichier[0];
			System.out.println("Le fichier "+infoFichier[1]+" va etre traité");
			List<TypeImage> imagesToPlace = LoadFile.lectureFichier(cheminFichier);
			
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
			
			System.out.println("\nPress enter to continue ...");
			System.in.read();
			System.out.println();
			
			//Chercher la solution 
			//Generer une solution aleatoire
			int nbPattern = imagesToPlace.size();
			GenerateRandomSolution generator = new GenerateRandomSolution(tit);
			Solution sRandom = generator.generate(nbPattern);
			sRandom = sRandom.reconstruct();
			
			System.out.println("Solution aléatoire : ");
			System.out.println(sRandom);
			System.out.println("prix sRandom = "+sRandom.calculPrice());
			
						
			//Premier algo tabou rapide
			Tabou algoTabou = new Tabou(nbPattern*imagesToPlace.size()*2, 100000); //taille liste tabou, nombre de level
			
			Solution bestSol = algoTabou.generatedTabou(sRandom);
			
			System.out.println("nbPat="+nbPattern+", nbIteration = "+algoTabou.getNbIteration());
			
			System.out.println(bestSol.reconstruct());
			System.out.println("Price = "+bestSol.calculPrice());
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

}
