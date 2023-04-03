public enum ShipType {
    AIRCRAFT_CARRIER(5, "Aircraft Carrier"),
    BATTLESHIP(4, "Battleship"),
    SUBMARINE(3, "Submarine"),
    CRUISER(3, "Cruiser"),
    DESTROYER(2, "Destroyer");

    public final int size;
    public final String name;
    ShipType(int size, String name) {
        this.size = size;
        this.name = name;
    }
}
