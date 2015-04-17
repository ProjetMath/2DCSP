package main;

import image.TypeImage;

import java.util.ArrayList;
import java.util.List;

import tabou.Tabou;

/**
 * Execution general
 *
 */
public class Execute {
	private final int nbPattern;
	
	public Execute(int nbPattern) {
		this.nbPattern = nbPattern;
	}
	
	public Solution execute() {
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
		
		//Generer une solution aleatoire
		GenerateRandomSolution generator = new GenerateRandomSolution(tit);
		Solution sRandom = generator.generate(nbPattern);
		
		//System.out.println(sRandom);
		//System.out.println("prix sRandom = "+sRandom.calculPrice());
		
		Tabou algoTabou = new Tabou(1000, 1500); //taille liste tabou, nombre de level
		Solution bestSol = algoTabou.generatedTabou(sRandom);
		//System.out.println(bestSol);
		
		return bestSol;
	}

	public static void main(String[] args) {
		System.out.println("Welcome on board ! .. Someone !\r\n");

		//Avec 1 pattern
		//double prix1 = new Execute(1).execute();
		//System.out.println("\r\nPrix avec 1 pattern = "+prix1+"\r\n"); //3518
		
		//Avec 2 pattern
		//double prix2 = new Execute(2).execute();
		//System.out.println("\r\nPrix avec 2 pattern = "+prix2+"\r\n"); //848
		
		//Avec 3 pattern 
		//double prix3 = new Execute(3).execute();
		//System.out.println("\r\nPrix avec 3 pattern = "+prix3+"\r\n"); //826
		
		//Avec 4 pattern
		//double prix4 = new Execute(4).execute();
		//System.out.println("\r\nPrix avec 4 pattern = "+prix4+"\r\n"); //839
		
		//NbMax = NbMaxImage
		for (int i=1; i < 5; ++i)
		{
			Solution minSol = null;
			
			for (int k=0; k<1000; k++)
			{
				Solution s = new Execute(i).execute();
				if (minSol == null || (minSol != null && s.calculPrice() < minSol.calculPrice()))
					minSol = s;
			}
			
			System.out.println(minSol);
		}
	}

}
