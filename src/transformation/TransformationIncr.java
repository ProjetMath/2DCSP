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
	 * Transforme la solution en paramètre avec cette transformation
	 * @param s Solution à transformer
	 * @return Solution transformée .. null
	 */
	public Solution transform(Solution s)
	{
		long timeStart = System.currentTimeMillis();
		
		Pattern p = s.getPatterns()[idPattern]; //recuperer pattern
		int nbTypeImage = p.getImgsNb().get(tI); //recuperer le nombre du type d'image correspondant dans le pattern

		if ((transformation < 0 && nbTypeImage > 0) || 
				(transformation > 0 && (((int) (Pattern.getSurface() / tI.getSurface())) > nbTypeImage + transformation)))
		{ 
			Map<TypeImage, Integer> newMapti = new HashMap<>(p.getImgsNb());
			newMapti.replace(tI, nbTypeImage+transformation); //Modifier le type d'image pour le pattern selon la transformation
			
			Pattern newPattern = new Placement(newMapti).place();
			if (newPattern != null) 
			{ //Si le nouveau pattern créé est correct
				Pattern[] newListPatterns = new Pattern[s.getPatterns().length];
				for (int i=0; i<s.getPatterns().length; i++)
				{
					newListPatterns[i] = s.getPatterns()[i];
				}
				
				newListPatterns[idPattern] = newPattern;
				
				//new Solution
				return new Solution(s.getTypesImages(), newListPatterns, timeStart-System.currentTimeMillis(), this);
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
}
