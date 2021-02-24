package personal.wt.llk;

import java.awt.*;
import java.util.Objects;

public class Picture {
    private Image image;
    private int status;
    private int type;

    private int row;
    private int col;

    public Picture(){}

    public Picture(Image image, int status, int type, int row, int col){
        this(row, col, type);
        this.image = image;
        this.status = status;
    }

    public Picture(int row, int col, int type){
        this(row, col);
        this.type = type;
    }

    public Picture(int row, int col){
        this.row = row;
        this.col = col;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getCol() {
        return col;
    }

    public void setCol(int col) {
        this.col = col;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Picture picture = (Picture) o;
        return row == picture.row &&
                col == picture.col;
    }

    @Override
    public int hashCode() {
        return Objects.hash(row, col);
    }

    @Override
    public String toString() {
        return "Picture{" +
                "status=" + status +
                ", type=" + type +
                ", row=" + row +
                ", col=" + col +
                '}';
    }
}
