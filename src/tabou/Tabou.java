package tabou;

import image.TypeImage;

import java.util.Map.Entry;

import main.Solution;
import transformation.Transformation;
import transformation.TransformationIncr;

public class Tabou {
	private int maxLevel;
	private final int maxSizeListTabou;
	private ListTabou listTabou;
	
	/**
	 * Constructor
	 * @param sizeListTabou Taille maximum de la liste tabou à créer
	 * @param maxLevel
	 */
	public Tabou(int maxSizeListTabou, int maxLevel) {
		this.maxSizeListTabou = maxSizeListTabou;
		this.maxLevel = maxLevel;
	}
	
	/**
	 * Executer l'algorithme tabou à partir d'une solution initial
	 * @param s1 : solution initial
	 * @return meilleur solution
	 */
	public Solution generatedTabou(Solution s1)
	{
		listTabou = new ListTabou(maxSizeListTabou);
		Solution sMin = s1;
		Solution sCurrent = s1;
		
		//Condition d'arrêt = nombre maximum de noeud parcouru
		for (int i=0; i < maxLevel; i++)
		{ 
			Solution bestNeighbor = bestNeighbor(sCurrent);
			if (bestNeighbor == null)
				break;						// plus de voisins
			
			sCurrent = bestNeighbor;
			
			if (sCurrent.getFitness() > sMin.getFitness())
			{ //Remplir liste tabou 
				
				listTabou.add(sCurrent.getFromTransform());
			}
			else 
			{ //meilleur solution que la precedente ne pas modifier la liste tabou
				sMin = sCurrent;
			}	
		}
		return sMin;
	}

	/**
	 * Renvoi le meilleur voisin selon la fitness de la solution passée en paramètre
	 * @param s : Solution de départ
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
					if (!listTabou.contains(transform))
					{
						Solution s1 = transform.transform(s); //transformer la solution 
						if (s1 != null)
						{
							//System.out.println("f="+s1.getFitness());
							
							//Si meilleur voisin trouvé le remplacer
							if (bestNeightbor == null 
									|| (bestNeightbor != null && s1.getFitness() < bestNeightbor.getFitness()))
								bestNeightbor = s1;
						}
					} 
					
					t = -t; //-1
				}
			}
		}
		
		return bestNeightbor;
	}

	public int getMaxLevel() {
		return maxLevel;
	}

	public void setMaxLevel(int maxLevel) {
		this.maxLevel = maxLevel;
	}
}
