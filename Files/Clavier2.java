import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.Random;

import javax.swing.JComponent;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import Prediction.Predictor;
import Prediction.Tree;

public class Clavier2 extends JComponent implements Observer, MouseListener, MouseMotionListener {
    private static final int NB_KEYS = 41;
    private static final long serialVersionUID = 1L;
	private static final int LONGUEUR = 400;
	private static final int HAUTEUR = 500;

    private Predictor predicteur = new Predictor();
    private Tree arbre;
	private List<String> phrases;
    private JTextArea phraseArea;
    private JTextPane textPane;
    private long timer = 600, depart;
    //                   10 min
    private int currentChar=0;
    private Mode mode;
    private ResultsWordPrediction wp;
    private int nbPart;

    private ToucheHexa[] keys;
    private Touche validerTouche;
    private int keyWidth = 85;
    private int keyHeight = (keyWidth+10)/2;
    private int nbKeysPredicted = 3;
    private int nbChar = 0, nbPredictedKeysPressed = 0, nbErrors = 0, nbClavier;
    private Clavier2Frame clavierFrame;

    SimpleAttributeSet basic, error;

    private float[] lignes = {1.5f, 2, 2.5f, 3, 3.5f, 4, 4.5f, 5, 5.5f, 6, 6.5f, 7f, 7.5f};
    private float[] colonnes = {1, 1.5f, 2, 2.5f, 3, 3.5f, 4};

    private List<String> letters = 
        new ArrayList<>(List.of("", "", "", "E", "A", "I", "S", "N", "R", "T", "O", "L", "U", "D", "C", "M",
        "P", "G", "B", "V", "H", "F", "Q", "Y", "X", "J", "'", "W", "?", ".", ",", "\u2190", "!", "_", "Ç", "É", "À", "È", "Ù", "K", "Z"));

    private List<String> lettersNoPred = 
        new ArrayList<>(List.of("R", "T", "O", "E", "A", "I", "S", "N", "V", "È", "H", "L", "U", "D", "C", "M",
        "P", "G", "B", "À", "Ù", "F", "Q", "Y", "X", "J", "'", "W", "?", ".", ",", "\u2190", "!", "_", "Ç", "É", "", "", "", "K", "Z"));

    private int[] x = { (int) (colonnes[2] * keyWidth), (int) (colonnes[3] * keyWidth), (int) (colonnes[4] * keyWidth), (int) (colonnes[4] * keyWidth), 
                        (int) (colonnes[3] * keyWidth), (int) (colonnes[2] * keyWidth), (int) (colonnes[1] * keyWidth), (int) (colonnes[1] * keyWidth), 
                        (int) (colonnes[2] * keyWidth), (int) (colonnes[3] * keyWidth), (int) (colonnes[4] * keyWidth), (int) (colonnes[5] * keyWidth), 
                        (int) (colonnes[5] * keyWidth), (int) (colonnes[5] * keyWidth), (int) (colonnes[4] * keyWidth), (int) (colonnes[3] * keyWidth), 
                        (int) (colonnes[2] * keyWidth), (int) (colonnes[1] * keyWidth), (int) (colonnes[1] * keyWidth), (int) (colonnes[2] * keyWidth), 
                        (int) (colonnes[4] * keyWidth), (int) (colonnes[5] * keyWidth), (int) (colonnes[5] * keyWidth), (int) (colonnes[4] * keyWidth), 
                        (int) (colonnes[2] * keyWidth), (int) (colonnes[1] * keyWidth), (int) (colonnes[0] * keyWidth), (int) (colonnes[3] * keyWidth), 
                        (int) (colonnes[6] * keyWidth), (int) (colonnes[0] * keyWidth), (int) (colonnes[0] * keyWidth), (int) (colonnes[6] * keyWidth), 
                        (int) (colonnes[6] * keyWidth), (int) (colonnes[3] * keyWidth), (int) (colonnes[6] * keyWidth), 
                        (int) (colonnes[0] * keyWidth), (int) (colonnes[1] * keyWidth), (int) (colonnes[3] * keyWidth), (int) (colonnes[5] * keyWidth),
                        (int) (colonnes[1] * keyWidth), (int) (colonnes[5] * keyWidth)};
                
