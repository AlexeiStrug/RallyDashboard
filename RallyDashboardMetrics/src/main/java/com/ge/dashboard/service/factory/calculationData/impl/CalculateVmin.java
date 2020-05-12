package com.ge.dashboard.service.factory.calculationData.impl;

import com.ge.dashboard.service.factory.calculationData.CalculateData;

import java.util.Collections;
import java.util.List;

public class CalculateVmin extends CalculateData {

    private static final int FIRST_THREE = 3;

    public CalculateVmin(List<Double> listOfStoryPoints) {
        super(listOfStoryPoints);
    }

    @Override
    public double calculate() {
        Collections.sort(listOfStoryPoints);
        double sum = listOfStoryPoints.stream().limit(3).mapToDouble(Double::doubleValue).sum();
        return sum / FIRST_THREE;
    }
}
