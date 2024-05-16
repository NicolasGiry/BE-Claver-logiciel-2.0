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
	private static final int LONGUEUR = 450;
	private static final int HAUTEUR = 200;
	private static final int SPINNERWIDTH = 50;
	private static final int SPINNERHEIGHT = 40;

    JLabel modeLabel, clavLabel, partLabel;
    JRadioButton bTrain, bExp, bClavierPred, bClavierNoPred;
    JButton bvalider;
    ButtonGroup groupExpTrain, groupClavier;
    JSpinner partSpinner;

    public Main() {
        super("Initialisation Clavier Logiciel");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
        setPreferredSize(new Dimension(LONGUEUR, HAUTEUR));

        modeLabel = new JLabel("Choisir un mode : ");
        clavLabel = new JLabel("Choisir un clavier : ");
        partLabel = new JLabel("Choisir un nombre de participant : ");
        bTrain = new JRadioButton("entrainement");
        bExp = new JRadioButton("experience");
        bClavierPred = new JRadioButton("avec prédiction");
        bClavierNoPred = new JRadioButton("sans prédiction");
        groupExpTrain = new ButtonGroup();
        groupClavier = new ButtonGroup();
        bvalider = new JButton("Valider");
        partSpinner = new JSpinner();

        JPanel radioModePanel = new JPanel(new BorderLayout());
        JPanel radioClavPanel = new JPanel(new BorderLayout());
        JPanel modelPanel = new JPanel(new BorderLayout());
        JPanel clavierPanel = new JPanel(new BorderLayout());
        JPanel partPanel = new JPanel(new BorderLayout());
        JPanel finalPanel = new JPanel(new BorderLayout());
        
        partPanel.setPreferredSize(new Dimension(SPINNERWIDTH, SPINNERHEIGHT));
        radioModePanel.add(bTrain, BorderLayout.WEST);
        radioModePanel.add(bExp, BorderLayout.CENTER);
        radioClavPanel.add(bClavierPred, BorderLayout.WEST);
        radioClavPanel.add(bClavierNoPred, BorderLayout.CENTER);

        modelPanel.add(modeLabel, BorderLayout.WEST);
        modelPanel.add(radioModePanel, BorderLayout.CENTER);
        clavierPanel.add(clavLabel, BorderLayout.WEST);
        clavierPanel.add(radioClavPanel, BorderLayout.CENTER);

        partPanel.add(partLabel, BorderLayout.WEST);
        partPanel.add(partSpinner, BorderLayout.CENTER);

        finalPanel.add(modelPanel, BorderLayout.NORTH);
        finalPanel.add(clavierPanel, BorderLayout.CENTER);
        finalPanel.add(partPanel, BorderLayout.SOUTH);
        
        groupExpTrain.add(bTrain);
        groupExpTrain.add(bExp);
        groupClavier.add(bClavierPred);
        groupClavier.add(bClavierNoPred);

        this.setLayout(new BorderLayout());
        this.add(finalPanel, BorderLayout.CENTER);
        this.add(bvalider, BorderLayout.SOUTH);
        pack();
		setLocationRelativeTo(null);
		setVisible(true);

        bvalider.addActionListener(new ActionListener() {      
            @Override
            public void actionPerformed(ActionEvent e) 
            { 
                Mode mode = Mode.TRAIN;
                ResultsWordPrediction wp = ResultsWordPrediction.PRED;
                int nbPart = (int) partSpinner.getValue();
                if (bTrain.isSelected()) { 
                    mode = Mode.TRAIN; 
                } else if (bExp.isSelected()) { 
                    mode = Mode.EXP; 
                } else {
                    return;
                }
                if (bClavierPred.isSelected()) { 
                    wp = ResultsWordPrediction.PRED; 
                } else if (bClavierNoPred.isSelected()) { 
                    wp = ResultsWordPrediction.NO_PRED; 
                } else {
                    return;
                }

                start(mode, wp, nbPart);
            } 
        }); 
    }

    private void start(Mode mode, ResultsWordPrediction wp, int nbPart) {
        ExpeLogger.debutSimulation(wp, nbPart, mode);
        new Clavier2Frame(mode, wp, nbPart);
        this.dispose();
    }

    public static void main(String[] args) {
        new Main();
    }
}