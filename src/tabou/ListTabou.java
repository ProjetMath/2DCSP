package tabou;

import transformation.Transformation;

public class ListTabou {
	private int iTbl; //index pour gérer l'ajout cyclique dans la liste tabou
	private final int maxSize;
	private Transformation[] tbl;
	
	public ListTabou(int maxSize) {
		this.maxSize = maxSize;
		this.tbl = new Transformation[maxSize];
	}
	
	/**
	 * Insérer une transformation dans la liste tabou
	 * Si la liste est pleine la plus vielle transformation est remplacée
	 * @param t Transformation à ajouter
	 * @return indice
	 */
	public Integer add(Transformation t) {
		if (tbl != null)
		{
			iTbl++;
			if (iTbl > maxSize)
				iTbl = 0; //cyclique
			
			tbl[iTbl] = t;
			
			return iTbl;
		}
		return null;
	}
	
	/**
	 * Tester si la liste contient la transformation en paramètre
	 * @param t Transformation à tester
	 * @return vrai si contient faux sinon
	 */
	public boolean contains(Transformation t)
	{
		if (t == null || tbl == null)
			return false;
		
		for (int i=0; i<maxSize; ++i) 
		{
			if (tbl[i].equals(t))
				return true;
		}
		
		return false;
	}
	
}
