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

    private String getSettingsString(Document document, int i, int nbPart) {
        String key_mode_part = "\n";
        NodeList settings = document.getElementsByTagName("Settings");
        Node settingsNode = settings.item(i);
        Element settingsElement = (Element) settingsNode;

        key_mode_part += settingsElement.getElementsByTagName("Keyboard").item(0).getAttributes().getNamedItem("name").getTextContent();
        key_mode_part += ";";
        key_mode_part += settingsElement.getElementsByTagName("Mode").item(0).getAttributes().getNamedItem("type").getTextContent();
        key_mode_part += ";";
        key_mode_part += nbPart - Integer.valueOf(settingsElement.getElementsByTagName("Participant").item(0).getAttributes().getNamedItem("id").getTextContent())+1;
        key_mode_part += ";";
        return key_mode_part;
    }

    private int getNbPart(Document document) {
        NodeList settings = document.getElementsByTagName("Settings");
        Node settingsNode = settings.item(0);
        Element settingsElement = (Element) settingsNode;
        return Integer.valueOf(settingsElement.getElementsByTagName("Participant").item(0).getAttributes().getNamedItem("id").getTextContent());
    }

    public void readXMLFileMot(File file) {
        initCSV(file, "MOT");
        int indice = 0, nbPart;
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(file);
            document.getDocumentElement().normalize();
            nbPart = getNbPart(document);
            String settingsString = getSettingsString(document, 0, nbPart);
            NodeList nodeList = document.getDocumentElement().getChildNodes();
            
            for (int i=0; i<nodeList.getLength(); i++) {
                Node node = nodeList.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;
                    if (element.getTagName().equals("Settings")) {
                        settingsString = getSettingsString(document, indice, nbPart);
                        indice++;
                    } else if (element.getTagName().equals("Phrase")) {
                        int debutMot = 0, totalLenght = 0;
                        String phrase = element.getAttribute("string");
                        NodeList attributs = element.getChildNodes();
                        boolean finMot = false;
                        double temps_depart = Double.parseDouble(element.getAttribute("t")), temps=0, distance = 0;
                        int nb_error = 0, nb_predicted_used = 0, x, y, oldX=-1, oldY=-1;
                        String phraseTapee = "";
                        parser.txtFile.write(settingsString);
                        for (int j=0; j<attributs.getLength(); j++) {
                            if (finMot) {
                                parser.txtFile.write(phraseTapee+";");                                                      // mot
                                parser.txtFile.write(phraseTapee.length()+";");                                             // NbChar
                                parser.txtFile.write((temps/1000)+";");                                                     // Duree
                                parser.txtFile.write(phraseTapee.equals(phrase.substring(debutMot, totalLenght-1))+";");    // Correct
                                parser.txtFile.write(nb_error+";");                                                         // NbErrors
                                parser.txtFile.write(nb_predicted_used+";");                                                // NbPredictionUsed
                                if (phraseTapee.length()>0) {                                                               // 
                                    parser.txtFile.write(((float)nb_predicted_used/(phraseTapee.length())) *100+"%;");      //
                                } else {                                                                                    // predCharRatio       
                                    parser.txtFile.write(";");                                                          //
                                }                                                                                           //
                                parser.txtFile.write(phraseTapee.length()/(temps/1000)+";");                                // CharParSec
                                parser.txtFile.write(distance+"");                                                          // Distance
                                parser.txtFile.write(settingsString);
                                finMot = false;
                                phraseTapee = "";
                                temps_depart = Double.parseDouble(element.getAttribute("t"));
                                nb_error = 0; nb_predicted_used = 0; oldX=-1; oldY=-1;
                                debutMot = totalLenght;
                            } else {
                                Node attribut = attributs.item(j);
                                if (attribut.getNodeType() == Node.ELEMENT_NODE && attribut.getNodeName().equals("SelectionCaractere")) {
                                    totalLenght++;
                                    if (totalLenght>phrase.length()) {
                                        totalLenght = phrase.length();
                                    }
                                    Element attributElement = (Element) attribut;
                                    String lettre = attributElement.getAttribute("name");
                                    finMot = lettre.equals(" ");
                                    if (lettre.equals("supp") && phraseTapee.length() > 0) {
                                        phraseTapee = phraseTapee.substring(0, phraseTapee.length()-1);
                                        totalLenght-=2;
                                    } else if (!lettre.equals("supp") && !lettre.equals(" ")) {
                                        phraseTapee += lettre;
                                    }
                                    if (attributElement.getAttribute("isCorrect").equals("false")) {
                                        nb_error ++;
                                    }
                                    if (attributElement.getAttribute("isPredicted").equals("true")) {
                                        nb_predicted_used ++;
                                    }
                                    temps = Double.parseDouble(attributElement.getAttribute("t")) - temps_depart;
                                    x = Integer.parseInt(attributElement.getAttribute("x"));
                                    y = Integer.parseInt(attributElement.getAttribute("y"));
                                    if (oldX!=-1 && oldY!=-1) {
                                        distance += Math.sqrt((oldX-x)*(oldX-x) + (oldY-y)*(oldY-y));
                                    } 
                                    oldX = x;
                                    oldY = y;
                                }
                            }
                        }
                        parser.txtFile.write(phraseTapee+";");
                        parser.txtFile.write(phraseTapee.length()+";");
                        parser.txtFile.write((temps/1000)+";");
                        parser.txtFile.write(phraseTapee.equals(phrase.substring(debutMot, totalLenght))+";");
                        parser.txtFile.write(nb_error+";");
                        parser.txtFile.write(nb_predicted_used+";");
                        if (phraseTapee.length()>0) {
                            parser.txtFile.write(((float)nb_predicted_used/(phraseTapee.length())) *100+"%;");
                        } else {
                            parser.txtFile.write(";");
                        }
                        parser.txtFile.write(phraseTapee.length()/(temps/1000)+";");
                        parser.txtFile.write(distance+"");
                    }
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

    // public void readXMLFileMot(File file) {
    //     initCSV(file, "MOT");
    //     int indice = 1, nbPart;
    //     try {
    //         DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    //         DocumentBuilder builder = factory.newDocumentBuilder();
    //         Document document = builder.parse(file);
    //         document.getDocumentElement().normalize();
    //         nbPart = getNbPart(document);
    //         String settingsString = getSettingsString(document, 0, nbPart);
    //         NodeList phrasesNodeList = document.getElementsByTagName("Phrase");
            
    //         for (int i=0; i<phrasesNodeList.getLength(); i++) {
    //             Node phraseNode = phrasesNodeList.item(i);
    //             int debutMot = 0, totalLenght = 0;
    //             if (phraseNode.getNodeType() == Node.ELEMENT_NODE) {
    //                 Element phraseElement = (Element) phraseNode;
    //                 String phrase = phraseElement.getAttribute("string");
    //                 NodeList attributs = phraseElement.getChildNodes();
    //                 boolean finMot = false;
    //                 double temps_depart = Double.parseDouble(phraseElement.getAttribute("t")), temps=0, distance = 0;
    //                 int nb_error = 0, nb_predicted_used = 0, x, y, oldX=-1, oldY=-1;
    //                 String phraseTapee = "";
    //                 parser.txtFile.write(settingsString);
    //                 for (int j=0; j<attributs.getLength(); j++) {
    //                     if (finMot) {
    //                         parser.txtFile.write(phraseTapee+";");                                                      // mot
    //                         parser.txtFile.write(phraseTapee.length()+";");                                             // NbChar
    //                         parser.txtFile.write((temps/1000)+";");                                                     // Duree
    //                         parser.txtFile.write(phraseTapee.equals(phrase.substring(debutMot, totalLenght-1))+";");    // Correct
    //                         parser.txtFile.write(nb_error+";");                                                         // NbErrors
    //                         parser.txtFile.write(nb_predicted_used+";");                                                // NbPredictionUsed
    //                         if (phraseTapee.length()>0) {                                                               // 
    //                             parser.txtFile.write(((float)nb_predicted_used/(phraseTapee.length())) *100+"%;");      //
    //                         } else {                                                                                    // predCharRatio       
    //                             parser.txtFile.write(";");                                                          //
    //                         }                                                                                           //
    //                         parser.txtFile.write(phraseTapee.length()/(temps/1000)+";");                                // CharParSec
    //                         parser.txtFile.write(distance+"");                                                          // Distance
    //                         parser.txtFile.write(settingsString);
    //                         finMot = false;
    //                         phraseTapee = "";
    //                         temps_depart = Double.parseDouble(phraseElement.getAttribute("t"));
    //                         nb_error = 0; nb_predicted_used = 0; oldX=-1; oldY=-1;
    //                         debutMot = totalLenght;
    //                     } else {
    //                         Node attribut = attributs.item(j);
    //                         if (attribut.getNodeType() == Node.ELEMENT_NODE && attribut.getNodeName().equals("SelectionCaractere")) {
    //                             totalLenght++;
    //                             if (totalLenght>phrase.length()) {
    //                                 totalLenght = phrase.length();
    //                             }
    //                             Element attributElement = (Element) attribut;
    //                             String lettre = attributElement.getAttribute("name");
    //                             finMot = lettre.equals(" ");
    //                             if (lettre.equals("supp") && phraseTapee.length() > 0) {
    //                                 phraseTapee = phraseTapee.substring(0, phraseTapee.length()-1);
    //                                 totalLenght-=2;
    //                             } else if (!lettre.equals("supp") && !lettre.equals(" ")) {
    //                                 phraseTapee += lettre;
    //                             }
    //                             if (attributElement.getAttribute("isCorrect").equals("false")) {
    //                                 nb_error ++;
    //                             }
    //                             if (attributElement.getAttribute("isPredicted").equals("true")) {
    //                                 nb_predicted_used ++;
    //                             }
    //                             temps = Double.parseDouble(attributElement.getAttribute("t")) - temps_depart;
    //                             x = Integer.parseInt(attributElement.getAttribute("x"));
    //                             y = Integer.parseInt(attributElement.getAttribute("y"));
    //                             if (oldX!=-1 && oldY!=-1) {
    //                                 distance += Math.sqrt((oldX-x)*(oldX-x) + (oldY-y)*(oldY-y));
    //                             } 
    //                             oldX = x;
    //                             oldY = y;
    //                         }
    //                     }
    //                 }
    //                 parser.txtFile.write(phraseTapee+";");
    //                 parser.txtFile.write(phraseTapee.length()+";");
    //                 parser.txtFile.write((temps/1000)+";");
    //                 parser.txtFile.write(phraseTapee.equals(phrase.substring(debutMot, totalLenght))+";");
    //                 parser.txtFile.write(nb_error+";");
    //                 parser.txtFile.write(nb_predicted_used+";");
    //                 if (phraseTapee.length()>0) {
    //                     parser.txtFile.write(((float)nb_predicted_used/(phraseTapee.length())) *100+"%;");
    //                 } else {
    //                     parser.txtFile.write(";");
    //                 }
    //                 parser.txtFile.write(phraseTapee.length()/(temps/1000)+";");
    //                 parser.txtFile.write(distance+"");
    //             }
    //         }
    //     } catch (Exception e) {
    //         e.printStackTrace();
    //     }

    //     try {
    //         parser.txtFile.close();
    //     } catch (IOException e) {
    //         e.printStackTrace();
    //     }
    // }

    public void readXMLFilePhrase(File file) {
        initCSV(file, "PHRASE");
        int indice = 0, nbPart;

        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(file);
            document.getDocumentElement().normalize();
            nbPart = getNbPart(document);
            String settingsString = getSettingsString(document, 0, nbPart);
            NodeList nodeList = document.getDocumentElement().getChildNodes();

            for (int i=0; i<nodeList.getLength(); i++) {
                Node node = nodeList.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;
                    if (element.getTagName().equals("Settings")) {
                        settingsString = getSettingsString(document, indice, nbPart);
                        indice++;
                    } else if (element.getTagName().equals("Phrase")) {
                        parser.txtFile.write(settingsString);
                        Element phraseElement = (Element) node;
                        String phrase = phraseElement.getAttribute("string");
                        parser.txtFile.write(phrase+";");
                        parser.txtFile.write(phrase.length()+";");
                        double temps_depart = Double.parseDouble(phraseElement.getAttribute("t")), temps=0, distance = 0;
                        int nb_error = 0, nb_predicted_used = 0, x, y, oldX=-1, oldY=-1;
                        NodeList attributs = phraseElement.getChildNodes();
                        String phraseTapee = "";
                        for (int j=0; j<attributs.getLength(); j++) {
                            Node attribut = attributs.item(j);
                            if (attribut.getNodeType() == Node.ELEMENT_NODE && attribut.getNodeName().equals("SelectionCaractere")) {
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
                                x = Integer.parseInt(attributElement.getAttribute("x"));
                                y = Integer.parseInt(attributElement.getAttribute("y"));
                                if (oldX!=-1 && oldY!=-1) {
                                    distance += Math.sqrt((oldX-x)*(oldX-x) + (oldY-y)*(oldY-y));
                                } 
                                oldX = x;
                                oldY = y;
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
                        parser.txtFile.write(distance+"");
                    }
                }
                    // if (node.getNodeType() == Node.ELEMENT_NODE) {
                    //     parser.txtFile.write(settingsString);
                    //     Element phraseElement = (Element) node;
                    //     String phrase = phraseElement.getAttribute("string");
                    //     parser.txtFile.write(phrase+";");
                    //     parser.txtFile.write(phrase.length()+";");
                    //     double temps_depart = Double.parseDouble(phraseElement.getAttribute("t")), temps=0, distance = 0;
                    //     int nb_error = 0, nb_predicted_used = 0, x, y, oldX=-1, oldY=-1;
                    //     NodeList attributs = phraseElement.getChildNodes();
                    //     String phraseTapee = "";
                    //     for (int j=0; j<attributs.getLength(); j++) {
                    //         Node attribut = attributs.item(j);
                    //         if (attribut.getNodeType() == Node.ELEMENT_NODE && attribut.getNodeName().equals("SelectionCaractere")) {
                    //             Element attributElement = (Element) attribut;
                    //             if (attributElement.getAttribute("isCorrect").equals("false")) {
                    //                 nb_error ++;
                    //             }
                    //             if (attributElement.getAttribute("isPredicted").equals("true")) {
                    //                 nb_predicted_used ++;
                    //             }
                    //             temps = Double.parseDouble(attributElement.getAttribute("t")) - temps_depart;
                    //             String lettre = attributElement.getAttribute("name");

                    //             if (lettre.equals("supp")) {
                    //                 phraseTapee = phraseTapee.substring(0, phraseTapee.length()-1);
                    //             } else {
                    //                 phraseTapee += lettre;
                    //             }
                    //             x = Integer.parseInt(attributElement.getAttribute("x"));
                    //             y = Integer.parseInt(attributElement.getAttribute("y"));
                    //             if (oldX!=-1 && oldY!=-1) {
                    //                 distance += Math.sqrt((oldX-x)*(oldX-x) + (oldY-y)*(oldY-y));
                    //             } 
                    //             oldX = x;
                    //             oldY = y;
                    //         }
                    //     }
                    //     parser.txtFile.write((temps/1000)+";");
                    //     parser.txtFile.write((phraseTapee.equals(phrase))+";");
                    //     parser.txtFile.write(nb_error+";");
                    //     parser.txtFile.write(nb_predicted_used+";");
                    //     if (phraseTapee.length()>0) {
                    //         parser.txtFile.write(((float)nb_predicted_used/(phraseTapee.length())) *100+"%;");
                    //     } else {
                    //         parser.txtFile.write(";");
                    //     }
                    //     parser.txtFile.write(phraseTapee.length()/(temps/1000)+";");
                    //     parser.txtFile.write(distance+"");
                    // }
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

    // public void readXMLFilePhrase(File file) {
    //     initCSV(file, "PHRASE");

    //     try {
    //         DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    //         DocumentBuilder builder = factory.newDocumentBuilder();
    //         Document document = builder.parse(file);
    //         document.getDocumentElement().normalize();
    //         String settingsString = getSettingsString(document);
    //         // NodeList phrasesNodeList = document.getElementsByTagName("Phrase");
    //         NodeList phrasesNodeList = document.getChildNodes()


    //         for (int i=0; i<phrasesNodeList.getLength(); i++) {
    //             Node phraseNode = phrasesNodeList.item(i);
    //             if (phraseNode.getNodeType() == Node.ELEMENT_NODE) {
    //                 parser.txtFile.write(settingsString);
    //                 Element phraseElement = (Element) phraseNode;
    //                 String phrase = phraseElement.getAttribute("string");
    //                 parser.txtFile.write(phrase+";");
    //                 parser.txtFile.write(phrase.length()+";");
    //                 double temps_depart = Double.parseDouble(phraseElement.getAttribute("t")), temps=0, distance = 0;
    //                 int nb_error = 0, nb_predicted_used = 0, x, y, oldX=-1, oldY=-1;
    //                 NodeList attributs = phraseElement.getChildNodes();
    //                 String phraseTapee = "";
    //                 for (int j=0; j<attributs.getLength(); j++) {
    //                     Node attribut = attributs.item(j);
    //                     if (attribut.getNodeType() == Node.ELEMENT_NODE && attribut.getNodeName().equals("SelectionCaractere")) {
    //                         Element attributElement = (Element) attribut;
    //                         if (attributElement.getAttribute("isCorrect").equals("false")) {
    //                             nb_error ++;
    //                         }
    //                         if (attributElement.getAttribute("isPredicted").equals("true")) {
    //                             nb_predicted_used ++;
    //                         }
    //                         temps = Double.parseDouble(attributElement.getAttribute("t")) - temps_depart;
    //                         String lettre = attributElement.getAttribute("name");

    //                         if (lettre.equals("supp")) {
    //                             phraseTapee = phraseTapee.substring(0, phraseTapee.length()-1);
    //                         } else {
    //                             phraseTapee += lettre;
    //                         }
    //                         x = Integer.parseInt(attributElement.getAttribute("x"));
    //                         y = Integer.parseInt(attributElement.getAttribute("y"));
    //                         if (oldX!=-1 && oldY!=-1) {
    //                             distance += Math.sqrt((oldX-x)*(oldX-x) + (oldY-y)*(oldY-y));
    //                         } 
    //                         oldX = x;
    //                         oldY = y;
    //                     }
    //                 }
    //                 parser.txtFile.write((temps/1000)+";");
    //                 parser.txtFile.write((phraseTapee.equals(phrase))+";");
    //                 parser.txtFile.write(nb_error+";");
    //                 parser.txtFile.write(nb_predicted_used+";");
    //                 if (phraseTapee.length()>0) {
    //                     parser.txtFile.write(((float)nb_predicted_used/(phraseTapee.length())) *100+"%;");
    //                 } else {
    //                     parser.txtFile.write(";");
    //                 }
    //                 parser.txtFile.write(phraseTapee.length()/(temps/1000)+";");
    //                 parser.txtFile.write(distance+"");
    //             }
    //         }
    //     } catch (Exception e) {
    //         e.printStackTrace();
    //     }

    //     try {
    //         parser.txtFile.close();
    //     } catch (IOException e) {
    //         e.printStackTrace();
    //     }
    // }

    private void initCSV(File file, String suffixe) {
        try {
			StringBuffer fileName = new StringBuffer();
            String folderName = "Files/logsCSV/";
            fileName.append(folderName);
			fileName.append(file.getName());
            fileName.append("_PARSER_");
            fileName.append(suffixe);
            fileName.append(".csv");
			
			parser.txtFile = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileName.toString()),StandardCharsets.UTF_8));
		} catch (Exception e) {
			e.printStackTrace();
		}
        try {
			parser.txtFile.write("Keyboard;Mode;Participant;Phrase;NbChar;Duree;Correct;NbErrors;NbPredictionUsed;predCharRatio;CharParSec;Distance");
        } catch (IOException e) {
			e.printStackTrace();
		}
    }

    public void readXMLFiles(File[] files) {
        for (File file : files) {
            System.out.println(file.getName());
            readXMLFileMot(file);
            readXMLFilePhrase(file);
        }
    }

    public static void main(String[] args) {
        XMLReader reader = new XMLReader();
        File folder = new File("Files/logs");

        if (folder.isDirectory()) {
            reader.readXMLFiles(folder.listFiles());
        } else {
            System.out.println("Le chemin spécifié n'est pas un dossier.");
        }
    }
}
