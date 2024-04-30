import java.awt.BorderLayout;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JTextArea;

public class Clavier2Frame extends JFrame{
	private Clavier2 clavier;
    private JTextArea jTextArea1;
	
	public Clavier2Frame() {
		super("Clavier Logiciel");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
        ImageIcon icon = new ImageIcon("icone.png");
        setIconImage(icon.getImage());

        jTextArea1 = new javax.swing.JTextArea();
		jTextArea1.setColumns(30);
        jTextArea1.setRows(5);
        jTextArea1.setLineWrap(true);
        jTextArea1.setWrapStyleWord(true);
        javax.swing.JScrollPane jScrollPane1 = new javax.swing.JScrollPane(jTextArea1);

		clavier = new Clavier2(jTextArea1);

        setLayout(new BorderLayout());

        add(jScrollPane1, BorderLayout.NORTH);
		add(clavier,BorderLayout.CENTER);
		pack();
		setLocationRelativeTo(null);
		setVisible(true);
	}
	
	public static void main(String[] args) {
		new Clavier2Frame();
	}
}
