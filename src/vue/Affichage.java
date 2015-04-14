package vue;

import image.Item;
import image.TypeImage;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JViewport;

import p.Pattern;
import p.Solution;

public class Affichage extends JFrame
{
	private JPanel panel;
	private JScrollPane scrollPane;
	// la methode paintComponent n'appel que des methode avec des attribue
	// finaux
	public Affichage(final Solution solution)
	{
		getContentPane().setForeground(Color.BLACK);
		panel = new JPanel()
		{
			public void paintComponent(Graphics dessin)
			{
				super.paintComponent(dessin);
				// image.drawArc( 100, 100, 80, 80, 0, 360);
				// appel de la fonction qui va reelement dessiner la solution
				dessineRectangle(solution, dessin);
			}
		};
		panel.setBackground(Color.BLACK);
		panel.setOpaque(false);
		panel.setPreferredSize(new Dimension(1900,1000));
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
		for (int i = 0; i < solution.getPatterns().size(); i++)
		{
			int hauteur=panel.getSize().height;
			composant.setColor(Color.RED);
			Pattern pattern = solution.getPatterns().get(i);
			// on commence par dessiner les pattern avec un cadre,
			// on prend donc leur taille et on les place en parsant en
			// int(contraintes)
			composant.drawRect((int) (i%9 * Pattern.getWidth() + 50),
					hauteur - (int) (((i/9)+1) *Pattern.getHeight()+20),
					(int) Pattern.getWidth(), (int) Pattern.getHeight());
			composant.setColor(Color.cyan);
			//une fois qu'un pattern est dessiner on le remplit avec des rectangle plein
			for(int j=0;j<pattern.getImages().size();j++)
			{
				Item image=pattern.getImages().get(j);
				composant.drawRect((int) (i%9 * Pattern.getWidth() + 50)+image.getPosition().x,
						hauteur - (int) (((i/9)+1) *Pattern.getHeight()+20)
						+(int)(Pattern.getHeight()-image.getPosition().y-image.getType().getHeight()),
						(int)image.getType().getWidth() , (int)image.getType().getHeight());
			}
		}
	}

	public static void main(String[] args)
	{
		//TODO faire le traitement correct pour une image avec un boolean true(position(x,y)->(y,x))
		//donner une couleur a un type d'image simple mais utile
		Pattern.setSize(200, 300);
		TypeImage typeA=new TypeImage(1,50,75,20);
		List<Pattern> patterns = new ArrayList<Pattern>();
		List<Item> images = new ArrayList<Item>();
		Item image1=new Item(typeA,new Point(0,0),false);
		images.add(image1);
		Map<TypeImage, Integer> imgsNb = null;
		for (int i = 0; i < 11; i++)
		{
			patterns.add(new Pattern(images, imgsNb));
			patterns.get(i).setImages(images);
		}
		final Solution solution = new Solution(patterns,
				new HashMap<Pattern, Integer>(), 0);
		JFrame frame = new Affichage(solution);
		frame.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}
}