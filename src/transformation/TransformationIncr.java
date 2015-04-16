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

	public TransformationIncr(int transformation, TypeImage tI, int idPattern) {
		super();
		this.transformation = transformation;
		this.tI = tI;
		this.idPattern = idPattern;
	}

	public Solution transform (Solution s)
	{
		long timeStart = System.currentTimeMillis();
		
		Pattern p = s.getPatterns()[idPattern];
		int nbTypeImage = p.getImgsNb().get(tI);

		if ((transformation < 0 && nbTypeImage > 0) || 
				(transformation > 0 && (((int) (Pattern.getSurface() / tI.getSurface())) > nbTypeImage + transformation)))
		{ 
			Map<TypeImage, Integer> newMapti = new HashMap<>(p.getImgsNb());
			newMapti.replace(tI, nbTypeImage+transformation);
			
			Pattern newPattern = new Placement(newMapti).place();
			if (newPattern != null) 
			{
				Pattern[] newListPatterns = new Pattern[s.getPatterns().length];
				for (int i=0; i<s.getPatterns().length; i++)
				{
					newListPatterns[i] = s.getPatterns()[i];
				}
				
				newListPatterns[idPattern] = newPattern;
				
				//new Solution
				return new Solution(s.getTypesImages(), newListPatterns, timeStart-System.currentTimeMillis());
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

	public TypeImage gettI() {
		return tI;
	}

	public void settI(TypeImage tI) {
		this.tI = tI;
	}

	public int getIdPattern() {
		return idPattern;
	}

	public void setIdPattern(int idPattern) {
		this.idPattern = idPattern;
	}

}
