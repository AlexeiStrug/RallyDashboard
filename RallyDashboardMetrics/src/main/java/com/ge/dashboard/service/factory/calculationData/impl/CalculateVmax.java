package com.ge.dashboard.service.factory.calculationData.impl;

import com.ge.dashboard.service.factory.calculationData.CalculateData;

import java.util.Collections;
import java.util.List;

public class CalculateVmax extends CalculateData {

    private static final int FIRST_THREE = 3;

    public CalculateVmax(List<Double> listOfStoryPoints) {
        super(listOfStoryPoints);
    }

    @Override
    public double calculate() {
        Collections.sort(listOfStoryPoints, Collections.reverseOrder());
        double sum = listOfStoryPoints.stream().limit(3).mapToDouble(Double::doubleValue).sum();
        return sum / FIRST_THREE;
    }
}
