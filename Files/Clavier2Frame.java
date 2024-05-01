import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;

public class Clavier2Frame extends JFrame{
	private Clavier2 clavier;
    private JTextArea textModelArea, textInputArea;
	private JLabel textModelLabel;
	//private JButton validerButton;
	//private List<String> phrases;
	//private long timer = 300000, depart; // 10min en millisecondes
	
	public Clavier2Frame() {
		super("Clavier Logiciel");
		//depart = System.currentTimeMillis();
		setDefaultCloseOperation(EXIT_ON_CLOSE);
        ImageIcon icon = new ImageIcon("icone.png");
        setIconImage(icon.getImage());

		int margin = 5;
		EmptyBorder border = new EmptyBorder(margin, margin, margin, margin);

		textModelLabel = new JLabel("Recopier cette phrase et appuyer sur Valider.");
		textModelLabel.setBorder(border);

		textModelArea = new JTextArea();
        textModelArea.setRows(5);
        textModelArea.setLineWrap(true);
        textModelArea.setWrapStyleWord(true);
		textModelArea.setEditable(false);
        javax.swing.JScrollPane jScrollPane1 = new javax.swing.JScrollPane(textModelArea);
		jScrollPane1.setBorder(border);

        textInputArea = new JTextArea();
        textInputArea.setRows(5);
        textInputArea.setLineWrap(true);
        textInputArea.setWrapStyleWord(true);
		textInputArea.setEditable(false);
        javax.swing.JScrollPane jScrollPane2 = new javax.swing.JScrollPane(textInputArea);
		jScrollPane2.setBorder(border);

		//validerButton = new JButton("Valider");

		JPanel modelPanel = new JPanel(new BorderLayout());
		modelPanel.add(jScrollPane1, BorderLayout.CENTER);

        JPanel inputPanel = new JPanel(new BorderLayout());
        inputPanel.add(jScrollPane2, BorderLayout.CENTER);

		JPanel containerPanel = new JPanel(new BorderLayout());
		containerPanel.add(textModelLabel, BorderLayout.NORTH);
        containerPanel.add(modelPanel, BorderLayout.CENTER);
        containerPanel.add(inputPanel, BorderLayout.SOUTH);

		//JPanel validerPanel = new JPanel(new BorderLayout());
		//validerPanel.add(validerButton, BorderLayout.CENTER);

		clavier = new Clavier2(textInputArea, textModelArea);

        setLayout(new BorderLayout());

        add(containerPanel, BorderLayout.NORTH);
		add(clavier,BorderLayout.CENTER);
		//add(validerPanel, BorderLayout.SOUTH);
		pack();
		setLocationRelativeTo(null);
		setVisible(true);

		//phrases = generatePhrases();
		//textModelArea.setText(texteSuivant());

		// validerButton.addActionListener(new ActionListener() {
        //     @Override
        //     public void actionPerformed(ActionEvent e) {
		// 		long timeElapsed = System.currentTimeMillis()-depart;
		// 		if (timeElapsed>=timer) {
		// 			setVisible(false); //you can't see me!
		// 			dispose(); //Destroy the JFrame object
		// 		}
		// 		long tempsRestant = (timer-timeElapsed);
		// 		System.out.println("Temps restants : "+ tempsRestant / (60000) + " min " + (tempsRestant % (60000)) / 1000 + " sec " + tempsRestant % 1000 + " ms");
        //         String texteRecopie = textInputArea.getText();
        //         if (textModelArea.getText().equals(texteRecopie)) {
        //             System.out.println("Correct !");
        //         } else {
        //             System.out.println("Incorrect !");
        //         }
		// 		textModelArea.setText(texteSuivant());
		// 		textInputArea.setText("");
        //     }
        // });
	}

	// private List<String> generatePhrases() {
	// 	List<String> phrases = new ArrayList<>();
	// 	String chemin = "Files/phrases_test.txt";
	// 	try {
    //         BufferedReader phrasesBufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(chemin), "UTF-8"));
    //         try {
    //             String ligne;
    //             while((ligne = phrasesBufferedReader.readLine()) != null)
    //             {
    //                 phrases.add(ligne);
    //             }
    //         } finally {
    //             phrasesBufferedReader.close();
    //         }
    //     } catch(IOException e)
    //     {
    //       e.printStackTrace();
    //     }
	// 	return phrases;
	// }

	// private String texteSuivant() {
	// 	if (phrases.size()<=0) {
	// 		return null;
	// 	}
	// 	Random rand = new Random();
	// 	int indice = rand.nextInt(phrases.size());
	// 	return phrases.remove(indice);
	// }
	
	public static void main(String[] args) {
		new Clavier2Frame();
	}
}
