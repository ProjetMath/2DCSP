package tabou;

import java.util.ArrayList;
import java.util.List;

import main.Solution;


public class ListElite {
	private List<Solution> list;
	private final int size;
	
	public ListElite(int size) {
		this.size = size;
		list = new ArrayList<>();		
	}
		
	public void add(Solution s)
	{
		int indexInsert = -1;
		for (int i=0; i < list.size(); ++i)
		{
			if (s.equals(list.get(i)))
				return; //element deja dans la liste
			
			if (s.getFitness() < list.get(i).getFitness())
			{
				indexInsert = i;
				break;
			}
		}
		
		if (indexInsert == -1 && list.size() < size)
			list.add(s); //ajouter la solution à la fin si moins bonne
		else if (indexInsert >= 0)
			list.add(indexInsert, s); //si meilleure insérer la solution à cette emplacement
		
		//List trop grande suppr denier element
		if (list.size() > size)
			list.remove(size);
	}
	
	public List<Solution> getList()
	{
		return list;
	}
}
