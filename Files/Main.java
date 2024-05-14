import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSpinner;

public class Main extends JFrame {
	private static final int LONGUEUR = 440;
	private static final int HAUTEUR = 200;
	private static final int SPINNERWIDTH = 50;
	private static final int SPINNERHEIGHT = 40;

    JLabel modeLabel, partLabel;
    JRadioButton bTrain, bExp1, bExp2;
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
        bExp1 = new JRadioButton("experience G1");
        bExp2 = new JRadioButton("experience G2");
        groupExpTrain = new ButtonGroup();
        bvalider = new JButton("Valider");
        partSpinner = new JSpinner();

        JPanel radioPanel = new JPanel(new BorderLayout());
        JPanel modelPanel = new JPanel(new BorderLayout());
        JPanel partPanel = new JPanel(new BorderLayout());
        JPanel finalPanel = new JPanel(new BorderLayout());
        
        partPanel.setPreferredSize(new Dimension(SPINNERWIDTH, SPINNERHEIGHT));
        radioPanel.add(bTrain, BorderLayout.WEST);
        radioPanel.add(bExp1, BorderLayout.CENTER);
        radioPanel.add(bExp2, BorderLayout.EAST);
        modelPanel.add(modeLabel, BorderLayout.WEST);
        modelPanel.add(radioPanel, BorderLayout.CENTER);
        partPanel.add(partLabel, BorderLayout.WEST);
        partPanel.add(partSpinner, BorderLayout.CENTER);
        finalPanel.add(modelPanel, BorderLayout.NORTH);
        finalPanel.add(partPanel, BorderLayout.CENTER);
        finalPanel.add(bvalider, BorderLayout.SOUTH);
        groupExpTrain.add(bTrain);
        groupExpTrain.add(bExp1);
        groupExpTrain.add(bExp2);
        this.add(finalPanel);
        pack();
		setLocationRelativeTo(null);
		setVisible(true);

        bvalider.addActionListener(new ActionListener() {      
            @Override
            public void actionPerformed(ActionEvent e) 
            { 
                Mode mode = Mode.TRAIN;
                int nbPart = (int) partSpinner.getValue();
                if (bTrain.isSelected()) { 
                    mode = Mode.TRAIN; 
                } else if (bExp1.isSelected()) { 
                    mode = Mode.EXP1; 
                } else if (bExp2.isSelected()) { 
                    mode = Mode.EXP2; 
                }
                start(mode, nbPart);
            } 
        }); 
    }

    private void start(Mode mode, int nbPart) {
        ResultsWordPrediction wp = ResultsWordPrediction.TROIS;
        ExpeLogger.debutSimulation(wp, nbPart, mode);
        new Clavier2Frame(mode);
        this.dispose();
    }

    public static void main(String[] args) {
        new Main();
    }
}