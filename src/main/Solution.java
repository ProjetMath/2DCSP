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
	private List<TypeImage> typesImages; //types d'images utilisés dans l'ensemble des patterns
	private int[] nbPrintPattern; //nombre d'impression pour chaque pattern
	private long elapsedTime; //temps pour obtenir la solution
	private double fitness; 
	private Transformation fromTransform;	//Transformation d'ou provient la solution

	public Solution(List<TypeImage> typesImages, Pattern[] patterns, long elapsedTime) {
		super();
		this.patterns = patterns;
		this.typesImages = typesImages;
		this.nbPrintPattern = new int[patterns.length];
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
		// describe the optimization problem : x1 + x2 + x3 + xn 
		//où xi est le nombre d'impression du pattern i
		int nbPattern = this.getPatterns().length;
		double[] coefficients = new double[nbPattern];
		for (int i=0; i<nbPattern ; ++i)
			coefficients[i] = 1; //init
		
		LinearObjectiveFunction f = new LinearObjectiveFunction(coefficients, 
				(double)this.getPatterns().length*Pattern.getPrice());
		
		//constraints selon les quantités à distribuer
		Collection<LinearConstraint> constraints = new ArrayList<>();
		for (TypeImage ti : typesImages)
		{
			double[] coefs = new double[nbPattern];
			
			for (int i=0; i < nbPattern; ++i) 
			{				
				coefs[i] = patterns[i].getImgsNb().get(ti);
			}
			constraints.add(new LinearConstraint(coefs, Relationship.GEQ, ti.getDemand()));
		}
		
		//coefs patterns doivent être supérieurs à 0 (on veut que tous les patterns sont imprimés au moins 1 fois)
		/*for (int i=0; i < nbPattern; ++i) 
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
		}*/
		
		// create and run the solver
		SimplexSolver solver = new SimplexSolver();
		
		try {
			PointValuePair optSolution = solver.optimize(new MaxIter(9999),
														f, 
														new LinearConstraintSet(constraints),               
														GoalType.MINIMIZE, 
														new NonNegativeConstraint(true)
														);

			double[] solution = optSolution.getPoint();
			for (int i=0; i<solution.length; ++i)
			{
				int s = (int) Math.ceil(solution[i]); //prendre la valeur superieur
				nbPrintPattern[i] = s;
			}
		
			fitness = optSolution.getValue();
		} catch (NoFeasibleSolutionException ex) {
			fitness = Double.MAX_VALUE;
		}
		
		return fitness;
	}
	
	public int[] getNbPrintPattern() {
		return nbPrintPattern;
	}
	
	public List<TypeImage> getTypesImages() {
		return typesImages;
	}
	
	/**
	 * Calcul du prix réel d'une solution
	 * @return double : prix calculé
	 */
	public double calculPrice() {
		double total = 0.0;
		
		total += Pattern.getPrice() * (double)(patterns.length); //prix des patterns 
	
		for (int i : nbPrintPattern)
			total += (double)i; //some des impressions des patterns (impression coute 1)
		
		return total;
	}
	
	public Solution reconstruct() {
		int nbPattern = 0;
		
		for (int print : nbPrintPattern)
			if (print > 0)
				nbPattern++;
		
		Pattern[] newPattern = new Pattern[nbPattern];
		int[] newPrint = new int[nbPattern];
		
		int incrP = 0;
		for (int i =0; i<patterns.length; ++i)
		{
			if (nbPrintPattern[i] > 0)
			{
				newPattern[incrP] = patterns[i];
				newPrint[incrP] = nbPrintPattern[i];
				incrP++;
			}
		}
		
		patterns = newPattern;
		nbPrintPattern = newPrint;
		
		calculFitness();
		
		return this;
	}
	
	@Override
	public String toString() {
		String s = "Solution = [pW="+Pattern.getWidth()+", pH="+Pattern.getHeight()+", t="+elapsedTime+", f="+fitness+", price="+calculPrice()+"] {\r\n";
		
		for (int i=0; i<patterns.length; ++i)
		{
			s += i+": "+patterns[i]+"";
			s += ", [printed= "+nbPrintPattern[i]+"]"; 
			s += "\r\n"; 
			//example : "p0 : 10 [t=2.2, lf=4.3, gf=4.3]"
		} 
		
		s += "\r\n}";
		
		return s;
	}
}
