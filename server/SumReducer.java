/*
CSci364 - HW3, SumReducer.java
Patrick Dougherty
patrick.r.dougherty@und.edu
26Mar2020
 */

package server;

import api.Worker;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class SumReducer extends Worker {
    private static final long serialVersionUID = 4122110755156464089L;
    private String type;

    private int intSum;
    private float floatSum;
    private double doubleSum;

    private Integer[] intArray;
    private Float[] floatArray;
    private Double[] doubleArray;

    public String getType() {
        return type;
    }

    SumReducer(UUID id, Integer[] arr){
        super(id, "Sum Reduce");
        intArray = arr;
        type = "int";
    }

    SumReducer(UUID id, Float[] arr){
        super(id, "Sum Reduce");
        floatArray = arr;
        type = "float";
    }

    SumReducer(UUID id, Double[] arr){
        super(id, "Sum Reduce");
        doubleArray = arr;
        type = "double";
    }


    @Override
    public void doWork() {
        switch(type){
            case "int":
                intSum = 0;
                for(int num : intArray){
                    intSum += num;
                }
                break;
            case "float":
                floatSum = 0.0f;
                for(float num : floatArray){
                    floatSum += num;
                }
                break;
            case "double":
                doubleSum = 0.0;
                for(double num : doubleArray){
                    doubleSum += num;
                }
        }
    }

    public Object getResult() {
        switch(type){
            case "int":
                return intSum;
            case "float":
                return floatSum;
            case "double":
                return doubleSum;
            default:
                return null;
        }
    }


}
