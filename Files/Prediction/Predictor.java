import java.util.ArrayList;
import java.util.List;
import java.io.*;

public class Predictor {
    private static final String separation = " ";
    private Tree racine = new Tree("", null);

    private static String reformateChar(int c) {
        // min -> maj
        if ((c<=122 && c>=97) || c==232 || c==233 || c==224 || c==249 || c == 231) {
            c -= 32;
        } 
        // garder les caractères utiles (ponctuations et majuscules)
        else if (c==32 || c==39 || c==44 || c==33 || c==63 || c==46 || (c>=65 && c<=90)) {
            return ""+(char) c;
        }
        // changer les accents non pris en charge en maj sans accent
        else if (c>=242 && c<=246 || c>=210 && c<=214) { 
            // O
            c = 79;
        } else if (c>=218 && c<=220 || c>=250 && c<=252 ) { 
            // U
            c = 85;
        } else if (c>= 193 && c<=198 || c>=225 && c<=230 ) {
            // A
            c=65;
        } else if (c>=202 && c<=203 || c>=234 && c<=235) {
            // E
            c = 69;
        } else if (c>=236 && c<=239 || c>=204 && c<=207) {
            // I
            c=73;
        }
        // remplacer caractères non pris en charge par un espace
        else {
            c=32;
        }

        return ""+(char) c;
    }


    private static void createCorpusTXT() {
        try {
            File livre = new File("Files/Prediction/Corpus_livre.txt");
            FileReader livreReader = new FileReader(livre);
            BufferedReader livreBufferedReader = new BufferedReader(livreReader);
            File scrabble = new File("Files/Prediction/dico.txt");
            FileReader scrabbleReader = new FileReader(scrabble);
            BufferedReader scrabbleBufferedReader = new BufferedReader(scrabbleReader);
            PrintWriter writer = new PrintWriter("Files/Prediction/corpus.txt");
            try {
                int c;
                while((c = livreBufferedReader.read()) != -1)
                {
                    writer.print(reformateChar(c));
                }
                while((c = scrabbleBufferedReader.read()) != -1)
                {
                    writer.print(reformateChar(c));
                }
            } finally {
                livreReader.close();
                livreBufferedReader.close();
                scrabbleReader.close();
                scrabbleBufferedReader.close();
                writer.close();
            }
        } catch(IOException e)
        {
          e.printStackTrace();
        }
    }

    private List<String> createCorpus() {
        List<String> corpus = new ArrayList<>();
        try {
            File file = new File("Files/Prediction/corpus.txt");
            FileReader fileReader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            try {
                int c=0;
                while((c = bufferedReader.read()) != -1)
                {
                    corpus.add(reformateChar(c));
                }
            } finally {
                fileReader.close();
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
