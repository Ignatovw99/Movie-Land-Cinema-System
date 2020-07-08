package movieland.domain.entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import movieland.domain.entities.base.BaseEntity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Entity
@Table(name = "seats")
public class Seat extends BaseEntity {

    private Integer row;

    private Integer column;

    private boolean isFree;

    private boolean isBlocked;

    private BigDecimal price;

    @JsonManagedReference
    private Projection projection;

    public Seat() {
        isFree = true;
        isBlocked = false;
    }

    @Column(name = "hall_row", nullable = false)
    @NotNull
    public Integer getRow() {
        return row;
    }

    public void setRow(Integer row) {
        this.row = row;
    }

    public Seat row(Integer row) {
        this.row = row;
        return this;
    }

    @Column(name = "hall_column", nullable = false)
    public Integer getColumn() {
        return column;
    }

    public void setColumn(Integer column) {
        this.column = column;
    }

    public Seat column(Integer column) {
        this.column = column;
        return this;
    }

    @Column(name = "is_free")
    public boolean getIsFree() {
        return isFree;
    }

    public void setIsFree(boolean free) {
        isFree = free;
    }

    public Seat isFree(boolean isFree) {
        this.isFree = isFree;
        return this;
    }

    @Column(name = "is_blocked")
    public boolean getIsBlocked() {
        return isBlocked;
    }

    public void setIsBlocked(boolean blocked) {
        isBlocked = blocked;
    }

    public Seat isBlocked(boolean isBlocked) {
        this.isBlocked = isBlocked;
        return this;
    }

    @Column(name = "price")
    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Seat price(BigDecimal price) {
        this.price = price;
        return this;
    }

    @ManyToOne(targetEntity = Projection.class)
    @JoinColumn(name = "projection_id", referencedColumnName = "id", nullable = false)
    public Projection getProjection() {
        return projection;
    }

    public void setProjection(Projection projection) {
        this.projection = projection;
    }

    public Seat projection(Projection projection) {
        this.projection = projection;
        return this;
    }
}
