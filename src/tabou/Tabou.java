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

public class Tabou {
	public Tabou() {
		// TODO Auto-generated constructor stub
	}
	
	public Solution generatedTabou(Solution s1)
	{
		// solution initiale s1, liste tabou vide au depart, calculfonction(double), compteur i
		List<Solution> tabou = new ArrayList<>();
		double f = s1.calculFitness();
		Solution sMin = s1;
		
		for (int i=0; i < 4; i++)
		{
			List<Solution> neighbors = new ArrayList<>(); //liste des voisins - sol interdite
			Solution sNeighbor = s1.newNeighbor();
			neighbors.add(sNeighbor);
			
			if (neighbors.size()>0)
			{
				Solution s2; //meilleure solution dans la liste des voisins
				s2.bestNeighbor(s1);
				
				if (s2.calculFitness() > s1.calculFitness())
				{
					//Remplir liste tabou avec s1
					tabou.add(s1);
				}
				else 
				{
					f=s2.getFitness();
					sMin = s2;
				}	
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
		
		for(Pattern p : s.getPatterns())
		{
			//Pour chaque type d'image faire +1/-1 et creer une nouvelle solution
			for (Entry<TypeImage, Integer> e : p.getImgsNb().entrySet())
			{
				int t = 1; //+1
				for (int incr=0; incr<2; incr++)
				{ //Tour 1 = +1 et tour 2 = -1
					Solution s1 = newNeighbor(s, p, e, t);
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
}
