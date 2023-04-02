package battleship;

public class Ship {
    Point headPos;
    Point tailPos;
    String name;
    int size;

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

    public void setName(String name) {
        this.name = name;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }
}
