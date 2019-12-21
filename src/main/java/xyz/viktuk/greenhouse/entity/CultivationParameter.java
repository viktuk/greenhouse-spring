package xyz.viktuk.greenhouse.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

@Data
@Entity
public class CultivationParameter extends BaseEntity{
    @NotNull
    @ManyToOne
    private Unit unit;

    @NotNull
    private Long duration;

    @NotNull
    private Integer expectedValue;
}
