package main;

import image.TypeImage;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

public class LoadFile
{

	public static List<TypeImage> lectureFichier(String path)
	{
		List<TypeImage> list = new ArrayList<>();
				
		int pX = 0;
		int pY = 0;
		
		File f;
		try
		{
			f = new File(path);
			BufferedReader br = new BufferedReader(new FileReader(f));
			
			String l;
			//HEADER
			while((l = br.readLine()) != null)
			{
				String[] header = l.split("=");
				if (header[0].contentEquals("m"))
				{
					break;
				} else if (header[0].contentEquals("LX")) {
					pX = Integer.valueOf(header[1]);
				} else if (header[0].contentEquals("LY")) {
					pY = Integer.valueOf(header[1]);
				}
			}

			//TYPE IMAGES
			int i = 0;
			while((l = br.readLine()) != null)
			{
				String[] data = l.split("\t");
				if (data.length == 3)
				{
					double width = Double.valueOf(data[0]);
					double height = Double.valueOf(data[1]);
					int demand = Integer.valueOf(data[2]);
					TypeImage ti = new TypeImage(++i, width, height, demand);
					list.add(ti);
					//System.out.println(ti);
				}
			}
			
			if (pX == 0 || pY == 0)
			{
				throw new Exception("Pattern size must be > 0");
			} else {
				//System.out.println("Pattern Width = "+pX+" / Pattern height = "+pY);
				Pattern.setSize(pX, pY);
			}
			
		} catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return list;
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
		LoadFile.lectureFichier("data/data_20Lalpha.txt");

	}

}
