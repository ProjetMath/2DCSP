package p;

import images.Item;
import images.TypeImage;

import java.util.List;

public class Solution {
	private List<Pattern> patterns;
	private int[] nbPrintPattern; //Nombre impression du pattern
	
	private long elapsedTime; //temps pour obtenir la solution
	
	private static double globalFitness;
	
	private double localFitness; 

	public Solution(List<Pattern> patterns, int[] nbPrintPattern,
			long elapsedTime, double localFitness) {
		super();
		this.patterns = patterns;
		this.nbPrintPattern = nbPrintPattern;
		this.elapsedTime = elapsedTime;
		this.localFitness = localFitness;
	}
	
	public void calculFitness()
	{
		for (Pattern p : patterns)
		{
			List<Item> images = p.getImages();
			
		}
			
		localFitness = 0;
		
		if (globalFitness > localFitness)
			localFitness = globalFitness;
	}
}
