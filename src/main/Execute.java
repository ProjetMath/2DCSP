package main;

import image.TypeImage;

import java.util.ArrayList;
import java.util.List;

import javax.lang.model.element.NestingKind;

import tabou.Tabou;

/**
 * Execution general
 *
 */
public class Execute {
	public static void main(String[] args) {
		System.out.println("Welcome on board ! .. Someone !\r\n");

		//Taille liste = nb de type d'image * 2
		//1 pattern = 3518
		//2 pattern = 848
		//3 pattern = 826
		//4 pattern = 839
		
		List<TypeImage> imagesToPlace = new ArrayList<>();
		
		//TODO  Charger les images depuis les fichiers de données
		imagesToPlace.add(new TypeImage(0, 30, 24, 246));
		imagesToPlace.add(new TypeImage(1, 56, 13, 562));
		imagesToPlace.add(new TypeImage(2, 22, 14, 1000));
		imagesToPlace.add(new TypeImage(3, 23, 9, 3498));
		
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
		
		Pattern.setSize(40, 60); 
		Pattern.setPrice(20);
		
		//Chercher la solution 
		//Generer une solution aleatoire
		int nbPattern = imagesToPlace.size();
		GenerateRandomSolution generator = new GenerateRandomSolution(tit);
		Solution sRandom = generator.generate(nbPattern);
		
		//System.out.println(sRandom);
		//System.out.println("prix sRandom = "+sRandom.calculPrice());
		
		//Premier algo tabou rapide
		Tabou algoTabou = new Tabou(nbPattern*imagesToPlace.size()*2, 100000); //taille liste tabou, nombre de level
		
		Solution bestSol = algoTabou.generatedTabou(sRandom);
		
		System.out.println("nbPat="+nbPattern+", nbIteration = "+algoTabou.getNbIteration());
		
		System.out.println(bestSol.reconstruct());
		System.out.println("Price = "+bestSol.calculPrice());
	}

}
