import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        ResultsWordPrediction wp = ResultsWordPrediction.TROIS_PRED;
        Mode mode = Mode.TRAIN;
        Scanner scanner = new Scanner(System.in);

        int m;
        do {
            System.out.println("Choisir un mode : (1) train (2) experience.");
            m = scanner.nextInt();
            switch (m) {
                case 1:
                    mode = Mode.TRAIN;
                    break;
                case 2:
                    mode = Mode.EXP;
                    break;
                default:
                    System.out.println("Erreur de saisie.");
            }
        } while (m!=1 && m!=2);

        System.out.println("Choisir un nombre de participant :");
        m = scanner.nextInt();
        
        ExpeLogger.debutSimulation(wp, m, mode);
        Clavier2Frame frame = new Clavier2Frame();
    }
}
