package personal.wt.llk;

import javax.swing.*;
import java.awt.*;
import java.awt.List;
import java.net.URL;
import java.util.*;
import static java.lang.Math.max;
import static java.lang.Math.min;

@SuppressWarnings("all")
public class Util {
    public static final Map<Integer, Image> imageMap = new HashMap<>();
    static {
        URL url = Util.class.getClassLoader().getResource("");
        Image image1 = new ImageIcon(url.getFile() + "1.png").getImage();
        Image image2 = new ImageIcon(url.getFile() + "2.png").getImage();
        Image image3 = new ImageIcon(url.getFile() + "3.png").getImage();
        Image image4 = new ImageIcon(url.getFile() + "4.png").getImage();
        Image image5 = new ImageIcon(url.getFile() + "5.png").getImage();
        Image image6 = new ImageIcon(url.getFile() + "6.png").getImage();
        Image image7 = new ImageIcon(url.getFile() + "7.png").getImage();
        imageMap.put(1, image1);
        imageMap.put(2, image2);
        imageMap.put(3, image3);
        imageMap.put(4, image4);
        imageMap.put(5, image5);
        imageMap.put(6, image6);
        imageMap.put(7, image7);
    }

    public static Picture getRandomImage(int row, int col){
        if(row==0 || row==11 || col==0 || col==11){
            return new Picture(null, 0, -1, row, col);
        }else{
            Random random = new Random();
            int r = random.nextInt(imageMap.size()) + 1; // 0-4 => 1-5
            return new Picture(imageMap.get(r), 1, r, row, col);
        }
    }