    private int[] y = { (int) (lignes[5] * keyHeight), (int) (lignes[4] * keyHeight), (int) (lignes[5] * keyHeight), (int) (lignes[7] * keyHeight), 
                        (int) (lignes[8] * keyHeight), (int) (lignes[7] * keyHeight), (int) (lignes[6] * keyHeight), (int) (lignes[4] * keyHeight), 
                        (int) (lignes[3] * keyHeight), (int) (lignes[2] * keyHeight), (int) (lignes[3] * keyHeight), (int) (lignes[4] * keyHeight),
                        (int) (lignes[6] * keyHeight), (int) (lignes[8] * keyHeight), (int) (lignes[9] * keyHeight), (int) (lignes[10] * keyHeight), 
                        (int) (lignes[9] * keyHeight), (int) (lignes[8] * keyHeight), (int) (lignes[2] * keyHeight), (int) (lignes[1] * keyHeight), 
                        (int) (lignes[1] * keyHeight), (int) (lignes[2] * keyHeight), (int) (lignes[10] * keyHeight), (int) (lignes[11] * keyHeight), 
                        (int) (lignes[11] * keyHeight), (int) (lignes[10] * keyHeight), (int) (lignes[9] * keyHeight), (int) (lignes[12] * keyHeight), 
                        (int) (lignes[9] * keyHeight), (int) (lignes[7] * keyHeight), (int) (lignes[5] * keyHeight), (int) (lignes[5] * keyHeight), 
                        (int) (lignes[7] * keyHeight), (int) (lignes[6] * keyHeight), (int) (lignes[3] * keyHeight), 
                        (int) (lignes[3] * keyHeight), (int) (lignes[0] * keyHeight), (int) (lignes[0] * keyHeight), (int) (lignes[0] * keyHeight),
                        (int) (lignes[12] * keyHeight), (int) (lignes[12] * keyHeight)};

    public Clavier2(JTextPane jTextpane, JTextArea textModelArea, Mode mode, ResultsWordPrediction wp, Clavier2Frame clavierFrame, int nbPart, int nbClavier) {
        super();
        this.mode = mode;
        this.wp = wp;
        this.nbPart = nbPart;
        this.clavierFrame = clavierFrame;
        this.nbClavier = nbClavier;
        depart = System.currentTimeMillis();
        if (mode == Mode.TRAIN) {
            timer = 0;
            this.nbClavier = 0;
        }
        this.phraseArea = textModelArea;
        textPane = jTextpane;
        predicteur.createTree();
        arbre = predicteur.getRacine();
        keys = new ToucheHexa[NB_KEYS];
		setPreferredSize(new Dimension(LONGUEUR, HAUTEUR));

        validerTouche = new Touche("Valider", (int) (2.5*keyWidth), (int) (9.5*keyHeight));
        validerTouche.setIsValiderTouche(true);
        validerTouche.addObserver(this);

        basic = new SimpleAttributeSet();
        error = new SimpleAttributeSet();
        StyleConstants.setForeground(basic, Color.black);
        StyleConstants.setForeground(error, Color.red);

        if (wp.equals(ResultsWordPrediction.NO_PRED)) {
            letters = lettersNoPred;
        }

        for (int i=0; i<NB_KEYS; i++) {
            if (!(wp.equals(ResultsWordPrediction.NO_PRED) &&  lettersNoPred.get(i).equals(""))) {
                keys[i] = new ToucheHexa(letters.get(i), x[i], y[i]);
                keys[i].addObserver(this);
                if (i<nbKeysPredicted && wp.equals(ResultsWordPrediction.PRED)) {
                    keys[i].setIsPredictedKey(true);
                }
            }
        }

        phrases = generatePhrases();
		phraseArea.setText(texteSuivant());
        ExpeLogger.debutDePhrase(phraseArea.getText());

        if (wp.equals(ResultsWordPrediction.PRED)) {
            updateClavier();
        }
		addMouseListener(this);
		addMouseMotionListener(this);

        addMouseMotionListener(new MouseMotionListener() {

            @Override
            public void mouseDragged(MouseEvent e) {}

            @Override
            public void mouseMoved(MouseEvent e) {
                ExpeLogger.mouvementSouris(e.getX(), e.getY());
            }
			
		});
    }

