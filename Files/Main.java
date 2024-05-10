import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Scanner;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSpinner;

public class Main extends JFrame {
	private static final int LONGUEUR = 400;
	private static final int HAUTEUR = 200;
	private static final int SPINNERWIDTH = 50;
	private static final int SPINNERHEIGHT = 40;

    JLabel modeLabel, partLabel;
    JRadioButton bTrain, bExp;
    JButton bvalider;
    ButtonGroup groupExpTrain;
    JSpinner partSpinner;

    public Main() {
        super("Initialisation Clavier Logiciel");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
        setPreferredSize(new Dimension(LONGUEUR, HAUTEUR));

        modeLabel = new JLabel("Choisir un mode : ");
        partLabel = new JLabel("Choisir un nombre de participant : ");
        bTrain = new JRadioButton("entrainement");
        bExp = new JRadioButton("experience");
        groupExpTrain = new ButtonGroup();
        bvalider = new JButton("Valider");
        partSpinner = new JSpinner();

        JPanel radioPanel = new JPanel(new BorderLayout());
        JPanel modelPanel = new JPanel(new BorderLayout());
        JPanel partPanel = new JPanel(new BorderLayout());
        JPanel finalPanel = new JPanel(new BorderLayout());
        
        partPanel.setPreferredSize(new Dimension(SPINNERWIDTH, SPINNERHEIGHT));

        radioPanel.add(bTrain, BorderLayout.WEST);
        radioPanel.add(bExp, BorderLayout.CENTER);
        modelPanel.add(modeLabel, BorderLayout.WEST);
        modelPanel.add(radioPanel, BorderLayout.CENTER);
        partPanel.add(partLabel, BorderLayout.WEST);
        partPanel.add(partSpinner, BorderLayout.CENTER);

        finalPanel.add(modelPanel, BorderLayout.NORTH);
        finalPanel.add(partPanel, BorderLayout.CENTER);
        finalPanel.add(bvalider, BorderLayout.SOUTH);


        groupExpTrain.add(bTrain);
        groupExpTrain.add(bExp);

        this.add(finalPanel);

        pack();
		setLocationRelativeTo(null);
		setVisible(true);


        bvalider.addActionListener(new ActionListener() { 
            
            @Override
            public void actionPerformed(ActionEvent e) 
            { 
                Mode mode;
                int nbPart = (int) partSpinner.getValue();
                if (bTrain.isSelected()) { 
                    mode = Mode.TRAIN; 
                    start(mode, nbPart);
                } 
                else if (bExp.isSelected()) { 
                    mode = Mode.EXP; 
                    start(mode, nbPart);
                }
            } 
        }); 
    }

    private void start(Mode mode, int nbPart) {
        ResultsWordPrediction wp = ResultsWordPrediction.TROIS;
        ExpeLogger.debutSimulation(wp, nbPart, mode);
        new Clavier2Frame();
        this.dispose();
    }

    public static void main(String[] args) {
        new Main();
        // ResultsWordPrediction wp = ResultsWordPrediction.TROIS;
        // Mode mode = Mode.TRAIN;
        // Scanner scanner = new Scanner(System.in);

        // int m;
        // do {
        //     System.out.println("Choisir un mode : (1) train (2) experience.");
        //     m = scanner.nextInt();
        //     switch (m) {
        //         case 1:
        //             mode = Mode.TRAIN;
        //             break;
        //         case 2:
        //             mode = Mode.EXP;
        //             break;
        //         default:
        //             System.out.println("Erreur de saisie.");
        //     }
        // } while (m!=1 && m!=2);

        // System.out.println("Choisir un nombre de participant :");
        // m = scanner.nextInt();
        
        // ExpeLogger.debutSimulation(wp, m, mode);
        // new Clavier2Frame();
        // scanner.close();
    }
}
