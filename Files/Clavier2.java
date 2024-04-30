import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JComponent;
import javax.swing.JTextArea;

public class Clavier2 extends JComponent implements Observer, MouseListener, MouseMotionListener {
    private static final int NB_KEYS = 41;
    private static final long serialVersionUID = 1L;
	private static final int LONGUEUR = 400;
	private static final int HAUTEUR = 400;

    private Predictor predicteur = new Predictor();
    private Tree arbre;

    private ToucheHexa[] keys;
    private int keyWidth = 85;
    private int keyHeight = (keyWidth+10)/2;
    private int nbKeysPredicted = 3;

    //private float[] lignes = {0.5f, 1, 1.5f, 2, 2.5f, 3, 3.5f, 4, 4.5f, 5, 5.5f, 6, 6.5f};
    private float[] lignes = {1.5f, 2, 2.5f, 3, 3.5f, 4, 4.5f, 5, 5.5f, 6, 6.5f, 7f, 7.5f};
    private float[] colonnes = {1, 1.5f, 2, 2.5f, 3, 3.5f, 4};

    private List<String> letters = new ArrayList<>(List.of("", "", "", "E", "A", "I", "S", "N", "R", "T", "O", "L", "U", "D", "C", "M",
                     "P", "G", "B", "V", "H", "F", "Q", "Y", "X", "J", "'", "W", "?", ".", ",", "\u2190", "!", "_", "Ç", "É", "À", "È", "Ù", "K", "Z"));

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

    public Clavier2(JTextArea jTextArea1) {
        super();
        predicteur.createTree();
        arbre = predicteur.getRacine();
        keys = new ToucheHexa[NB_KEYS];
		setPreferredSize(new Dimension(LONGUEUR, HAUTEUR));

        for (int i=0; i<NB_KEYS; i++) {
            keys[i] = new ToucheHexa(letters.get(i), x[i], y[i]);
            keys[i].setTextArea(jTextArea1);
            keys[i].addObserver(this);
            if (i<nbKeysPredicted) {
                keys[i].isPredictedKey();
            }
        }

        updateClavier();
		addMouseListener(this);
		addMouseMotionListener(this);
    }
    
    public void updateClavier() {
        letters = arbre.predictNext(true);
        
        for (int i=0; i<nbKeysPredicted && i<letters.size(); i++) {
            keys[i].changeLetter(letters.get(i));
        }
    }

    public void predict(String letter) {
        if (letter.equals("\u2190")) {
            System.out.println("SUPPRESSION");
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


    public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D)g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
        for (ToucheHexa k : keys) {
            k.paint(g2);
        }
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
            k.mouseDragged(getMousePosition());
        }
		repaint();
    }

    @Override
    public void mouseMoved(java.awt.event.MouseEvent e) {
        for (ToucheHexa k : keys) {
            k.mouseMoved(e.getPoint());
        }
		repaint();
    }

    @Override
    public void mouseClicked(java.awt.event.MouseEvent e) {
        // TODO Auto-generated method stub
    }

    @Override
    public void mousePressed(java.awt.event.MouseEvent e) {
        for (ToucheHexa k : keys) {
            if (k.mousePressed(e.getPoint())) {
                predict(k.getStr());
            }
        }
        updateClavier();
		repaint();
    }

    @Override
    public void mouseReleased(java.awt.event.MouseEvent e) {
        for (ToucheHexa k : keys) {
            k.mouseReleased(e.getPoint());
        }
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
