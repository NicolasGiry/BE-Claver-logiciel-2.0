import java.util.ArrayList;
import java.util.List;
import java.io.*;

public class Predictor {
    private static final String separation = " ";
    private Tree racine = new Tree("", null);

    // private static String reformateChar(int c) {
    //     // min -> maj
    //     if (c<=122 && c>=97) {
    //         c -= 32;
    //     } 
    //     // remplacer saut de ligne par espace
    //     else if (c==10) {
    //         c=32;
    //     }
    //     // é è ê ë -> E
    //     // else if (c<=235 && c>=232 || c<=203 && c>=200) {
    //     //     c = 69;
    //     // }
    //     else if (c==232 || c==233 || c==224 || c==249) {
    //         c-=32;
    //     } 
    //     else if (c<=235 && c>=232 || c<=203 && c>=200) {
    //         c = 69;
    //     }
    //     // à ... -> A
    //     else if (c<=197 && c>=192 || c<=229 && c>=224) {
    //         c = 65;
    //     }
    //     // ù -> U
    //     else if (c<=220 && c>=218 || c<=252 && c>=249) {
    //         c = 85;
    //     }
    //     // ö -> O
    //     else if (c<=214 && c>=210 || c<=246 && c>=242) {
    //         c = 79;
    //     }
    //     // ï -> I
    //     else if (c<=207 && c>=204 || c<=239 && c>=236) {
    //         c = 73;
    //     }
    //     // ç -> C
    //     else if (c==231) {
    //         c = 199;
    //     } // ne pas prendre en compte les caractères non présents sur le clavier
    //     // else if (c<=191 && c>=123 || c<=96 && c>=91 || c<=64 && c>=33 || c==11 || c==13 || c<=31 && c>=1 || c==215 || c==198 ) {
    //     //     c = 0;
    //     // }
    //     else if (c>90 || (c<65 && c!=32 && c!=39 && c!=44 && c!=33 && c!=63 && c!=46)) {
    //         c=32;
    //     }

    //     return ""+(char) c;
    // }

    private static String reformateChar(int c) {
        // min -> maj
        if ((c<=122 && c>=97) || c==232 || c==233 || c==224 || c==249 || c == 231) {
            c -= 32;
        } 
        // garder les caractères utiles
        else if (c==32 || c==39 || c==44 || c==33 || c==63 || c==46 || (c>=65 && c<=90)) {
            return ""+(char) c;
        }
        // changer les accents pas utilisés en maj
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
        // remplacer caractères pas utilisés par un espace
        else {
            c=32;
        }

        return ""+(char) c;
    }


