
public enum Mode {
    TRAIN("Train"),
    EXP1("Experience clavier 1"),
    EXP2("Experience clavier 2");

    private String name;

    Mode(String name) {
        this.name = name;
    }
    
    @Override
    public String toString() {
        return name;
    }
}
