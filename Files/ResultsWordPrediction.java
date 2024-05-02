
public enum ResultsWordPrediction {
    TROIS("trois_lettres_predites");

    private String name;

    ResultsWordPrediction (String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