    public static boolean canClean(Picture[][] pic, Picture p1, Picture p2){
        if(p1.getType()!=p2.getType()){
            return false;
        }
        //水平相邻，必然可以消除
        if(p1.getRow()==p2.getRow() && Math.abs(p1.getCol()-p2.getCol())==1){
            LinkInfo linkInfo = new LinkInfo();
            linkInfo.setType(LinkInfo.DIRECT);
            ArrayList<Point> linkPoints = new ArrayList<>(2);
            linkPoints.add(buildLinkPoint(p1.getRow(), p1.getCol()));
            linkPoints.add(buildLinkPoint(p2.getRow(), p2.getCol()));
            if(p1.getCol()>p2.getCol()){
                Collections.reverse(linkPoints);
            }
            linkInfo.setLinkPoints(linkPoints);
            MyPan.setLinkInfo(linkInfo);
            System.out.println("水平相邻消除，连接点：" + linkPoints);
            return true;
        }
        //垂直相邻，必然可以消除
        if(p1.getCol()==p2.getCol() && Math.abs(p1.getRow()-p2.getRow())==1){
            LinkInfo linkInfo = new LinkInfo();
            linkInfo.setType(LinkInfo.DIRECT);
            ArrayList<Point> linkPoints = new ArrayList<>(2);
            linkPoints.add(buildLinkPoint(p1.getRow(), p1.getCol()));
            linkPoints.add(buildLinkPoint(p2.getRow(), p2.getCol()));
            if(p1.getRow()>p2.getRow()){
                Collections.reverse(linkPoints);
            }

            linkInfo.setLinkPoints(linkPoints);
            MyPan.setLinkInfo(linkInfo);
            System.out.println("垂直相邻消除，连接点：" + linkPoints);
            return true;
        }

        //1、在同一行
        if(p1.getRow() == p2.getRow()){
            //边界行上的两个相同图片必然能消除
            /*if(p1.getRow() == 1 || p1.getRow() == 10){
                LinkInfo linkInfo = new LinkInfo();
                linkInfo.setType(LinkInfo.TWO_POINT);
                ArrayList<Point> linkPoints = new ArrayList<>(4);
                int rowOffset = p1.getRow() == 1 ? -1 : 1;
                linkPoints.add(buildLinkPoint(p1.getRow(), p1.getCol()));
                linkPoints.add(buildLinkPoint(p1.getRow()+rowOffset, p1.getCol()));
                linkPoints.add(buildLinkPoint(p2.getRow()+rowOffset, p2.getCol()));
                linkPoints.add(buildLinkPoint(p2.getRow(), p2.getCol()));
                if(p1.getCol()>p2.getCol()){
                    Collections.reverse(linkPoints);
                }
                linkInfo.setLinkPoints(linkPoints);
                MyPan.setLinkInfo(linkInfo);
                System.out.println("同一行,边界行上的两个相同图片必然能消除，连接点：" + linkPoints);
                return true;
            }*/

            //在同一行，但不在边界上（看能不能直接连接消除）
            boolean bool = true;
            int start = min(p1.getCol(), p2.getCol()) + 1;
            int end = max(p1.getCol(), p2.getCol());
            for(int col=start; col<end; col++){
                if(pic[p1.getRow()][col].getStatus() != 0){
                    bool =  false;
                    break;
                }
            }
            //能直接连接消除
            if(bool){
                LinkInfo linkInfo = new LinkInfo();
                linkInfo.setType(LinkInfo.DIRECT);
                ArrayList<Point> linkPoints = new ArrayList<>(2);
                linkPoints.add(buildLinkPoint(p1.getRow(), p1.getCol()));
                linkPoints.add(buildLinkPoint(p2.getRow(), p2.getCol()));
                if(p1.getCol()>p2.getCol()){
                    Collections.reverse(linkPoints);
                }
                linkInfo.setLinkPoints(linkPoints);
                MyPan.setLinkInfo(linkInfo);
                System.out.println("在同一行，但不在边界上，连接点：" + linkPoints);
                return true;
            }
        }

        //2、在同一列
        if(p1.getCol() == p2.getCol()){
            //边界行上的两个相同图片必然能消除
            /*if(p1.getCol() == 1 || p1.getCol() == 10){
                LinkInfo linkInfo = new LinkInfo();
                linkInfo.setType(LinkInfo.TWO_POINT);
                ArrayList<Point> linkPoints = new ArrayList<>(4);
                int colOffset = p1.getCol() == 1 ? -1 : 1;
                linkPoints.add(buildLinkPoint(p1.getRow(), p1.getCol()));
                linkPoints.add(buildLinkPoint(p1.getRow(), p1.getCol()+colOffset));
                linkPoints.add(buildLinkPoint(p2.getRow(), p2.getCol()+colOffset));
                linkPoints.add(buildLinkPoint(p2.getRow(), p2.getCol()));
                if(p1.getRow()>p2.getRow()){
                    Collections.reverse(linkPoints);
                }
                linkInfo.setLinkPoints(linkPoints);
                MyPan.setLinkInfo(linkInfo);
                System.out.println("同一列，边界行上的两个相同图片必然能消除，连接点：" + linkPoints);
                return true;
            }*/

            //在同一列，但不在边界上（看能不能直接连接消除）
            boolean bool = true;
            int start = min(p1.getRow(), p2.getRow()) + 1;
            int end = max(p1.getRow(), p2.getRow());
            for(int row=start; row<end; row++){
                if(pic[row][p1.getCol()].getStatus() != 0){
                    bool =  false;
                    break;
                }
            }
            //能直接连接消除
            if(bool){
                LinkInfo linkInfo = new LinkInfo();
                linkInfo.setType(LinkInfo.DIRECT);
                ArrayList<Point> linkPoints = new ArrayList<>(2);
                linkPoints.add(buildLinkPoint(p1.getRow(), p1.getCol()));
                linkPoints.add(buildLinkPoint(p2.getRow(), p2.getCol()));
                if(p1.getRow()>p2.getRow()){
                    Collections.reverse(linkPoints);
                }
                linkInfo.setLinkPoints(linkPoints);
                MyPan.setLinkInfo(linkInfo);
                System.out.println("在同一列，但不在边界上，连接点：" + linkPoints);
                return true;
            }
        }

        if(isLt2Rb(pic, p1, p2)){
            return true;
        }

        if(isRt2Lb(pic, p1, p2)){
            return true;
        }

        //判断上方的U型折现是否成立
        if(isTopU(pic, p1, p2)){
            return true;
        }
        //判断下方的U型折现是否成立
        if(isBottomU(pic, p1, p2)){
            return true;
        }

        //判断左方的U型折现是否成立
        if(isLeftU(pic, p1, p2)){
            return true;
        }
        //判断右方的U型折现是否成立
        if(isRightU(pic, p1, p2)){
            return true;
        }
        MyPan.setLinkInfo(null);
        return false;
    }

