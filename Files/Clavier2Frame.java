import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.border.EmptyBorder;

public class Clavier2Frame extends JFrame{
	private Clavier2 clavier;
    private JTextArea textModelArea;
	private JTextPane textInputPane;
	private JLabel textModelLabel;
	
	public Clavier2Frame(Mode mode) {
		super("Clavier Logiciel");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
        ImageIcon icon = new ImageIcon("Files/icone.png");
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

        textInputPane = new JTextPane();
		textInputPane.setPreferredSize(new Dimension(0, 100));
		textInputPane.setEditable(false);
        javax.swing.JScrollPane jScrollPane2 = new javax.swing.JScrollPane(textInputPane);
		jScrollPane2.setBorder(border);

		JPanel modelPanel = new JPanel(new BorderLayout());
		modelPanel.add(jScrollPane1, BorderLayout.CENTER);

        JPanel inputPanel = new JPanel(new BorderLayout());
        inputPanel.add(jScrollPane2, BorderLayout.CENTER);

		JPanel containerPanel = new JPanel(new BorderLayout());
		containerPanel.add(textModelLabel, BorderLayout.NORTH);
        containerPanel.add(modelPanel, BorderLayout.CENTER);
        containerPanel.add(inputPanel, BorderLayout.SOUTH);

		clavier = new Clavier2(textInputPane, textModelArea, mode);

        setLayout(new BorderLayout());

        add(containerPanel, BorderLayout.NORTH);
		add(clavier,BorderLayout.CENTER);
		pack();
		setLocationRelativeTo(null);
		setVisible(true);
	}
	
	public static void main(String[] args) {
		new Clavier2Frame(Mode.TRAIN);
	}
}
