package com.ge.dashboard.service.factory.calculationData;

import java.util.List;

public abstract class CalculateData {

    protected List<Double> listOfStoryPoints;

    public CalculateData(List<Double> listOfStoryPoints) {
        this.listOfStoryPoints = listOfStoryPoints;
    }

    public abstract double calculate();
}