    //判断上方的U型折现是否成立
    private static boolean isTopU(Picture[][] pic, Picture p1, Picture p2){
        LinkInfo linkInfo = new LinkInfo();
        linkInfo.setType(LinkInfo.TWO_POINT);
        ArrayList<Point> linkPoints = new ArrayList<>();
        int left = min(p1.getCol(), p2.getCol());
        int right = max(p1.getCol(), p2.getCol());
        int minRow = min(p1.getRow(), p2.getRow())-1;
        int maxRow = max(p1.getRow(), p2.getRow())-1;

        int c = p1.getRow()>p2.getRow() ? p1.getCol() : p2.getCol();
        for(int i=minRow;i<=maxRow;i++){
            if(pic[i][c].getStatus()!=0){
                return false;
            }
        }
        for(;minRow>=0;minRow--){
            if(pic[minRow][left].getStatus()!=0 || pic[minRow][right].getStatus()!=0 ){
                break;
            }
            for(int curretCol = left;curretCol<=right; curretCol++){
                if(pic[minRow][left].getStatus()!=0){
                    break;
                }
                if(curretCol==right){
                    linkPoints.add(buildLinkPoint(p1.getRow(), p1.getCol()));
                    linkPoints.add(buildLinkPoint(minRow, p1.getCol()));
                    linkPoints.add(buildLinkPoint(minRow, p2.getCol()));
                    linkPoints.add(buildLinkPoint(p2.getRow(), p2.getCol()));
                    if(p1.getCol()>p2.getCol()){
                        Collections.reverse(linkPoints);
                    }
                    linkInfo.setLinkPoints(linkPoints);
                    MyPan.setLinkInfo(linkInfo);
                    System.out.println("上方的U型折线成立,连接点：" + linkPoints);
                    return true;
                }
            }
        }
        return false;
    }

    //判断下方的U型折现是否成立
    private static boolean isBottomU(Picture[][] pic, Picture p1, Picture p2){
        LinkInfo linkInfo = new LinkInfo();
        linkInfo.setType(LinkInfo.TWO_POINT);
        ArrayList<Point> linkPoints = new ArrayList<>();

        int left = min(p1.getCol(), p2.getCol());
        int right = max(p1.getCol(), p2.getCol());
        int minRow = min(p1.getRow(), p2.getRow()) + 1;
        int maxRow = max(p1.getRow(), p2.getRow()) + 1;

        int c = p1.getRow()<p2.getRow() ? p1.getCol() : p2.getCol();
        for(int i=minRow;i<=maxRow;i++){
            if(pic[i][c].getStatus()!=0){
                return false;
            }
        }
        for(;maxRow<=11;maxRow++){
            if(pic[maxRow][left].getStatus()!=0 || pic[maxRow][right].getStatus()!=0 ){
                break;
            }
            for(int curretCol = left;curretCol<=right; curretCol++){
                if(pic[maxRow][curretCol].getStatus()!=0){
                    break;
                }
                if(curretCol==right){
                    linkPoints.add(buildLinkPoint(p1.getRow(), p1.getCol()));
                    linkPoints.add(buildLinkPoint(maxRow, p1.getCol()));
                    linkPoints.add(buildLinkPoint(maxRow, p2.getCol()));
                    linkPoints.add(buildLinkPoint(p2.getRow(), p2.getCol()));
                    if(p1.getCol()>p2.getCol()){
                        Collections.reverse(linkPoints);
                    }
                    linkInfo.setLinkPoints(linkPoints);
                    MyPan.setLinkInfo(linkInfo);
                    System.out.println("下方的U型折线成立，连接点：" + linkPoints);
                    return true;
                }
            }
        }
        return false;
    }

