package xyz.viktuk.greenhouse.entity;

import lombok.Data;
import xyz.viktuk.greenhouse.entity.sensors.ActiveSensor;
import xyz.viktuk.greenhouse.entity.sensors.PassiveSensor;
import xyz.viktuk.greenhouse.enums.SimulationState;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.List;

@Entity
@Data
public class Project extends BaseEntity {
    @OneToMany(cascade = CascadeType.ALL)
    private List<Device> devices;

    @OneToMany(cascade = CascadeType.ALL)
    private List<ActiveSensor> activeSensors;

    @OneToMany(cascade = CascadeType.ALL)
    private List<PassiveSensor> passiveSensors;

    @OneToMany(cascade = CascadeType.ALL)
    private List<Unit> units;

    @OneToMany(cascade = CascadeType.ALL)
    private List<CultivationParameter> cultivationParameters;

    private SimulationState simulationState;

    private Long tickCount;
}
