package main;

import image.TypeImage;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.math3.optim.MaxIter;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.linear.LinearConstraint;
import org.apache.commons.math3.optim.linear.LinearConstraintSet;
import org.apache.commons.math3.optim.linear.LinearObjectiveFunction;
import org.apache.commons.math3.optim.linear.NoFeasibleSolutionException;
import org.apache.commons.math3.optim.linear.NonNegativeConstraint;
import org.apache.commons.math3.optim.linear.Relationship;
import org.apache.commons.math3.optim.linear.SimplexSolver;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;

import transformation.Transformation;

public class Solution {
	private Pattern[] patterns;
	private List<TypeImage> typesImages; //types d'images utilis�s dans l'ensemble des patterns
	private Map<Pattern, Integer> nbPrintPattern; //nombre d'impression pour chaque pattern
	private long elapsedTime; //temps pour obtenir la solution
	private double fitness; 
	private Transformation fromTransform;	//Transformation d'ou provient la solution

	public Solution(List<TypeImage> typesImages, Pattern[] patterns, long elapsedTime) {
		super();
		this.patterns = patterns;
		this.typesImages = typesImages;
		this.nbPrintPattern = new HashMap<Pattern, Integer>();
		this.elapsedTime = elapsedTime;
		this.fitness = 0;
		this.fromTransform = null;
		
		calculFitness();
	}
	
	public Solution(List<TypeImage> typesImages, Pattern[] patterns, long elapsedTime, Transformation fromTransform) {
		this(typesImages, patterns, elapsedTime);
		this.fromTransform = fromTransform;
	}
	
	public Pattern[] getPatterns(){
		return this.patterns;
	}
	
	public long getElapsedTime() {
		return elapsedTime;
	}
	
	public void setElapsedTime(long elapsedTime) {
		this.elapsedTime = elapsedTime;
	}
	
	public double getFitness() {
		return fitness;
	}
	
	public Transformation getFromTransform() {
		return fromTransform;
	}

	public void setFromTransform(Transformation fromTransform) {
		this.fromTransform = fromTransform;
	}

	private double calculFitness()
	{
		nbPrintPattern.clear();
		
		// describe the optimization problem : x1 + x2 + x3 + xn 
		//o� xi est le nombre d'impression du pattern i
		int nbPattern = this.getPatterns().length;
		double[] coefficients = new double[nbPattern];
		for (int i=0; i<nbPattern ; ++i)
			coefficients[i] = 1; //init
		
		LinearObjectiveFunction f = new LinearObjectiveFunction(coefficients, 0.0);
		
		//constraints selon les quantit�s � distribuer
		Collection<LinearConstraint> constraints = new ArrayList<>();
		for (TypeImage ti : typesImages)
		{
			double[] coefs = new double[nbPattern];
			
			for (int i=0; i < nbPattern; ++i) 
				coefs[i] = patterns[i].getImgsNb().get(ti);
			
			constraints.add(new LinearConstraint(coefs, Relationship.GEQ, ti.getDemand()));
		}
		
		//coefs patterns doivent �tre sup�rieurs � 0 (on veut que tous les patterns sont imprim�s au moins 1 fois)
		for (int i=0; i < nbPattern; ++i) 
		{
			double[] coefsP = new double[nbPattern];
			for (int j=0; j < nbPattern; ++j) 
			{
				if (i == j)
					coefsP[j] = 1;
				else
					coefsP[j] = 0;
			}
			
			constraints.add(new LinearConstraint(coefsP, Relationship.GEQ, 1));
		}
		
		// create and run the solver
		SimplexSolver solver = new SimplexSolver();
		
		try {
			PointValuePair optSolution = solver.optimize(new MaxIter(100),
														f, 
														new LinearConstraintSet(constraints),               
														GoalType.MINIMIZE, 
														new NonNegativeConstraint(true)
														);

			double[] solution = optSolution.getPoint();
			for (int i=0; i<solution.length; ++i)
			{
				int s = (int) Math.ceil(solution[i]); //prendre la valeur superieur
				nbPrintPattern.put(patterns[i], s);
			}
		
			fitness = optSolution.getValue();
		} catch (NoFeasibleSolutionException ex) {
			fitness = Double.MAX_VALUE;
		}
		
		return fitness;
	}
	
	public Map<Pattern, Integer> getNbPrintPattern() {
		return nbPrintPattern;
	}
	
	public List<TypeImage> getTypesImages() {
		return typesImages;
	}
	
	/**
	 * Calcul du prix r�el d'une solution
	 * @return double : prix calcul�
	 */
	public double calculPrice() {
		double total = 0.0;
		
		total += Pattern.getPrice() * (double)(patterns.length); //prix des patterns 
	
		for (Entry<Pattern, Integer> e : nbPrintPattern.entrySet())
			total += (double)e.getValue(); //some des impressions des patterns (impression coute 1)
		
		return total;
	}
	
	@Override
	public String toString() {
		String s = "Solution = [pW="+Pattern.getWidth()+", pH="+Pattern.getHeight()+", t="+elapsedTime+", f="+fitness+", price="+calculPrice()+"] {\r\n";
		
		for (int i=0; i<patterns.length; ++i)
		{
			s += i+": "+patterns[i]+"";
			s += ", [printed= "+nbPrintPattern.get(patterns[i])+"]"; 
			s += "\r\n"; 
			//example : "p0 : 10 [t=2.2, lf=4.3, gf=4.3]"
		} 
		
		s += "\r\n}";
		
		return s;
	}
}
