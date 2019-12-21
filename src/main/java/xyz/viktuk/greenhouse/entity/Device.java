package xyz.viktuk.greenhouse.entity;


import lombok.Data;
import xyz.viktuk.greenhouse.enums.DeviceState;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public class Device extends BaseEntity implements Point {
    @Enumerated(value = EnumType.STRING)
    private DeviceState state;

    private Integer x;

    private Integer y;

    @NotNull
    private Integer power;

    @OneToMany(cascade = CascadeType.ALL)
    private List<DeviceBehaviour> behaviours;

    public List<DeviceBehaviour> getBehaviours() {
        if(behaviours == null){
            behaviours = new ArrayList<>();
        }
        return behaviours;
    }
}
