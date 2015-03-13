package p;

import java.awt.Point;

/*
 * Rectangle espace vide
 */
public class Bin {
	private int mId; //num bin
	private double mLargeur; 
	private double mLongueur;
	//private int idPattern;
	private Point mPosition; //left bottom point
	private boolean rotated; //pivoté ou non 
	
	private Bin mBinCutting1; //Bin à desactiver après la coupure
	private Bin mBinCutting2; //Bin à desactiver après la coupure
	
	public Bin() 
	{

	}
	
	
}
