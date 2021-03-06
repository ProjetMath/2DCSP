package transformation;

import image.TypeImage;

import java.util.HashMap;
import java.util.Map;

import main.Pattern;
import main.Solution;
import placement.Placement;

public class TransformationIncr implements Transformation {
	private int transformation;
	private TypeImage tI;
	private int idPattern;

	/**
	 * 
	 * @param transformation
	 * @param tI
	 * @param idPattern
	 */
	public TransformationIncr(int transformation, TypeImage tI, int idPattern) {
		super();
		this.transformation = transformation;
		this.tI = tI;
		this.idPattern = idPattern;
	}

	/**
	 * Transforme la solution en param�tre avec cette transformation
	 * @param s Solution � transformer
	 * @return Solution transform�e .. null
	 */
	public Solution transform(Solution s)
	{
		Pattern p = s.getPatterns()[idPattern]; //recuperer pattern
		int nbTypeImage = p.getImgsNb().get(tI); //recuperer le nombre du type d'image correspondant dans le pattern

		/**
		 * Quand transformation = -1 et que nbTypeImage = 1 => nbTypeImage = 0
		 * Au lieu de v�rifier qu'aucun type d'image n'a �t� supprim� de la solution, 
		 * le calcul de la fitness de la solution incorrect d�clenche une exception 
		 * et la fitness est �gal � la plus grande valeur possible ce qui �lime la solution
		 */
		
		if ((transformation < 0 && nbTypeImage > 0) || 
				(transformation > 0 && (((int) (Pattern.getSurface() / tI.getSurface())) > nbTypeImage + transformation)))
		{ 
			Map<TypeImage, Integer> newMapti = new HashMap<>(p.getImgsNb());
			newMapti.replace(tI, nbTypeImage+transformation); //Modifier le type d'image pour le pattern selon la transformation
			
			Pattern newPattern = new Placement(newMapti).place();
			if (newPattern != null) 
			{ //Si le nouveau pattern cr�� est correct
				//recr�� le nouveau tableau de pattern en modifiant l'�l�ment
				Pattern[] newListPatterns = new Pattern[s.getPatterns().length];
				for (int i=0; i<s.getPatterns().length; i++)
					newListPatterns[i] = s.getPatterns()[i];
				
				newListPatterns[idPattern] = newPattern;
				
				//new Solution
				return new Solution(s.getTypesImages(), newListPatterns, 0, this);
			}		
		}
		return null;	
	}

	public int getTransformation() {
		return transformation;
	}

	public void setTransformation(int transformation) {
		this.transformation = transformation;
	}

	public TypeImage getTI() {
		return tI;
	}

	public void setTI(TypeImage tI) {
		this.tI = tI;
	}

	public int getIdPattern() {
		return idPattern;
	}

	public void setIdPattern(int idPattern) {
		this.idPattern = idPattern;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof TransformationIncr)
		{
			TransformationIncr t = (TransformationIncr) obj;
			return this.transformation == t.getTransformation() && 
					this.tI == t.getTI() && 
					this.idPattern == t.getIdPattern();
		} else 
			return false;		
	}
	
	@Override
	public String toString() {
		String s = "";
		
		s += "Transform = [t="+transformation+", ";
		s += "ti="+tI.getId()+", numPat="+idPattern+"]";
		
		return s;
	}
}
