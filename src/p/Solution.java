package p;

import image.TypeImage;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.math3.optim.MaxIter;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.linear.LinearConstraint;
import org.apache.commons.math3.optim.linear.LinearConstraintSet;
import org.apache.commons.math3.optim.linear.LinearObjectiveFunction;
import org.apache.commons.math3.optim.linear.NonNegativeConstraint;
import org.apache.commons.math3.optim.linear.Relationship;
import org.apache.commons.math3.optim.linear.SimplexSolver;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;

public class Solution {
	private List<Pattern> patterns;
	private List<TypeImage> typesImages; //types d'images utilisés dans l'ensemble des patterns
	private Map<Pattern, Integer> nbPrintPattern; //nombre d'impression pour chaque pattern
	private long elapsedTime; //temps pour obtenir la solution
	private double localFitness; 

	public Solution(List<TypeImage> typesImages, List<Pattern> patterns, long elapsedTime) {
		super();
		this.patterns = patterns;
		this.typesImages = typesImages;
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
	
	public double calculFitness()
	{
		nbPrintPattern.clear();
		
		// describe the optimization problem : x1 + x2 + x3 + xn 
		//où xi est le nombre d'impression du pattern i
		int nbPattern = this.getPatterns().size();
		double[] coefficients = new double[nbPattern];
		for (int i=0; i<nbPattern ; ++i)
			coefficients[i] = 1; //init
		
		LinearObjectiveFunction f = new LinearObjectiveFunction(coefficients, 0.0);
		
		//constraints selon les quantités à distribuer
		Collection<LinearConstraint> constraints = new ArrayList<>();
		for (TypeImage ti : typesImages)
		{
			double[] coefs = new double[nbPattern];
			
			for (int i=0; i < nbPattern; ++i) 
				coefs[i] = patterns.get(i).getImgsNb().get(ti);
			
			constraints.add(new LinearConstraint(coefs, Relationship.GEQ, ti.getDemand()));
		}
		
		//coefs patterns doivent être supérieurs à 0 (on veut que tous les patterns sont imprimés au moins 1 fois)
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
			nbPrintPattern.put(patterns.get(i), s);
		}
		
		localFitness = optSolution.getValue();
		
		return localFitness;
	}
	
	public Map<Pattern, Integer> getNbPrintPattern() {
		return nbPrintPattern;
	}
	
	@Override
	public String toString() {
		String s = "Solution = [pW="+Pattern.getWidth()+", pH="+Pattern.getHeight()+", t="+elapsedTime+", f="+localFitness+"] {\r\n";
		
		for (int i=0; i<patterns.size(); ++i)
		{
			s += patterns.get(i)+"";
			s += ", [printed= "+nbPrintPattern.get(patterns.get(i))+"]"; 
			s += "\r\n"; 
			//example : "p0 : 10 [t=2.2, lf=4.3, gf=4.3]"
		} 
		
		s += "\r\n}";
		
		return s;
	}
}