    private static void createCorpusTXT() {
        try {
            //File wiki = new File("C:/Users/nicol/OneDrive/Documents/Cours/S6/BE/wikipediaFR-TXT.txt/wikipediaTXT.txt");
            //FileReader wikiReader = new FileReader(wiki);
            //BufferedReader wikiBufferedReader = new BufferedReader(wikiReader);
            //File livre = new File("C:/Users/nicol/OneDrive/Documents/Cours/S6/BE/Corpus/proust_a_la_recherche_du_temps_perdu_1_swann.txt");
            File livre = new File("Files/Prediction/Corpus_livre.txt");
            FileReader livreReader = new FileReader(livre);
            BufferedReader livreBufferedReader = new BufferedReader(livreReader);
            File scrabble = new File("Files/Prediction/dico.txt");
            FileReader scrabbleReader = new FileReader(scrabble);
            BufferedReader scrabbleBufferedReader = new BufferedReader(scrabbleReader);
            PrintWriter writer = new PrintWriter("Files/Prediction/corpus.txt");
            try {
                int c;//= wikiBufferedReader.read();
                //for (int i=0; i<10000000 && c!=-1; i++) {
                //    writer.print(reformateChar(c));
                //    c = wikiBufferedReader.read();
                //}
                while((c = livreBufferedReader.read()) != -1)
                {
                    writer.print(reformateChar(c));
                }
                while((c = scrabbleBufferedReader.read()) != -1)
                {
                    writer.print(reformateChar(c));
                }
            } finally {
                //wikiReader.close();
                //wikiBufferedReader.close();
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

    public List<String> updateClavier (List<String> oldClavier, List<String> prediction, int methode) {
        switch (methode) {
            case 1:
                return updateClavier1Letter(oldClavier, prediction);
            case 2:
                return updateClavier1LetterAlways(oldClavier, prediction);
            default:
                //return updateClavierLettersAlways(oldClavier, prediction, methode);
                return prediction;
        }
    }

    // placer la lettre la plus probable en première position si elle n'est pas dans les 6 premières lettres (premier cercle)
    public List<String> updateClavier1Letter (List<String> oldClavier, List<String> prediction) {
        List<String> newClavier = new ArrayList<>();
        List<String> premierCercle = new ArrayList<>();
        String firstLetter = prediction.get(0);

        for (int i=0; i<6 && i<oldClavier.size(); i++) {
            premierCercle.add(oldClavier.get(i));
        }

        if (!premierCercle.contains(firstLetter)) {
            int indice = oldClavier.indexOf(firstLetter);
            String oldLetter = oldClavier.get(0);
            newClavier.add(firstLetter);
            for (int i=1; i<oldClavier.size()-2; i++) {
                if (i==indice) {
                    newClavier.add(oldLetter);
                } else {
                    newClavier.add(oldClavier.get(i));
                }
            }

            // ajouter espace et retour en arrière
            newClavier.add(oldClavier.get(oldClavier.size()-2));
            newClavier.add(oldClavier.get(oldClavier.size()-1));
            return newClavier;
        }
        return oldClavier;
    }

    // placer la lettre la plus probable en première position même si elle est dans le premier cercle
    public List<String> updateClavier1LetterAlways (List<String> oldClavier, List<String> prediction) {
        List<String> newClavier = new ArrayList<>();
        String firstLetter = prediction.get(0);

        int indice = oldClavier.indexOf(firstLetter);
        String oldLetter = oldClavier.get(0);
        newClavier.add(firstLetter);
        for (int i=1; i<oldClavier.size()-2; i++) {
            if (i==indice) {
                newClavier.add(oldLetter);
            } else {
                newClavier.add(oldClavier.get(i));
            }
        }

        // ajouter espace et retour en arrière
        newClavier.add(oldClavier.get(oldClavier.size()-2));
        newClavier.add(oldClavier.get(oldClavier.size()-1));
        return newClavier;
    }

    // placer le nb de lettres voulues les plus probables en premières positions 
    public List<String> updateClavierLettersAlways (List<String> oldClavier, List<String> prediction, int nbLetters) {
        List<String> newClavier = new ArrayList<>();
        int indice = 0, i=0;

        for (i=0; i<nbLetters && i<prediction.size(); i++) {
            newClavier.add(prediction.get(i));
        }

        for (; i<oldClavier.size(); i++) {
            if (newClavier.contains(oldClavier.get(i))) {
                while (newClavier.contains(oldClavier.get(indice))) {
                    indice ++;
                }
                newClavier.add(oldClavier.get(indice));
                indice ++;
            } else {
                newClavier.add(oldClavier.get(i));
            }
        }

        return newClavier;
    }


















    public List<String> updateClavierLettersAlwaysBis (List<String> oldClavier, List<String> prediction, int nbLetters) {
        List<String> newClavier = new ArrayList<>();

        String[] oldLetters = new String[nbLetters];
        List<Integer> indices = new ArrayList<>();
        int i;
        for (i=0; i<nbLetters && i<prediction.size(); i++) {
            oldLetters[i] = oldClavier.get(i);
            indices.add(oldClavier.indexOf(prediction.get(i)));
            newClavier.add(prediction.get(i));
        }
        System.out.println(i);
        for (; i<oldClavier.size()-2; i++) {
            if (indices.contains(i)) {
                newClavier.add(oldLetters[indices.indexOf(i)]);
            } else {
                newClavier.add(oldClavier.get(i));
            }
        }

        // ajouter espace et retour en arrière
        newClavier.add(oldClavier.get(oldClavier.size()-2));
        newClavier.add(oldClavier.get(oldClavier.size()-1));
        return newClavier;
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

        // affichage arbre
        // 
        // racine.printTree(0);
    }
}
