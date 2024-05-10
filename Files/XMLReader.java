import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class XMLReader {

    private static XMLReader parser = new XMLReader();
	
	BufferedWriter txtFile;

    //public void readXMLFile(String chemin) {
    public void readXMLFile(File file) {
        initCSV(file);

        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();

            Document document = builder.parse(file);
            document.getDocumentElement().normalize();

            String key_mode_part = "\n";
            NodeList settings = document.getElementsByTagName("Settings");

            Node settingsNode = settings.item(0);
            Element settingsElement = (Element) settingsNode;

            key_mode_part += settingsElement.getElementsByTagName("Keyboard").item(0).getAttributes().getNamedItem("name").getTextContent();
            key_mode_part += ";";
            key_mode_part += settingsElement.getElementsByTagName("Mode").item(0).getAttributes().getNamedItem("type").getTextContent();
            key_mode_part += ";";
            key_mode_part += settingsElement.getElementsByTagName("Participant").item(0).getAttributes().getNamedItem("id").getTextContent();
            key_mode_part += ";";

            NodeList phrasesNodeList = document.getElementsByTagName("Phrase");

            for (int i=0; i<phrasesNodeList.getLength(); i++) {
                Node phraseNode = phrasesNodeList.item(i);

                if (phraseNode.getNodeType() == Node.ELEMENT_NODE) {
                    parser.txtFile.write(key_mode_part);
                    Element phraseElement = (Element) phraseNode;
                    String phrase = phraseElement.getAttribute("string");
                    parser.txtFile.write(phrase+";");
                    parser.txtFile.write(phrase.length()+";");
                    double temps_depart = Double.parseDouble(phraseElement.getAttribute("t")), temps=0;
                    int nb_error = 0, nb_predicted_used = 0;

                    NodeList attributs = phraseElement.getChildNodes();
                    String phraseTapee = "";
                    for (int j=0; j<attributs.getLength(); j++) {
                        Node attribut = attributs.item(j);
                        if (attribut.getNodeType() == Node.ELEMENT_NODE) {
                            Element attributElement = (Element) attribut;
                            if (attributElement.getAttribute("isCorrect").equals("false")) {
                                nb_error ++;
                            }
                            if (attributElement.getAttribute("isPredicted").equals("true")) {
                                nb_predicted_used ++;
                            }
                            temps = Double.parseDouble(attributElement.getAttribute("t")) - temps_depart;
                            String lettre = attributElement.getAttribute("name");

                            if (lettre.equals("supp")) {
                                phraseTapee = phraseTapee.substring(0, phraseTapee.length()-1);
                            } else {
                                phraseTapee += lettre;
                            }
                        }
                    }

                    parser.txtFile.write((temps/1000)+";");
                    parser.txtFile.write((phraseTapee.equals(phrase))+";");
                    parser.txtFile.write(nb_error+";");
                    parser.txtFile.write(nb_predicted_used+";");
                    if (phraseTapee.length()>0) {
                        parser.txtFile.write(((float)nb_predicted_used/(phraseTapee.length())) *100+"%;");
                    } else {
                        parser.txtFile.write(";");
                    }
                    parser.txtFile.write(phraseTapee.length()/(temps/1000)+";");
                    parser.txtFile.write("d");

                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            parser.txtFile.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initCSV(File file) {
        try {
			StringBuffer fileName = new StringBuffer();
            String folderName = "Files/logsCSV/";
            // if (System.getProperty("os.name").toLowerCase().contains("windows")) {
            //     folderName = "Files/logsCSV/";
            // }
            fileName.append(folderName);
			fileName.append(file.getName());
            fileName.append("_PARSER.csv");
			
			parser.txtFile = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileName.toString()),StandardCharsets.UTF_8));
		} catch (Exception e) {
			e.printStackTrace();
		}
        try {
			parser.txtFile.write("Keyboard;Mode;Participant;Phrase;NbChar;Duree;Correct;NbErrors;NbPredictionUsed;predCharRatio;CarParSec;Distance");
        } catch (IOException e) {
			e.printStackTrace();
		}
    }

    public static void main(String[] args) {
        XMLReader reader = new XMLReader();
        String folderName = "Files/logs";
        // if (System.getProperty("os.name").toLowerCase().contains("windows")) {
        //     folderName = "Files/logs";
        // }
        File folder = new File(folderName);
        if (folder.isDirectory()) {
            File[] files = folder.listFiles();
            for (File file : files) {
                //reader.readXMLFile("Files/logs/2024_5_2_14_47_1_TRAIN_P1_TROIS_PRED");
                reader.readXMLFile(file);
            }
        } else {
            System.out.println("Le chemin spécifié n'est pas un dossier.");
        }

       
    }
}