    //判断左方的U型折现是否成立
    private static boolean isLeftU(Picture[][] pic, Picture p1, Picture p2){
        LinkInfo linkInfo = new LinkInfo();
        linkInfo.setType(LinkInfo.TWO_POINT);
        ArrayList<Point> linkPoints = new ArrayList<>();

        int top = min(p1.getRow(), p2.getRow());
        int bottom = max(p1.getRow(), p2.getRow());
        int minCol = min(p1.getCol(), p2.getCol()) - 1;
        int maxCol = max(p1.getCol(), p2.getCol()) - 1;

        int r = p1.getCol() > p2.getCol() ? p1.getRow() : p2.getRow();
        for(int i=minCol; i<=maxCol; i++){
            if(pic[r][i].getStatus()!=0){
                return false;
            }
        }

        for(;minCol>=0;minCol--){
            if(pic[top][minCol].getStatus()!=0 || pic[bottom][minCol].getStatus()!=0 ){
                break;
            }
            for(int currentRow = top;currentRow<=bottom; currentRow++){
                if(pic[currentRow][minCol].getStatus()!=0){
                    break;
                }
                if(currentRow==bottom){
                    linkPoints.add(buildLinkPoint(p1.getRow(), p1.getCol()));
                    linkPoints.add(buildLinkPoint(p1.getRow(), minCol));
                    linkPoints.add(buildLinkPoint(p2.getRow(), minCol));
                    linkPoints.add(buildLinkPoint(p2.getRow(), p2.getCol()));
                    if(p1.getCol()>p2.getCol()){
                        Collections.reverse(linkPoints);
                    }
                    linkInfo.setLinkPoints(linkPoints);
                    MyPan.setLinkInfo(linkInfo);
                    System.out.println("左方的U型折线成立，连接点：" + linkPoints);
                    return true;
                }
            }
        }
        return false;
    }

    //判断右方的U型折现是否成立
    public static boolean isRightU(Picture[][] pic, Picture p1, Picture p2){
        LinkInfo linkInfo = new LinkInfo();
        linkInfo.setType(LinkInfo.TWO_POINT);
        ArrayList<Point> linkPoints = new ArrayList<>();

        int top = min(p1.getRow(), p2.getRow());
        int bottom = max(p1.getRow(), p2.getRow());
        int minCol = min(p1.getCol(), p2.getCol()) + 1;
        int maxCol = max(p1.getCol(), p2.getCol()) + 1;

        int r = p1.getCol()>p2.getCol() ? p2.getRow() : p1.getRow();
        for(int i=minCol; i<=maxCol; i++){
            if(pic[r][i].getStatus()!=0){
                return false;
            }
        }

        for(;maxCol<=11;maxCol++){
            if(pic[top][maxCol].getStatus()!=0 || pic[bottom][maxCol].getStatus()!=0 ){
                break;
            }
            for(int currentRow = top;currentRow<=bottom; currentRow++){
                if(pic[currentRow][maxCol].getStatus()!=0){
                    break;
                }
                if(currentRow==bottom){
                    linkPoints.add(buildLinkPoint(p1.getRow(), p1.getCol()));
                    linkPoints.add(buildLinkPoint(p1.getRow(), maxCol));
                    linkPoints.add(buildLinkPoint(p2.getRow(), maxCol));
                    linkPoints.add(buildLinkPoint(p2.getRow(), p2.getCol()));
                    if(p1.getCol()>p2.getCol()){
                        Collections.reverse(linkPoints);
                    }
                    linkInfo.setLinkPoints(linkPoints);
                    MyPan.setLinkInfo(linkInfo);
                    System.out.println("右方的U型折线成立，连接点：" + linkPoints);
                    return true;
                }
            }
        }
        return false;
    }

