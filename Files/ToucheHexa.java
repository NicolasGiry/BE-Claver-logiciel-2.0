import java.awt.Graphics2D;
import java.awt.geom.GeneralPath;

public class ToucheHexa extends Touche{
	private static final int RAYON = 25;
	private GeneralPath gp;
	
	public ToucheHexa(String str, int cX, int cY){
        super(str, cX, cY);
        int x0 = cX - RAYON;
        int y0 = cY;
        int x1 = (int)(cX - (Math.cos(Math.PI/3) * RAYON));
        int y1 = (int)(cY - (Math.sin(Math.PI/3) * RAYON));
        int x2 = (int)(cX + (Math.cos(Math.PI/3) * RAYON));
        int y2 = (int)(cY - (Math.sin(Math.PI/3) * RAYON));
        int x3 = cX + RAYON;
        int y3 = cY;
        int x4 = (int)(cX + (Math.cos(Math.PI/3) * RAYON));
        int y4 = (int)(cY + (Math.sin(Math.PI/3) * RAYON));
        int x5 = (int)(cX - (Math.cos(Math.PI/3) * RAYON));
        int y5 = (int)(cY + (Math.sin(Math.PI/3) * RAYON));
        gp = new GeneralPath();
        gp.moveTo(x0, y0);
        gp.lineTo(x1, y1);
        gp.lineTo(x2, y2);
        gp.lineTo(x3, y3);
        gp.lineTo(x4, y4);
        gp.lineTo(x5, y5);
        gp.closePath();
        forme = gp;
    }
}
