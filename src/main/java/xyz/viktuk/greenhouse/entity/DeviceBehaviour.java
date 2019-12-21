package xyz.viktuk.greenhouse.entity;

import lombok.Data;
import xyz.viktuk.greenhouse.enums.DeviceAction;
import xyz.viktuk.greenhouse.enums.UnitAction;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

@Data
@Entity
public class DeviceBehaviour extends BaseEntity{
    @NotNull
    @ManyToOne
    private Unit unit;

    @NotNull
    @Enumerated(EnumType.STRING)
    private UnitAction unitAction;

    @NotNull
    @Enumerated(EnumType.STRING)
    private DeviceAction deviceAction;
}
