package xyz.viktuk.greenhouse.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.validation.constraints.NotNull;

@Entity
@Data
public class Unit extends BaseEntity {
    private String parameterName;

    @NotNull
    private Integer defaultValue;

    @Lob
    private UnitEnvironment environment;
}
