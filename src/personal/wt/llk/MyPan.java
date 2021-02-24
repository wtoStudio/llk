package personal.wt.llk;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class MyPan extends JPanel {
    public static final int WIDTH = 480;
    public static final int HEIGHT = 480;

    private static LinkInfo linkInfo;
    //行数
    public static int rowCount = 10;
    //列数
    public static int colCount = 10;
    //小方格边长
    public static int cellLength = 40;

    private int currRow = 0;
    private int currCol = 0;

    //当前第一个选中的
    private Picture firstSelected = null;
    //当前第二个选中的
    private Picture secondSelected = null;

    private Picture pic[][] = new Picture[rowCount + 2][colCount + 2];
    public MyPan(){
        for(int i=0; i<=11; i++){
            for(int j=0; j<=11; j++){
                pic[i][j] = Util.getRandomImage(i, j);
            }
        }
        this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        this.setBackground(new Color(213,103,7));
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int currRow = e.getY()/cellLength;
                int currCol = e.getX()/cellLength;
                //点击的位置在边界之外，不处理
                if(currRow==0 || currRow>10 || currCol==0 || currCol>10){
                    firstSelected = null;
                    secondSelected = null;
                    MyPan.this.repaint();
                    return;
                }
                //点击了已消除的图片，不处理
                if(pic[currRow][currCol].getStatus()==0){
                    firstSelected = null;
                    secondSelected = null;
                    MyPan.this.repaint();
                    return;
                }
                int type = pic[currRow][currCol].getType();
                if(firstSelected == null){
                    firstSelected = new Picture(currRow, currCol, type);
                    MyPan.this.repaint();
                }else if(firstSelected != null && secondSelected == null){
                    //两次点击了同一个位置的图片，不处理
                    if(firstSelected.getRow()==currRow && firstSelected.getCol()==currCol){
                        return;
                    }
                    secondSelected = new Picture(currRow, currCol, type);
                    if(Util.canClean(pic, firstSelected, secondSelected)){
                        final int row1 = firstSelected.getRow();
                        final int col1 = firstSelected.getCol();
                        final int row2 = secondSelected.getRow();
                        final int col2 = secondSelected.getCol();
                        new Thread(()->{
                            int s = 5;
                            int drawLength = 0;
                            int step = 0;
                            int step2 = 0;
                            int step3 = 0;
                            LinkInfo linkInfo = MyPan.linkInfo;
                            int linkType = linkInfo.getType();
                            List<Point> linkPoints = linkInfo.getLinkPoints();
                            List<Line> lines = linkInfo.getLines();
                            int totalLength = Util.calcTotalLength(linkPoints);
                            while (drawLength<=totalLength){
                                if(linkType == LinkInfo.DIRECT){
                                    Point p1 = linkPoints.get(0);
                                    Point p2 = linkPoints.get(1);
                                    linkInfo.setP1(p1);
                                    Point p = new Point();
                                    if(p1.x==p2.x){ //垂直划线
                                        //step = p2.y>p1.y ? step : -step;
                                        p.x = p1.x;
                                        p.y = p1.y + step;
                                        step += (p2.y>p1.y ? 1 : -1) * s;
                                    }else if(p1.y==p2.y){ //水平划线
                                        //step = p2.x>p1.x ? step : -step;
                                        p.x = p1.x + step;
                                        p.y = p1.y;
                                        step += (p2.x>p1.x ? 1 : -1) * s;
                                    }
                                    linkInfo.setP2(p);
                                    drawLength = Util.calcLength(p1, p);
                                    //step += 10;
                                }else if(linkType == LinkInfo.ONE_POINT){
                                    Point p1 = linkPoints.get(0);
                                    Point p2 = linkPoints.get(1);
                                    Point p3 = linkPoints.get(2);
                                    int s1 = Util.calcLength(p1, p2);
                                    int s2 = Util.calcLength(p2, p3);
                                    if(drawLength<=s1){
                                        linkInfo.setP1(p1);
                                        Point p = new Point();
                                        if(p1.x==p2.x){ //垂直划线
                                            p.x = p1.x;
                                            p.y = p1.y + step;
                                            step += (p2.y>p1.y ? 1 : -1) * s;
                                        }else if(p1.y==p2.y){ //水平划线
                                            p.x = p1.x + step;
                                            p.y = p1.y;
                                            step += (p2.x>p1.x ? 1 : -1) * s;
                                        }
                                        linkInfo.setP2(p);
                                        drawLength = Util.calcLength(p1, p);
                                    }else if(drawLength>s1){
                                        linkInfo.getLines().add(new Line(p1, p2));
                                        linkInfo.setP1(p2);
                                        Point p = new Point();
                                        if(p2.x==p3.x){ //垂直划线
                                            p.x = p2.x;
                                            p.y = p2.y + step2;
                                            step2 += (p3.y>p2.y ? 1 : -1) * s;
                                        }else if(p2.y==p3.y){ //水平划线
                                            p.x = p2.x + step2;
                                            p.y = p2.y;
                                            step2 += (p3.x>p2.x ? 1 : -1) * s;
                                        }
                                        linkInfo.setP2(p);
                                        drawLength = s1 + Util.calcLength(p2, p);
                                    }
                                }else if(linkType == LinkInfo.TWO_POINT){
                                    Point p1 = linkPoints.get(0);
                                    Point p2 = linkPoints.get(1);
                                    Point p3 = linkPoints.get(2);
                                    Point p4 = linkPoints.get(3);
                                    int s1 = Util.calcLength(p1, p2);
                                    int s2 = Util.calcLength(p2, p3);
                                    int s3 = Util.calcLength(p3, p4);
                                    if(drawLength<=s1){
                                        linkInfo.setP1(p1);
                                        Point p = new Point();
                                        if(p1.x==p2.x){ //垂直划线
                                            p.x = p1.x;
                                            p.y = p1.y + step;
                                            step += (p2.y>p1.y ? 1 : -1) * s;
                                        }else if(p1.y==p2.y){ //水平划线
                                            p.x = p1.x + step;
                                            p.y = p1.y;
                                            step += (p2.x>p1.x ? 1 : -1) * s;
                                        }
                                        linkInfo.setP2(p);
                                        drawLength = Util.calcLength(p1, p);
                                    }else if(drawLength>s1 && drawLength<=(s1+s2)){
                                        linkInfo.getLines().add(new Line(p1, p2));
                                        linkInfo.setP1(p2);
                                        Point p = new Point();
                                        if(p2.x==p3.x){ //垂直划线
                                            p.x = p2.x;
                                            p.y = p2.y + step2;
                                            step2 += (p3.y>p2.y ? 1 : -1) * s;
                                        }else if(p2.y==p3.y){ //水平划线
                                            p.x = p2.x + step2;
                                            p.y = p2.y;
                                            step2 += (p3.x>p2.x ? 1 : -1) * s;
                                        }
                                        linkInfo.setP2(p);
                                        drawLength = s1 + Util.calcLength(p2, p);
                                    }else if(drawLength>(s1+s2)){
                                        linkInfo.getLines().add(new Line(p2, p3));
                                        linkInfo.setP1(p3);
                                        Point p = new Point();
                                        if(p3.x==p4.x){ //垂直划线
                                            p.x = p3.x;
                                            p.y = p3.y + step3;
                                            step3 += (p4.y>p3.y ? 1 : -1) * s;
                                        }else if(p3.y==p4.y){ //水平划线
                                            p.x = p3.x + step3;
                                            p.y = p3.y;
                                            step3 += (p4.x>p3.x ? 1 : -1) * s;
                                        }
                                        linkInfo.setP2(p);
                                        drawLength = s1 + s2 + Util.calcLength(p3, p);
                                    }
                                }
                                MyPan.this.repaint();
                                try {Thread.sleep(20);} catch (InterruptedException ex) {ex.printStackTrace();}
                            }
                            pic[row1][col1].setStatus(0);
                            pic[row2][col2].setStatus(0);
                            MyPan.linkInfo = null;
                            MyPan.this.repaint();
                        }).start();
                    }
                    firstSelected = null;
                    secondSelected = null;
                }
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        paintGrid(g);
        paintCurrCell(g);
        paintLinkLine(g);
    }

    //绘制网格
    private void paintGrid(Graphics g){
        for(int i=1; i<=rowCount; i++){
            for(int j=1; j<=colCount; j++){
                g.drawRect(j*cellLength, i*cellLength, cellLength, cellLength);
                Picture picture = pic[i][j];
                if(picture.getStatus() == 1){
                    g.drawImage(picture.getImage(), j*cellLength+1, i*cellLength+1, cellLength-2, cellLength-2,null);
                }
            }
        }
    }

    //绘制当前选中的框框
    private void paintCurrCell(Graphics g){
        Graphics2D gg = (Graphics2D)g;
        gg.setStroke(new BasicStroke(3));
        gg.setColor(Color.GREEN);
        if(this.firstSelected != null){
            gg.drawRect(this.firstSelected.getCol()*cellLength, this.firstSelected.getRow()*cellLength, cellLength, cellLength);
        }

        if(this.secondSelected != null){
            gg.drawRect(this.secondSelected.getCol()*cellLength, this.secondSelected.getRow()*cellLength, cellLength, cellLength);
        }
    }

    //绘制连接线
    private void paintLinkLine(Graphics g){
        if(MyPan.linkInfo != null){
            MyPan.linkInfo.getLines().forEach(line->{
                g.drawLine(line.start.x, line.start.y, line.end.x, line.end.y);
            });
            Point p1 = MyPan.linkInfo.getP1();
            Point p2 = MyPan.linkInfo.getP2();
            g.drawLine(p1.x, p1.y, p2.x, p2.y);
        }
    }

    public void setCurrCol(int currCol) {
        this.currCol = currCol;
    }

    public int getCurrCol() {
        return currCol;
    }

    public void setCurrRow(int currRow) {
        this.currRow = currRow;
    }

    public int getCurrRow() {
        return currRow;
    }

    public static LinkInfo getLinkInfo() {
        return linkInfo;
    }

    public static void setLinkInfo(LinkInfo linkInfo) {
        MyPan.linkInfo = linkInfo;
    }
}
