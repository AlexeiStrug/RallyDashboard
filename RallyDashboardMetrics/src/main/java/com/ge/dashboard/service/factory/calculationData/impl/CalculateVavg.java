package com.ge.dashboard.service.factory.calculationData.impl;

import com.ge.dashboard.service.factory.calculationData.CalculateData;

import java.util.List;

public class CalculateVavg extends CalculateData {

    public CalculateVavg(List<Double> listOfStoryPoints) {
        super(listOfStoryPoints);
    }

    @Override
    public double calculate() {
        double vavg = listOfStoryPoints.stream().mapToDouble(Double::doubleValue).sum();
        return Math.floor((vavg / listOfStoryPoints.size()) * 10) / 10;
    }
}
