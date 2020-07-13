package movieland.domain.models.service;

import java.math.BigDecimal;

public class SeatServiceModel implements Comparable<SeatServiceModel> {

    private String id;

    private Integer row;

    private Integer column;

    private boolean isFree;

    private boolean isBlocked;

    private BigDecimal price;

    public SeatServiceModel() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getRow() {
        return row;
    }

    public void setRow(Integer row) {
        this.row = row;
    }

    public Integer getColumn() {
        return column;
    }

    public void setColumn(Integer column) {
        this.column = column;
    }

    public boolean isFree() {
        return isFree;
    }

    public void setFree(boolean free) {
        isFree = free;
    }

    public boolean isBlocked() {
        return isBlocked;
    }

    public void setBlocked(boolean blocked) {
        isBlocked = blocked;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    @Override
    public int compareTo(SeatServiceModel o) {
        int compare = Integer.compare(row, o.row);
        if (compare == 0) {
            return Integer.compare(column, o.column);
        }
        return compare;
    }
}
