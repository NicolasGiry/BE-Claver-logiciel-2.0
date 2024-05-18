import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Calendar;

public class ExpeLogger{
	private static ExpeLogger logger = new ExpeLogger();
	
	BufferedWriter txtFile;
	
	private ExpeLogger(){}
	
	public static void debutSimulation(ResultsWordPrediction wp, int participant, Mode mode){
		long time = System.currentTimeMillis();
		Calendar c=Calendar.getInstance();
		c.setTimeInMillis(time);
		int year = c.get(Calendar.YEAR);
		int month = c.get(Calendar.MONTH)+1;
		int day = c.get(Calendar.DAY_OF_MONTH);
		int hour = c.get(Calendar.HOUR_OF_DAY);
		int minute = c.get(Calendar.MINUTE);
		int second = c.get(Calendar.SECOND);
		
		System.out.println(day+"/"+month+"/"+year+" - "+hour+":"+minute+":"+second);
		
		try {
			StringBuffer fileName = new StringBuffer("Files/logs/");
			fileName.append(year);
			fileName.append("_");
			fileName.append(month);
			fileName.append("_");
			fileName.append(day);
			fileName.append("_");
			fileName.append(hour);
			fileName.append("_");
			fileName.append(minute);
			fileName.append("_");
			fileName.append(second);
			fileName.append("_");
			fileName.append(mode.name());
			fileName.append("_P");
			fileName.append(participant);
			fileName.append("_");
			fileName.append(wp.name());
			
			logger.txtFile = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileName.toString()),StandardCharsets.UTF_8));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		try {
			logger.txtFile.write("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
			logger.txtFile.newLine();
			logger.txtFile.write("<ExpeKeyboard xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:noNamespaceSchemaLocation=\"expekeyboard.xsd\">");
			logger.txtFile.newLine();
			logger.txtFile.write("\t<Settings t=\"");
			logger.txtFile.write(String.valueOf(time));
			logger.txtFile.write("\">");
			logger.txtFile.newLine();
			logger.txtFile.write("\t\t<Keyboard name=\""+wp.name()+"\"/>");
			logger.txtFile.newLine();
			logger.txtFile.write("\t\t<Mode type=\""+mode.name()+"\"/>");
			logger.txtFile.newLine();
			logger.txtFile.write("\t\t<Participant id=\""+participant+"\"/>");
			logger.txtFile.newLine();
			logger.txtFile.write("\t</Settings>");
			logger.txtFile.newLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void finSimulation(){
		try {
			System.out.println("FIN SIMULATION");
			logger.txtFile.write("</ExpeKeyboard>");
			logger.txtFile.newLine();
			logger.txtFile.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void debutDePhrase(String phrase){
		try {
			long time = System.currentTimeMillis();
			logger.txtFile.write("\t<Phrase string=\"");
			logger.txtFile.write(phrase);
			logger.txtFile.write("\" t=\"");
			logger.txtFile.write(String.valueOf(time));
			logger.txtFile.write("\">");
			logger.txtFile.newLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void finDePhrase() {
		try {
			logger.txtFile.write("\t</Phrase>");
			logger.txtFile.newLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void ChangementParametres(ResultsWordPrediction wp, Mode mode, int nbPart) {
		try {
			long time = System.currentTimeMillis();
			logger.txtFile.write("\t<Settings t=\"");
			logger.txtFile.write(String.valueOf(time));
			logger.txtFile.write("\">");
			logger.txtFile.newLine();
			logger.txtFile.write("\t\t<Keyboard name=\""+wp.name()+"\"/>");
			logger.txtFile.newLine();
			logger.txtFile.write("\t\t<Mode type=\""+mode.name()+"\"/>");
			logger.txtFile.newLine();
			logger.txtFile.write("\t\t<Participant id=\""+nbPart+"\"/>");
			logger.txtFile.newLine();
			logger.txtFile.write("\t</Settings>");
			logger.txtFile.newLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void mouvementSouris(int x, int y) {
		try {
			long time = System.currentTimeMillis();
			logger.txtFile.write("\t\t<MouvementSouris t=\""+String.valueOf(time)+"\" x=\""+x+"\" y=\""+y+"\"/>");
			logger.txtFile.newLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void selectionMot(String name, int x, int y) {
		try {
			long time = System.currentTimeMillis();
			logger.txtFile.write("\t\t<SelectionMot name=\"");
			logger.txtFile.write(name);
			logger.txtFile.write("\" x=\"");
			logger.txtFile.write(String.valueOf(x));
			logger.txtFile.write("\" y=\"");
			logger.txtFile.write(String.valueOf(y));
			logger.txtFile.write("\" t=\"");
			logger.txtFile.write(String.valueOf(time));
			logger.txtFile.write("\"/>");
			logger.txtFile.newLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void selectionCaractere(String car, int x, int y, boolean correct, boolean isPredicted, int xSouris, int ySouris) {
		try {
			long time = System.currentTimeMillis();
			logger.txtFile.write("\t\t<SelectionCaractere name=\"");
			logger.txtFile.write(car);
			logger.txtFile.write("\" x=\"");
			logger.txtFile.write(String.valueOf(x));
			logger.txtFile.write("\" y=\"");
			logger.txtFile.write(String.valueOf(y));
			logger.txtFile.write("\" xSouris=\"");
			logger.txtFile.write(String.valueOf(xSouris));
			logger.txtFile.write("\" ySouris=\"");
			logger.txtFile.write(String.valueOf(ySouris));
			logger.txtFile.write("\" isCorrect=\"");
			logger.txtFile.write(String.valueOf(correct));
			logger.txtFile.write("\" isPredicted=\"");
			logger.txtFile.write(String.valueOf(isPredicted));
			logger.txtFile.write("\" t=\"");
			logger.txtFile.write(String.valueOf(time));
			logger.txtFile.write("\"/>");
			logger.txtFile.newLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void debutPrediction(){
		try {
			long time = System.currentTimeMillis();
			logger.txtFile.write("\t\t<Prediction t=\"");
			logger.txtFile.write(String.valueOf(time));
			logger.txtFile.write("\">");
			logger.txtFile.newLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void resultatPrediction(String letter, int position) {
		try {
			logger.txtFile.write("\t\t\t<ResultatPrediction letter=\"");
			logger.txtFile.write(letter);
			logger.txtFile.write("\" position=\"");
			logger.txtFile.write(String.valueOf(position));
			logger.txtFile.write("\"/>");
			logger.txtFile.newLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void finPrediction(){
		try {
			logger.txtFile.write("\t\t</Prediction>");
			logger.txtFile.newLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}