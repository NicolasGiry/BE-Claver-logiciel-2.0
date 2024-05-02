import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class XMLReader {

    public void readXMLFile(String chemin) {

        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();

            Document document = builder.parse(new File(chemin));
            document.getDocumentElement().normalize();

            NodeList phrasesNodeList = document.getElementsByTagName("Phrase");

            for (int i=0; i<phrasesNodeList.getLength(); i++) {
                Node phraseNode = phrasesNodeList.item(i);

                if (phraseNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element phraseElement = (Element) phraseNode;
                    System.out.println("Phrase : " + phraseElement.getAttribute("string"));
                    System.out.println("\ttemps : " + phraseElement.getAttribute("t"));

                    NodeList attributs = phraseElement.getChildNodes();
                    for (int j=0; j<attributs.getLength(); j++) {
                        Node attribut = attributs.item(j);
                        if (attribut.getNodeType() == Node.ELEMENT_NODE) {
                            Element attributElement = (Element) attribut;
                            System.out.println("\t"+attributElement.getTagName() + ": " + attributElement.getAttribute("name"));
                        }
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        XMLReader reader = new XMLReader();
        reader.readXMLFile("Files/logs/2024_5_2_14_47_1_TRAIN_P1_TROIS_PRED");
    }
}
