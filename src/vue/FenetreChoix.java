package vue;

import javax.swing.JFrame;
import java.awt.BorderLayout;
import java.io.File;

import javax.swing.JFileChooser;
//-------------
//constructeur
//-------------
public class FenetreChoix extends JFrame{
	JFileChooser fileChooser;
	protected String nomFichier;
	protected String[] chemin=new String[2];
	public FenetreChoix() {
		getContentPane().setLayout(new BorderLayout(0, 0));
		this.setTitle("Choix fichier images");
		this.setSize(800,400);
		//this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLocationRelativeTo(null);
		fileChooser= new JFileChooser();
		getContentPane().add(fileChooser, BorderLayout.CENTER);
	}
	//cette fenetre a pour but de pouvoir recuperer une erreur sans que cela influe sur le modele qui doit etre stable
	public String[] choisir()
	{
		//le file user permet d'acceder directement a un dossier contenant les fichier image a telecharger
		fileChooser.setCurrentDirectory(new File("Documents"));
		int retour=fileChooser.showOpenDialog(null);
		try
		{
		if(retour==JFileChooser.APPROVE_OPTION){
		   // nom du fichier  choisi 
			nomFichier=fileChooser.getSelectedFile().getName();
		   // chemin absolu du fichier choisi
			chemin[0]=String.valueOf(fileChooser.getSelectedFile().getAbsolutePath());
			this.setVisible(false);
			chemin[1]=nomFichier;
		}
		else
		{
			chemin[0]="rien";
		}
		return chemin;
		}
		catch(Error e){
			System.out.println("Veuillez recommencer il faut choisir un fichier");
			chemin[0]="rien";
			return chemin;
		}

}
}
