public class Ship {
    private Point headPos;
    private Point tailPos;
    private final String name;
    private final int size;
    private Compass facing;

    public Ship(String name, int size) {
        this.headPos = new Point(0, 0);
        this.tailPos = new Point(0, 0);
        this.name = name;
        this.size = size;
    }

    public Point getHeadPos() {
        return headPos;
    }

    public void setHeadPos(Point headPos) {
        this.headPos.setPoint(headPos);
    }

    public Point getTailPos() {
        return tailPos;
    }

    public void setTailPos(Point tailPos) {
        this.tailPos.setPoint(tailPos);
    }

    public String getName() {
        return name;
    }

    public int getSize() {
        return size;
    }
    

    public void setFacing(Compass facing) {
        this.facing = facing;
    }

    public Compass getFacing() {
        return facing;
    }

    @Override
    public String toString() {
        return String.format(
                "Name: %s, Size: %d, HeadPos: %s, TailPos: %s", name, size, headPos, tailPos
        );
    }
}