    private List<String> generatePhrases() {
		List<String> phrases = new ArrayList<>();
		String chemin = "Files/Prediction/PhraseSet_French.txt";
		try {
            BufferedReader phrasesBufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(chemin), "UTF-8"));
            try {
                String ligne;
                while((ligne = phrasesBufferedReader.readLine()) != null)
                {
                    phrases.add(ligne);
                }
            } finally {
                phrasesBufferedReader.close();
            }
        } catch(IOException e)
        {
          e.printStackTrace();
        }
		return phrases;
	}

	private String texteSuivant() {
		if (phrases.size()<=0) {
			return null;
		}
		Random rand = new Random();
		int indice = rand.nextInt(phrases.size());
		return phrases.remove(indice);
	}

    public void valider() {
        long timeElapsed = System.currentTimeMillis()-depart;
        ExpeLogger.finDePhrase();
        textPane.setText("");
        currentChar = 0;
        if (timeElapsed>=timer) {
            LaunchSecondKeyboard(); 
            return;
        }
        long tempsRestant = (timer-timeElapsed);
        System.out.println("Temps restants : "+ tempsRestant / (60000) + " min " + (tempsRestant % (60000)) / 1000 + " sec " + tempsRestant % 1000 + " ms");
        phraseArea.setText(texteSuivant());
        ExpeLogger.debutDePhrase(phraseArea.getText());
        reset();
    }
    
    public void updateClavier() {
        letters = arbre.predictNext(false);
        ExpeLogger.debutPrediction();
        for (int i=0; i<nbKeysPredicted && i<letters.size(); i++) {
            keys[i].changeLetter(letters.get(i));
            ExpeLogger.resultatPrediction(letters.get(i), i);
        }
        ExpeLogger.finPrediction();
    }

    public void predict(String letter) {
        if (letter.equals("supp")) {
            supp();
            return;
        }
        arbre = arbre.goTo(letter);
        if (arbre == null) {
            reset();
        }
    }

    public void reset() {
        arbre = predicteur.getRacine();
    }

    public void supp() {
        Tree parent = arbre.getparent();
        if (parent != null) {
            arbre = parent;
        } else {
            reset();
        }
    }

    private void updateInputText(String str, boolean isError) throws BadLocationException {
		String txt = textPane.getText();
        switch (str) {
            case "_":
                textPane.setText(txt+" ");
                break;
            case "supp":
                if (txt.length()>0) {
                    StyledDocument doc = textPane.getStyledDocument();
                    int length = doc.getLength();
                    if (length > 0) {
                        try {
                            doc.remove(length - 1, 1);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
                break;
            default:
                SimpleAttributeSet set;
                if (isError) {
                    set = error;
                } else {
                    set = basic;
                }
                Document doc = textPane.getStyledDocument();
                doc.insertString(doc.getLength(), str, set);
                break;
        }
	}

    private int increment(int v, int v_max) {
        v++;
        if (v>=v_max) {
            v = v_max;
        }
        return v;
    }

    private boolean isCorrect(String str) {
        if (!(""+phraseArea.getText().charAt(currentChar)).equals(str) && !str.equals("supp")) {
            nbErrors++;
            currentChar = increment(currentChar, phraseArea.getText().length()-1);
            return false;
        } else if (str.equals("supp")) {
            currentChar--;
            if (currentChar<0) {
                currentChar = 0;
            }
        } else {
            currentChar = increment(currentChar, phraseArea.getText().length()-1);
        }
        return true;
    }

    private void LaunchSecondKeyboard() {
        nbClavier--;
        if (mode.equals(Mode.EXP)) {
            switch(wp) {
                case PRED:
                    wp = ResultsWordPrediction.NO_PRED;
                    break;
                case NO_PRED:
                    wp = ResultsWordPrediction.PRED;
                    break;
            }
        }
        if (nbClavier>0) {
            clavierFrame.launchSecondKeyboard(mode, wp, nbPart);
            return;
        }
        nbPart --;
        if (nbPart>0) {
            clavierFrame.launchKeyboardNewPart(mode, wp, nbPart);
            return;
        }
        ExpeLogger.finSimulation();
        System.exit(0);
    }

    public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D)g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
        for (ToucheHexa k : keys) {
            if (k!=null) {
                k.paint(g2);
            }
        }
        validerTouche.paint(g2);
	}

    @Override
    public void update(Observable o, Object arg) {
        if(o instanceof Touche){
			String s = (String)arg;
			System.out.println(s);
		}
    }

    @Override
    public void mouseDragged(java.awt.event.MouseEvent e) {
        for (ToucheHexa k : keys) {
            if (k!=null) {
                k.mouseDragged(getMousePosition());
            }
        }
        validerTouche.mouseDragged(getMousePosition());
		repaint();
    }

    @Override
    public void mouseMoved(java.awt.event.MouseEvent e) {
        for (ToucheHexa k : keys) {
            if (k!=null) {
                k.mouseMoved(e.getPoint());
            }
        }
        validerTouche.mouseMoved(e.getPoint());
		repaint();
    }

    @Override
    public void mouseClicked(java.awt.event.MouseEvent e) {
        // TODO Auto-generated method stub
    }

    @Override
    public void mousePressed(java.awt.event.MouseEvent e) {
        for (ToucheHexa k : keys) {
            if (k!=null && k.mousePressed(e.getPoint())) {
                predict(k.getStr());
                boolean isPredicted = k.isPredictedKey();
                boolean isError = !isCorrect(k.getStr());
                ExpeLogger.selectionCaractere(k.getStr(), k.centreX, k.centreY, !isError, isPredicted);
                try {
                    updateInputText(k.getStr(), isError);
                } catch (BadLocationException err) {
                    err.printStackTrace();
                }
            }
        }

        if (validerTouche.mousePressed(e.getPoint())) {
            valider();
        }
        if (wp.equals(ResultsWordPrediction.PRED)) {
            updateClavier();
            repaint();
        }
       
    }

    @Override
    public void mouseReleased(java.awt.event.MouseEvent e) {
        for (ToucheHexa k : keys) {
            if (k!=null) {
                k.mouseReleased(e.getPoint());
            }
        }
        validerTouche.mouseReleased(e.getPoint());
		repaint();
    }

    @Override
    public void mouseEntered(java.awt.event.MouseEvent e) {
        // TODO Auto-generated method stub
    }

    @Override
    public void mouseExited(java.awt.event.MouseEvent e) {
        // TODO Auto-generated method stub
    }
}