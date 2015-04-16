package tabou;

import transformation.Transformation;

public class ListTabou {
	private int iTbl; //index pour g�rer l'ajout cyclique dans la liste tabou
	private final int maxSize;
	private Transformation[] tbl;
	
	public ListTabou(int maxSize) {
		this.maxSize = maxSize;
		this.tbl = new Transformation[maxSize];
	}
	
	/**
	 * Ins�rer une transformation dans la liste tabou
	 * Si la liste est pleine la plus vielle transformation est remplac�e
	 * @param t Transformation � ajouter
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
	 * Tester si la liste contient la transformation en param�tre
	 * @param t Transformation � tester
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
