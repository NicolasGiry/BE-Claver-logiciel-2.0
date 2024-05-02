
public enum Mode {
    TRAIN("Train"),
    EXP("Experience");

    private String name;

    Mode(String name) {
        this.name = name;
    }
    
    @Override
    public String toString() {
        return name;
    }
}
