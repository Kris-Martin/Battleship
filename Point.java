public class Point {
    int x;
    int y;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void setPoint(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void setPoint(Point point) {
        this.x = point.x;
        this.y = point.y;
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public static int lengthBetweenPoints(Point a, Point b) {
        return Math.abs(a.x - b.x + a.y - b.y) + 1;
    }

    @Override
    public String toString() {
        return "(%s, %s)".formatted(this.x, this.y);
    }
}
