package vue;

import image.Item;
import image.TypeImage;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JViewport;

import main.GenerateRandomSolution;
import main.Pattern;
import main.Solution;

public class Affichage extends JFrame
{
	private JPanel panel;
	private JScrollPane scrollPane;
	private int positionnementPattern;
	
	// la methode paintComponent n'appel que des methode avec des attribue
	// finaux
	public Affichage(final Solution solution,Dimension patternSize)
	{
		//permet de repartit les pattern sur la dimension desir�
		positionnementPattern=1850/patternSize.width;
		//permet d'obetnir la taille en hauteur pour empiler 1� 1 tout les pattern(pour des gros pattern)
		int hauteurPanel= patternSize.height+((patternSize.height*solution.getPatterns().length)/positionnementPattern);
		getContentPane().setForeground(Color.BLACK);
		panel = new JPanel()
		{
			public void paintComponent(Graphics dessin)
			{
				super.paintComponent(dessin);
				dessineRectangle(solution, dessin);
			}
		};
		panel.setBackground(Color.BLACK);
		panel.setOpaque(false);
		panel.setPreferredSize(new Dimension(1900,hauteurPanel));
		scrollPane = new JScrollPane();
		scrollPane.setPreferredSize(new Dimension(1000, 600));
		getContentPane().add(scrollPane);
		// Class me permettant de dessiner
		JViewport viewport = new JViewport();
		viewport.setBackground(Color.BLACK);

		viewport.setView(panel);
		scrollPane.setViewport(viewport);
		scrollPane.getVerticalScrollBar().setUnitIncrement(16);
	}

	public void dessineRectangle(Solution solution, Graphics dessin)
	{
		Graphics composant = dessin;
		for (int i = 0; i < solution.getPatterns().length; i++)
		{
			int hauteur=panel.getSize().height;
			composant.setColor(Color.RED);
			Pattern pattern = solution.getPatterns()[i];
			// on commence par dessiner les pattern avec un cadre,
			// on prend donc leur taille et on les place en parsant en
			// int(contraintes)
			composant.drawRect((int) (i%positionnementPattern * Pattern.getWidth() + 50),
					hauteur - (int) (((i/positionnementPattern)+1) *Pattern.getHeight()+20),
					(int) Pattern.getWidth(), (int) Pattern.getHeight());
			composant.setColor(Color.cyan);
			//une fois qu'un pattern est dessiner on le remplit avec des rectangle plein
			for(int j=0;j<pattern.getImages().size();j++)
			{
				Item image=pattern.getImages().get(j);
				if(image.isRotated())
				{
					composant.drawRect((int) (i%positionnementPattern * Pattern.getWidth() + 50)+image.getPosition().x,
							hauteur - (int) (((i/positionnementPattern)+1) *Pattern.getHeight()+20)
							+(int)(Pattern.getHeight()-image.getPosition().y-image.getType().getWidth()),
							(int)image.getType().getHeight(), (int)image.getType().getWidth());
				}
				else
				{
				composant.drawRect((int) (i%positionnementPattern * Pattern.getWidth() + 50)+image.getPosition().x,
						hauteur - (int) (((i/positionnementPattern)+1) *Pattern.getHeight()+20)
						+(int)(Pattern.getHeight()-image.getPosition().y-image.getType().getHeight()),
						(int)image.getType().getWidth() , (int)image.getType().getHeight());
				}
			}
		}
	}
	public static void affiche(JFrame frame)
	{
		frame.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}
	public static void main(String[] args)
	{
		//TODO faire le traitement correct pour une image avec un boolean true(position(x,y)->(y,x))
		//donner une couleur a un type d'image simple mais utile
		
		Pattern.setSize(400, 600); 
		final int nbMaxPat = 6;
		
		//list types images
		List<TypeImage> tImages = new ArrayList<>();
		tImages.add(new TypeImage(1, 130, 56, 562));
		tImages.add(new TypeImage(0, 240, 30, 246));
		tImages.add(new TypeImage(2, 140, 22, 1000));
		tImages.add(new TypeImage(3, 90, 23, 3498));	
		
		//trier les images
		List<TypeImage> tempListTi = new ArrayList<TypeImage>(tImages);
		List<TypeImage> tit = new ArrayList<TypeImage>();
		while (tit.size() <= tImages.size())
		{
			if (tempListTi.size() == 0) break;
			
			TypeImage bigest = tempListTi.get(0);
			for (TypeImage ti : tempListTi)
				if (bigest.getSurface() < ti.getSurface())
					bigest = ti;
			
			tempListTi.remove(bigest);
			tit.add(bigest);
		}
		
		Solution s = new GenerateRandomSolution(tit).generate(nbMaxPat);
		
		JFrame frame = new Affichage(s,new Dimension((int)Pattern.getWidth(),(int)Pattern.getHeight()));
		affiche(frame);
		System.out.println(s);
	}
}
