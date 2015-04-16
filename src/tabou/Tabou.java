package tabou;

import image.TypeImage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import main.GenerateRandomSolution;
import main.Pattern;
import main.Solution;
import placement.Placement;
import transformation.Transformation;
import transformation.TransformationIncr;

public class Tabou {
	private int maxLevel;
	
	public Tabou(int maxLevel) {
		// TODO Auto-generated constructor stub
		this.maxLevel = maxLevel;
	}
	
	public Solution generatedTabou(Solution s1)
	{
		List<Transformation> tabou = new ArrayList<>();
		Solution sMin = s1;
		Solution sCurrent = s1;
		
		for (int i=0; i < maxLevel; i++)
		{
			Solution bestNeighbor = bestNeighbor(sCurrent);
			if (bestNeighbor == null)
				break;						// plus de voisins
			
			sCurrent = bestNeighbor;
			sCurrent.calculFitness();
			
			if (sCurrent.getFitness() > sMin.getFitness())
			{
				//Remplir liste tabou avec s1
				tabou.add(sCurrent.getFromTransform());
			}
			else 
			{
				sMin = sCurrent;
			}	
		}
		return sMin;
	}

	/**
	 * Renvoi le meilleur voisin selon la fitness de la solution passée en paramètre
	 * @param s : Solution de départ
	 * @param listTransformForbidden : liste des transformations interdite
	 * @return Solution : meilleur voisin selon la fitness
	 */
	public Solution bestNeighbor(Solution s)
	{
		Solution bestNeightbor = null; 
		
		for(int i=0; i < s.getPatterns().length ; i++)
		{
			//Pour chaque type d'image faire +1/-1 et creer une nouvelle solution
			for (Entry<TypeImage, Integer> e : s.getPatterns()[i].getImgsNb().entrySet())
			{
				int t = 1; //+1
				for (int incr=0; incr<2; incr++)
				{ //Tour 1 = +1 et tour 2 = -1
					Transformation transform = new TransformationIncr(t, e.getKey(), i);
					Solution s1 = transform.transform(s);
					if (s1 != null)
					{
						s1.calculFitness();

						//System.out.println("f="+s1.getFitness());
						
						//Si meilleur voisin trouvé le remplacer
						if (bestNeightbor == null 
								|| (bestNeightbor != null && s1.getFitness() < bestNeightbor.getFitness()))
							bestNeightbor = s1;
					}
					
					t = -t; //-1
				}
			}
		}
		
		return bestNeightbor;
	}
	
	
	/**
	 * Main test algo tabou
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println("hey");
	}

	public int getMaxLevel() {
		return maxLevel;
	}

	public void setMaxLevel(int maxLevel) {
		this.maxLevel = maxLevel;
	}
}
