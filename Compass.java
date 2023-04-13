public enum Compass {
    NORTH, EAST, SOUTH, WEST;

    public static Compass getFacing(Point headPos, Point tailPos) {
        if (headPos.getX() == tailPos.getX() && headPos.getY() < tailPos.getY()) {
            return Compass.WEST;
        } else if (headPos.getX() == tailPos.getX() && headPos.getY() > tailPos.getY()) {
            return Compass.EAST;
        } else if (headPos.getY() == tailPos.getY() && headPos.getX() < tailPos.getX()) {
            return Compass.NORTH;
        } else {
            return Compass.SOUTH;
        }
    }
}