    //左上 - 右下
    private static boolean isLt2Rb(Picture[][] pic, Picture p1, Picture p2){
        //左上
        Picture lt = null;
        //右下
        Picture rb = null;
        if(p1.getRow()<p2.getRow() && p1.getCol()<p2.getCol()){
            lt = p1;
            rb = p2;
        }else if(p1.getRow()>p2.getRow() && p1.getCol()>p2.getCol()){
            lt = p2;
            rb = p1;
        }else{
            return false;
        }

        //判断左上方水平方向 与 右下方垂直方向 是否能通过一折连接
        boolean b = true;
        for(int c = lt.getCol()+1; c<=rb.getCol(); c++){
            if(pic[lt.getRow()][c].getStatus()!=0){
                b = false;
                break;
            }
        }
        if(b){
            for(int r=lt.getRow();r<rb.getRow();r++){
                if(pic[r][rb.getCol()].getStatus()!=0){
                    break;
                }
                if(r==rb.getRow()-1){
                    LinkInfo linkInfo = new LinkInfo();
                    linkInfo.setType(LinkInfo.ONE_POINT);
                    ArrayList<Point> linkPoints = new ArrayList<>();
                    linkPoints.add(buildLinkPoint(lt.getRow(), lt.getCol()));
                    linkPoints.add(buildLinkPoint(lt.getRow(), rb.getCol()));
                    linkPoints.add(buildLinkPoint(rb.getRow(), rb.getCol()));
                    linkInfo.setLinkPoints(linkPoints);
                    MyPan.setLinkInfo(linkInfo);
                    System.out.println("左上方水平方向 与 右下方垂直方向，连接点：" + linkPoints);
                    return true;
                }
            }
        }

        //判断左上方垂直方向 与 右下方水平方向 是否能通过一折连接
        boolean f = true;
        for(int r=lt.getRow()+1;r<=rb.getRow();r++){
            if(pic[r][lt.getCol()].getStatus()!=0){
                f = false;
                break;
            }
        }
        if(f){
            for(int c=lt.getCol();c<rb.getCol();c++){
                if(pic[rb.getRow()][c].getStatus()!=0){
                    break;
                }
                if(c==rb.getCol()-1){
                    LinkInfo linkInfo = new LinkInfo();
                    linkInfo.setType(LinkInfo.ONE_POINT);
                    ArrayList<Point> linkPoints = new ArrayList<>();
                    linkPoints.add(buildLinkPoint(lt.getRow(), lt.getCol()));
                    linkPoints.add(buildLinkPoint(rb.getRow(), lt.getCol()));
                    linkPoints.add(buildLinkPoint(rb.getRow(), rb.getCol()));
                    linkInfo.setLinkPoints(linkPoints);
                    MyPan.setLinkInfo(linkInfo);
                    System.out.println("左上方垂直方向 与 右下方水平方向，连接点：" + linkPoints);
                    return true;
                }
            }
        }

        // Z型折现（平行线在水平方向）
        Set<Picture> list1 = new HashSet<>();
        Set<Picture> list2 = new HashSet<>();
        for(int c=lt.getCol()+1;c<rb.getCol();c++){
            if(pic[lt.getRow()][c].getStatus()==0){
                list1.add(pic[lt.getRow()][c]);
            }
        }
        for(int c=rb.getCol()-1;c>lt.getCol();c--){
            if(pic[rb.getRow()][c].getStatus()==0){
                list2.add(pic[rb.getRow()][c]);
            }
        }

        for(Picture p : list1){
            int row = p.getRow();
            int col = p.getCol();
            for(int r=row;r<=rb.getRow();r++){
                if(pic[r][col].getStatus()!=0){
                    continue;
                }
                if(r==rb.getRow() && list2.contains(new Picture(r, col))){
                    LinkInfo linkInfo = new LinkInfo();
                    linkInfo.setType(LinkInfo.TWO_POINT);
                    ArrayList<Point> linkPoints = new ArrayList<>();
                    linkPoints.add(buildLinkPoint(lt.getRow(), lt.getCol()));
                    linkPoints.add(buildLinkPoint(lt.getRow(), col));
                    linkPoints.add(buildLinkPoint(rb.getRow(), col));
                    linkPoints.add(buildLinkPoint(rb.getRow(), rb.getCol()));
                    linkInfo.setLinkPoints(linkPoints);
                    MyPan.setLinkInfo(linkInfo);
                    System.out.println("左上-右下 => 平行线在水平方向（有bug），连接点：" + linkPoints);
                    return true;
                }
            }
        }

        // Z型折现（平行线在垂直方向）
        list1.clear();
        list2.clear();
        for(int r=lt.getRow()+1;r<rb.getRow();r++){
            if(pic[r][lt.getCol()].getStatus()==0){
                list1.add(new Picture(r, lt.getCol()));
            }
        }
        for(int r=rb.getRow()-1;r>lt.getRow();r--){
            if(pic[r][rb.getCol()].getStatus()==0){
                list2.add(new Picture(r, rb.getCol()));
            }
        }
        for(Picture p : list1){
            int row = p.getRow();
            int col = p.getCol();
            for(int c=col;c<=rb.getCol();c++){
                if(pic[row][c].getStatus()!=0){
                    continue;
                }
                if(c==rb.getCol() && list2.contains(new Picture(row, c))){
                    LinkInfo linkInfo = new LinkInfo();
                    linkInfo.setType(LinkInfo.TWO_POINT);
                    ArrayList<Point> linkPoints = new ArrayList<>();
                    linkPoints.add(buildLinkPoint(lt.getRow(), lt.getCol()));
                    linkPoints.add(buildLinkPoint(row, lt.getCol()));
                    linkPoints.add(buildLinkPoint(row, rb.getCol()));
                    linkPoints.add(buildLinkPoint(rb.getRow(), rb.getCol()));
                    linkInfo.setLinkPoints(linkPoints);
                    MyPan.setLinkInfo(linkInfo);
                    System.out.println("左上-右下 => 平行线在垂直方向，连接点：" + linkPoints);
                    return true;
                }
            }
        }
        return false;
    }


