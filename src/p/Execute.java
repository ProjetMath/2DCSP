package p;

import image.TypeImage;

import java.util.ArrayList;
import java.util.List;

import algoPlacement.AlgoPlacement;

/**
 * Execution general
 *
 */
public class Execute {

	public static void main(String[] args) {
		System.out.println("Welcome on board ! .. Someone !");

		List<TypeImage> imagesToPlace = new ArrayList<>();
		//TODO  Charger les images depuis les fichiers de données
		imagesToPlace.add(new TypeImage(0, 30, 24, 246));
		imagesToPlace.add(new TypeImage(1, 56, 13, 562));
		imagesToPlace.add(new TypeImage(2, 22, 14, 1000));
		imagesToPlace.add(new TypeImage(3, 23, 9, 3498));
		
		Pattern.setSize(40, 60); 
		
		//Algo placement TODO
		//TODO Trier les images (grande >> petite)
		int nbPatternMax = 1;
		AlgoPlacement ap = new AlgoPlacement(imagesToPlace, nbPatternMax);
		
		Solution s = ap.execute();
		System.out.println(s);
	}

}
