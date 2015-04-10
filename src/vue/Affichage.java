package vue;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JViewport;

public class Affichage extends JFrame {
	public Affichage() {
		getContentPane().setForeground(Color.BLACK);
	        JPanel panel = new JPanel();
	        panel.setBackground(Color.BLACK);
	        panel.setOpaque( false );
	        panel.setPreferredSize( new Dimension(400, 400) );

	        JViewport viewport = new JViewport()
	        {
	            public void paintComponent(Graphics g)
	            {
	                super.paintComponent(g);
	                g.setColor( Color.BLUE );
	                g.drawArc( 100, 100, 80, 80, 0, 360);
	            }
	        };
	        viewport.setBackground(Color.BLACK);

	        viewport.setView( panel );
	        JScrollPane scrollPane = new JScrollPane();
	        scrollPane.setViewport( viewport );
	        scrollPane.setPreferredSize( new Dimension(300, 300) );
	        getContentPane().add( scrollPane );
	    }

	    public static void main(String[] args)
	    {
	        JFrame frame = new Affichage();
	        frame.setDefaultCloseOperation( DISPOSE_ON_CLOSE );
	        frame.pack();
	        frame.setLocationRelativeTo( null );
	        frame.setVisible(true);
	    }
}