    //右上 - 左下
    private static boolean isRt2Lb(Picture[][] pic, Picture p1, Picture p2){
        //右上
        Picture rt = null;
        //左下
        Picture lb = null;
        if(p1.getRow()<p2.getRow() && p1.getCol()>p2.getCol()){
            rt = p1;
            lb = p2;
        }else if(p2.getRow()<p1.getRow() && p2.getCol()>p1.getCol()){
            rt = p2;
            lb = p1;
        }else{
            return false;
        }

        //右上水平方向 和 左下垂直方向
        boolean b = true;
        for(int c=rt.getCol()-1;c>=lb.getCol();c--){
            if(pic[rt.getRow()][c].getStatus()!=0){
                b = false;
                break;
            }
        }
        if(b){
            for(int r=rt.getRow();r<lb.getRow();r++){
                if(pic[r][lb.getCol()].getStatus()!=0){
                    break;
                }
                if(r==lb.getRow()-1){
                    LinkInfo linkInfo = new LinkInfo();
                    linkInfo.setType(LinkInfo.ONE_POINT);
                    ArrayList<Point> linkPoints = new ArrayList<>();
                    linkPoints.add(buildLinkPoint(lb.getRow(), lb.getCol()));
                    linkPoints.add(buildLinkPoint(rt.getRow(), lb.getCol()));
                    linkPoints.add(buildLinkPoint(rt.getRow(), rt.getCol()));
                    linkInfo.setLinkPoints(linkPoints);
                    MyPan.setLinkInfo(linkInfo);
                    System.out.println("右上水平方向 和 左下垂直方向，连接点：" + linkPoints);
                    return true;
                }
            }
        }

        //右上垂直方向 和 左下水平方向
        boolean f = true;
        for(int c=lb.getCol()+1;c<=rt.getCol();c++){
            if(pic[lb.getRow()][c].getStatus()!=0){
                f = false;
                break;
            }
        }
        if(f){
            for(int r=rt.getRow()+1;r<=lb.getRow();r++){
                if(pic[r][rt.getCol()].getStatus()!=0){
                    break;
                }
                if(r==lb.getRow()){
                    LinkInfo linkInfo = new LinkInfo();
                    linkInfo.setType(LinkInfo.ONE_POINT);
                    ArrayList<Point> linkPoints = new ArrayList<>();
                    linkPoints.add(buildLinkPoint(lb.getRow(), lb.getCol()));
                    linkPoints.add(buildLinkPoint(lb.getRow(), rt.getCol()));
                    linkPoints.add(buildLinkPoint(rt.getRow(), rt.getCol()));
                    linkInfo.setLinkPoints(linkPoints);
                    MyPan.setLinkInfo(linkInfo);
                    System.out.println("右上垂直方向 和 左下水平方向，连接点：" + linkPoints);
                    return true;
                }
            }
        }

        // Z型折现（平行线在水平方向）
        Set<Picture> list1 = new HashSet<>();
        Set<Picture> list2 = new HashSet<>();
        for(int c=rt.getCol()-1;c>lb.getCol();c--){
            if(pic[rt.getRow()][c].getStatus()==0){
                list1.add(pic[rt.getRow()][c]);
            }
        }
        for(int c=lb.getCol()+1;c<rt.getCol();c++){
            if(pic[lb.getRow()][c].getStatus()==0){
                list2.add(pic[lb.getRow()][c]);
            }
        }

        for(Picture p : list1){
            int row = p.getRow();
            int col = p.getCol();
            for(int r=row;r<=lb.getRow();r++){
                if(pic[r][col].getStatus()!=0){
                    continue;
                }
                if(r==lb.getRow() && list2.contains(new Picture(r, col))){
                    LinkInfo linkInfo = new LinkInfo();
                    linkInfo.setType(LinkInfo.TWO_POINT);
                    ArrayList<Point> linkPoints = new ArrayList<>();
                    linkPoints.add(buildLinkPoint(lb.getRow(), lb.getCol()));
                    linkPoints.add(buildLinkPoint(lb.getRow(), col));
                    linkPoints.add(buildLinkPoint(rt.getRow(), col));
                    linkPoints.add(buildLinkPoint(rt.getRow(), rt.getCol()));
                    linkInfo.setLinkPoints(linkPoints);
                    MyPan.setLinkInfo(linkInfo);
                    System.out.println("右上-左下 => 平行线在水平方向，连接点：" + linkPoints);
                    return true;
                }
            }
        }

        // Z型折现（平行线在垂直方向）
        list1.clear();
        list2.clear();
        for(int r=rt.getRow()+1;r<lb.getRow();r++){
            if(pic[r][rt.getCol()].getStatus()==0){
                list1.add(new Picture(r, rt.getCol()));
            }
        }
        for(int r=lb.getRow()-1;r>rt.getRow();r--){
            if(pic[r][lb.getCol()].getStatus()==0){
                list2.add(new Picture(r, lb.getCol()));
            }
        }
        for(Picture p : list1){
            int row = p.getRow();
            int col = p.getCol();
            for(int c=col;c>=lb.getCol();c--){
                if(pic[row][c].getStatus()!=0){
                    continue;
                }
                if(c==lb.getCol() && list2.contains(new Picture(row, c))){
                    LinkInfo linkInfo = new LinkInfo();
                    linkInfo.setType(LinkInfo.TWO_POINT);
                    ArrayList<Point> linkPoints = new ArrayList<>();
                    linkPoints.add(buildLinkPoint(lb.getRow(), lb.getCol()));
                    linkPoints.add(buildLinkPoint(row, lb.getCol()));
                    linkPoints.add(buildLinkPoint(row, rt.getCol()));
                    linkPoints.add(buildLinkPoint(rt.getRow(), rt.getCol()));
                    linkInfo.setLinkPoints(linkPoints);
                    MyPan.setLinkInfo(linkInfo);
                    System.out.println("右上-左下 => 平行线在垂直方向，连接点：" + linkPoints);
                    return true;
                }
            }
        }
        return false;
    }

    private static Point buildLinkPoint(int row, int col){
        return new Point(col*MyPan.cellLength + MyPan.cellLength/2, row*MyPan.cellLength + MyPan.cellLength/2);
        //return new Point(row, col);
    }

    public static int calcLength(Point p1, Point p2){
        return (int) Math.sqrt((p1.x-p2.x)*(p1.x-p2.x) + (p1.y-p2.y)*(p1.y-p2.y));
    }

    public static int calcTotalLength(java.util.List<Point> linkPoints){
        int sum = 0;
        for(int i=0; i<linkPoints.size()-1; i++){
            sum += calcLength(linkPoints.get(i), linkPoints.get(i+1));
        }
        return sum;
    }
}
