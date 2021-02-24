package personal.wt.llk;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

public class LinkInfo {
    public static final int DIRECT = 0;

    public static final int ONE_POINT = 1;

    public static final int TWO_POINT = 2;

    private int type;

    private List<Point> linkPoints;


    private List<Line> lines = new ArrayList<>();

    private Point p1 = new Point();

    private Point p2 = new Point();

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public List<Point> getLinkPoints() {
        return linkPoints;
    }

    public void setLinkPoints(List<Point> linkPoints) {
        this.linkPoints = linkPoints;
    }

    public List<Line> getLines() {
        return lines;
    }

    public void setLines(List<Line> lines) {
        this.lines = lines;
    }

    public Point getP1() {
        return p1;
    }

    public void setP1(Point p1) {
        this.p1 = p1;
    }

    public Point getP2() {
        return p2;
    }

    public void setP2(Point p2) {
        this.p2 = p2;
    }
}
