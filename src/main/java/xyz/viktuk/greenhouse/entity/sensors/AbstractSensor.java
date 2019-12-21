package xyz.viktuk.greenhouse.entity.sensors;

import lombok.Data;
import xyz.viktuk.greenhouse.entity.BaseEntity;
import xyz.viktuk.greenhouse.entity.Point;
import xyz.viktuk.greenhouse.entity.Unit;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Data
@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class AbstractSensor extends BaseEntity implements Point {
    @NotNull
    @ManyToOne
    private Unit unit;

    private Integer x;

    private Integer y;

    private Integer value;


}
