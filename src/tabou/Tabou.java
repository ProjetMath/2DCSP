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

	/**
	 * Renvoi le meilleur voisin selon la fitness de la solution passée en paramètre
	 * @param s : Solution de départ
	 * @param listTransformForbidden : liste des transformations interdite
	 * @return Solution : meilleur voisin selon la fitness
	 */
	public Solution bestNeighbor(Solution s)//, List<TransformForbidden> listTransformForbidden)
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
	 * Création d'un nouveau voisin en modifiant le nombre d'un type d'image dans un pattern 
	 * selon la transformation en paramètre
	 * @param s : Solution à transformer
	 * @param p : Pattern dans la solution à transformer
	 * @param eTypeImage : Type d'image dans le à transformer
	 * @param transformation : + ou -
	 * @return Solution correspondant au nouveau voisin (null si voisin incorrect)
	 */
	private Solution newNeighbor(Solution s, Pattern p, Entry<TypeImage, Integer> eTypeImage, int transformation)
	{
		long timeStart = System.currentTimeMillis();
		
		//TODO verifier si transformation n'est pas interdite à partir de la liste tabou
		if ((transformation < 0 && eTypeImage.getValue() > 0) || 
				(transformation > 0 && (((int) (Pattern.getSurface() / eTypeImage.getKey().getSurface())) > eTypeImage.getValue() + transformation)))
		{ 
			Map<TypeImage, Integer> newMapti = new HashMap<>(p.getImgsNb());
			newMapti.replace(eTypeImage.getKey(), eTypeImage.getValue()+transformation);
			
			Pattern newPattern = new Placement(newMapti).place(p.getId());
			if (newPattern != null) 
			{
				List<Pattern> newListPatterns = new ArrayList<>();
				for (Pattern pat : s.getPatterns())
				{//copier la liste des patterns sauf celui qui correspont à celui modifié
					if (!pat.equals(newPattern))
						newListPatterns.add(pat);
				}
				newListPatterns.add(newPattern); //ajouter le pattern modifié
				
				//new Solution
				return new Solution(s.getTypesImages(), newListPatterns, timeStart-System.currentTimeMillis());
			}
		}
		
		return null;
	}
	
	/**
	 * Main test algo tabou
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println("hey");
	}
}
