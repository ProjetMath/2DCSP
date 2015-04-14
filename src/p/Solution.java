package p;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Solution {
	private List<Pattern> patterns;
	//private int[] nbPrintPattern; //Nombre impression du pattern
	private Map<Pattern, Integer> nbPrintPattern; //nombre d'impression pour chaque pattern
	private long elapsedTime; //temps pour obtenir la solution
	private static double globalFitness = 0.0;
	private double localFitness; 

	public Solution(List<Pattern> patterns, long elapsedTime) {
		super();
		this.patterns = patterns;
		this.nbPrintPattern = new HashMap<Pattern, Integer>();
		this.elapsedTime = elapsedTime;
		this.localFitness = 0;
	}
	public List<Pattern> getPatterns(){
		return this.patterns;
	}
	public long getElapsedTime() {
		return elapsedTime;
	}
	
	public void setElapsedTime(long elapsedTime) {
		this.elapsedTime = elapsedTime;
	}
	
	public double getLocalFitness() {
		return localFitness;
	}
	
	public void setLocalFitness(double localFitness) {
		this.localFitness = localFitness;
	}
	
	public static double getGlobalFitness() {
		return globalFitness;
	}
	
	public double calculFitness()
	{
		localFitness = 0;
		
		if (globalFitness > localFitness)
			localFitness = globalFitness;
		
		return localFitness;
	}
	
	
	@Override
	public String toString() {
		String s = "Solution = {\r\n";
		
		for (int i=0; i<patterns.size(); ++i)
		{
			s += patterns.get(i)+"";
			s += ", [printed= "+nbPrintPattern.get(patterns.get(i))+", t="+elapsedTime+", lf="+localFitness+", gf="+globalFitness+"]"; 
			s += "\r\n"; 
			//example : "p0 : 10 [t=2.2, lf=4.3, gf=4.3]"
		} 
		
		s += "\r\n}";
		
		return s;
	}
}
