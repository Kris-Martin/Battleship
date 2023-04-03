public enum CellType {
    OCEAN('~'),
    SHIP('O'),
    HIT('X'),
    MISS('M');
    public final char value;

    CellType(char value){
        this.value = value;
    }
}
