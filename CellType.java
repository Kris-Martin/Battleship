public enum CellType {
    OCEAN('~'),
    SHIP_CELL('O'),
    HIT('X'),
    MISS('M');
    public final char status;

    CellType(char status){
        this.status = status;
    }
}
