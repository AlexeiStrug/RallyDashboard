package com.ge.dashboard.service.factory.calculationData;

import com.ge.dashboard.model.enums.TypeOfCalculation;
import com.ge.dashboard.service.factory.calculationData.impl.CalculateVavg;
import com.ge.dashboard.service.factory.calculationData.impl.CalculateVmax;
import com.ge.dashboard.service.factory.calculationData.impl.CalculateVmin;
import com.ge.dashboard.utils.exception.TypeOfRequestProcessingException;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class CalculateDataFactory {

    public static CalculateData calculateData(TypeOfCalculation typeOfCalculation, List<Double> listOfStoryPoints) throws TypeOfRequestProcessingException {
        Optional<TypeOfCalculation> typeOfCalculationOptional = Arrays.stream(TypeOfCalculation.values())
                .filter(value -> value.name().equals(typeOfCalculation.name()))
                .findFirst();
        CalculateData result = null;
        switch (Objects.requireNonNull(typeOfCalculationOptional.orElse(null))) {
            case MAX:
                result = new CalculateVmax(listOfStoryPoints);
                break;
            case AVG:
                result = new CalculateVavg(listOfStoryPoints);
                break;
            case MIN:
                result = new CalculateVmin(listOfStoryPoints);
                break;
            default:
                throw new TypeOfRequestProcessingException("Sorry! Next type of calculation -> " + typeOfCalculation.getName() + " does not supported yet.");
        }
        return result;
    }
}
