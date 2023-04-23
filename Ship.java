import java.util.HashMap;
import java.util.Map;

public class Ship {
    private final String name;
    private final int size;
    private final Point headPos;
    private final Point tailPos;
    private Compass facing;
    private final Map<String, Boolean> cells;

    public Ship(String name, int size) {
        this.headPos = new Point(0, 0);
        this.tailPos = new Point(0, 0);
        this.name = name;
        this.size = size;
        this.cells = new HashMap<>();
    }

    public void setPosition(Point headPos, Point tailPos) {
        this.setHeadPos(headPos);
        this.setTailPos(tailPos);
        this.setFacing(Compass.getFacing(headPos, tailPos));
    }

    private void setHeadPos(Point headPos) {
        this.headPos.setPoint(headPos);
    }

    private void setTailPos(Point tailPos) {
        this.tailPos.setPoint(tailPos);
    }

    private void setFacing(Compass facing) {
        this.facing = facing;
    }

    public void addCell(String pos, Boolean isShot) {
        this.cells.put(pos, isShot);
    }

    public Boolean isSunk() {
        return this.cells.values().stream().allMatch(isShot -> isShot);
    }

    public Map<String, Boolean> getCells() {
        return this.cells;
    }

    public String getName() {
        return name;
    }

    public int getSize() {
        return size;
    }

    public Point getHeadPos() {
        return headPos;
    }

    public Point getTailPos() {
        return tailPos;
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
