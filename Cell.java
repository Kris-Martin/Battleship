public enum Cell {
    OCEAN('~'),
    SHIP('O'),
    HIT('X'),
    MISS('M');
    public final char value;

    Cell(char value){
        this.value = value;
    }
}
