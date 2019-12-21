package xyz.viktuk.greenhouse.utils;

import xyz.viktuk.greenhouse.entity.CultivationParameter;
import xyz.viktuk.greenhouse.entity.Project;
import xyz.viktuk.greenhouse.entity.Unit;

import java.util.List;
import java.util.stream.Collectors;

import static xyz.viktuk.greenhouse.Constants.CYCLE_CULTIVATION_PARAMETERS;

public class CultivationParameterUtils {


    public static CultivationParameter findCultivationParameter(Project project, Unit unit) {
        Long ticks = project.getTickCount();
        List<CultivationParameter> cultivationParameterList = project.getCultivationParameters().stream()
                .filter(cultivationParameter -> cultivationParameter.getUnit() != null
                        && unit.getId().equals(cultivationParameter.getUnit().getId()))
                .collect(Collectors.toList());

        if (CYCLE_CULTIVATION_PARAMETERS) {
            Long sum = cultivationParameterList.stream().map(CultivationParameter::getDuration).reduce(0L, Long::sum);
            if (sum > ticks) {
                ticks %= sum;
            }
        }

        for (CultivationParameter parameter : cultivationParameterList) {
            if (ticks <= parameter.getDuration()) {
                return parameter;
            }
            ticks -= parameter.getDuration();
        }
        return null;
    }
}
