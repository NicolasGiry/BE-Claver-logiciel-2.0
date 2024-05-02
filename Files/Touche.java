import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Shape;
import java.awt.font.LineMetrics;
import java.awt.geom.Rectangle2D;
import java.util.Observable;

import javax.swing.JTextArea;
import javax.swing.JTextPane;

public class Touche extends Observable {
    private static final int LONGUEUR = 60;
	private static final int HAUTEUR = 40;
	
	int centreX, centreY;
	String str;
	Etat etat;
	Shape forme;
    Font f;
    private JTextPane jTextPane;
    boolean isPredictedKey, isValiderTouche;
	
	public Touche(String str, int cX, int cY){
		this.str = str;
		centreX = cX;
		centreY = cY;
		etat = Etat.NORMAL;
		
		forme = new Rectangle2D.Double(cX-LONGUEUR/2, cY-HAUTEUR/2, LONGUEUR, HAUTEUR);
	}
	
	public void setState(Etat s) {
		etat = s;
	}
	
	public void mouseMoved(Point p) {
		if(forme.contains(p)){
			if(etat == Etat.NORMAL)
				etat = Etat.SURVOL;
		}else{
			etat = Etat.NORMAL;
		}
	}
	
	public void mouseReleased(Point p) {
		if(forme.contains(p)){
			etat = Etat.SURVOL;
		}else{
			etat = Etat.NORMAL;
		}
	}

	
	
	public boolean mousePressed(Point p) {
		if(forme.contains(p)){
			etat = Etat.PRESSE;
			///sendInfo("[KeyPressed]"+str+";x="+centreX+";y="+centreY);
            return true;
		}
        return false;
	}
	
	public void mouseDragged(Point p) {
		if(!forme.contains(p)){
			etat = Etat.NORMAL;
		}
	}
	
	public void paint(Graphics2D g2) {
		switch (etat) {
			case NORMAL:
                if (isPredictedKey) {
                    g2.setStroke(new BasicStroke(1.5f));
				    g2.setColor(Color.BLUE);
                } else {
                    g2.setStroke(new BasicStroke(1f));
                    g2.setColor(Color.BLACK);
                }
				
			break;
			case SURVOL:
                if (isPredictedKey) {
                    g2.setStroke(new BasicStroke(2.5f));
                    g2.setColor(Color.CYAN);
                } else {
                    g2.setStroke(new BasicStroke(2f));
                    g2.setColor(Color.LIGHT_GRAY);
                }
				break;
			case PRESSE:
                if (isPredictedKey) {
                    g2.setStroke(new BasicStroke(2.5f));
                    g2.setColor(Color.BLUE);
                } else {
                    g2.setStroke(new BasicStroke(2f));
                    g2.setColor(Color.BLACK);
                }
				break;
			default:
				break;
		}
		g2.draw(forme);
		f = new Font(Font.SANS_SERIF, Font.BOLD, 20);
		paintText(g2, f, forme.getBounds2D(), str);
	}
	
	public static void paintText(Graphics2D g2, Font f, Rectangle2D box, String text){
		int paddingLeft = 10;
		int paddingRight = 10;
		
		java.awt.font.FontRenderContext frc = g2.getFontRenderContext();
		Rectangle2D bounds = f.getStringBounds(text,frc);
		LineMetrics metrics = f.getLineMetrics(text,frc);
		float width = (float)bounds.getWidth(); // Largeur du texte
		float lineheight = metrics.getHeight(); // Hauteur total de la ligne
		float ascent = metrics.getAscent();     // Du haut à la ligne de base
		
		int n=1;
		Font fBis = f;
		while(width>(box.getWidth()-paddingLeft-paddingRight) || lineheight>box.getHeight()){
			fBis = new Font(f.getName(),f.getStyle(),f.getSize()-n);
			bounds = fBis.getStringBounds(text,frc);
			metrics = fBis.getLineMetrics(text,frc);
			width = (float)bounds.getWidth();
			lineheight = metrics.getHeight();
			ascent = metrics.getAscent();
			n++;
		}
		
		float x0 = (float)(box.getX() + (box.getWidth() - width)/2);
		float y0 = (float)(box.getY() + (box.getHeight() - lineheight)/2 + ascent);
			
		g2.setFont(fBis);
		g2.drawString(text,x0,y0);
	}
	
	public void sendInfo(String s) {
		setChanged(); 
		notifyObservers(s); 
	}

    public void changeLetter(String newLetter) {
        str = newLetter;
    }

	public void setTextPane(JTextPane jTextPane) {
        this.jTextPane = jTextPane;
    }

    public void setIsPredictedKey(boolean b) {
        isPredictedKey = b;
    }

	public void setIsValiderTouche(boolean b) {
        isValiderTouche = b;
    }

	public boolean isValiderTouche() {
        return isValiderTouche;
    }

	public boolean isPredictedKey() {
        return isPredictedKey;
    }

    public String getStr() {
        switch (str) {
            case "_":
                return " ";
            case "\u2190":
				return "supp";
            default:
                return str;
        }
    }
}