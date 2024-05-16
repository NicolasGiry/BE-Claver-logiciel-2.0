
public enum ResultsWordPrediction {
    PRED("clavier avec prediction"),
    NO_PRED("clavier sans prediction");

    private String name;

    ResultsWordPrediction (String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
