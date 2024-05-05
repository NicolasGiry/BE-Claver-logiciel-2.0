package Prediction;

import java.util.ArrayList;
import java.util.List;
import java.io.*;

public class Predictor {
    private static final String separation = " ";
    private Tree racine = new Tree("", null);
    private static String cheminLivre = "Prediction/Corpus_livre.txt";
    private static String cheminDico = "Prediction/dico.txt";
    private static String cheminCorpus = "Prediction/corpus.txt";

    private static String reformateChar(char c) {
        switch (c) {
            case 'é': case 'è': case 'à': case 'ù': return String.valueOf(Character.toUpperCase(c));
            case 'Â': case 'â': case 'ä': case 'Ä': return "A";
            case 'ê': case 'Ê': case 'ë': case 'Ë': return "E";
            case 'î': case 'Î': case 'ï': case 'Ï': return "I";
            case 'ô': case 'Ô': case 'ö': case 'Ö': return "O";
            case 'û': case 'Û': case 'ü': case 'Ü': return "U";
            case '-': case '_': case '/': case '\\': case '(': case ')': case '[': case ']': case '«': case '»': case '{': case '}': 
            case '°': case '"': case ':': case 'º': case '*': case ';': return "";
            default:
                if (Character.isDigit(c)) {
                    return "";
                }
                if (Character.isLowerCase(c)) {
                    return String.valueOf(Character.toUpperCase(c));
                } else {
                    return String.valueOf(c);
                }
        }
    }


    private static void createCorpusTXT() {
        if (System.getProperty("os.name").toLowerCase().contains("windows")) {
            cheminLivre = "Files/" + cheminLivre;
            cheminDico = "Files/" + cheminDico;
            cheminCorpus = "Files/" + cheminCorpus;
        }
        try {
            BufferedReader reader1 = new BufferedReader(new InputStreamReader(new FileInputStream(cheminLivre), "UTF-8"));
            BufferedReader reader2 = new BufferedReader(new InputStreamReader(new FileInputStream(cheminDico), "UTF-8"));
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(cheminCorpus), "UTF-8"));
            try {
                processFile(reader1, writer);
                processFile(reader2, writer);
            } finally {
                reader1.close();
                reader2.close();
                writer.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void processFile(BufferedReader reader, BufferedWriter writer) throws IOException {
        String line;
        while ((line = reader.readLine()) != null) {
            for (int i = 0; i < line.length(); i++) {
                char c = line.charAt(i);
                writer.write(reformateChar(c));
            }
            writer.write(" ");
        }
    }

    private List<String> createCorpus() {
        if (System.getProperty("os.name").toLowerCase().contains("windows")) {
            cheminLivre = "Files/" + cheminLivre;
            cheminDico = "Files/" + cheminDico;
            cheminCorpus = "Files/" + cheminCorpus;
        }
        List<String> corpus = new ArrayList<>();
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(cheminCorpus), "UTF-8"));
            try {
                int c=0;
                while((c = bufferedReader.read()) != -1)
                {
                    char ch = (char) c;
                    corpus.add(String.valueOf(ch));
                }
            } finally {
                bufferedReader.close();
            }
        } catch(IOException e)
        {
          e.printStackTrace();
        }
        return corpus; 
    }

    public void createTree() {
        Tree current;
        List<String> corpus = createCorpus();

        current = racine;
        for (String letter : corpus) {
            if (letter.equals(separation)) {
                current = racine;
            } else if (!letter.equals(((char) 0)+"")) {
                current = current.addLettre(letter, current);
            }
        }
    }

    public Tree getRacine() {
        return racine;
    }

    public static void main(String[] args) {
        String osName = System.getProperty("os.name").toLowerCase();
        if (osName.contains("windows")) {
            cheminLivre = "Files/" + cheminLivre;
            cheminDico = "Files/" + cheminDico;
            cheminCorpus = "Files/" + cheminCorpus;
        }
        Predictor predictor = new Predictor();
        createCorpusTXT();
        predictor.createTree();
        Tree racine = predictor.getRacine();
        List<String> pred = racine.predictNext(true);

        for (String l : pred) {
            System.out.print(l+", ");
        }
    }
}
