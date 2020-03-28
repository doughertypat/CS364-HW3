/*
CSci364 - HW3, FractionReducer.java
Patrick Dougherty
patrick.r.dougherty@und.edu
26Mar2020
 */

package server;

import api.Worker;

import java.util.List;
import java.util.UUID;

public class FractionReducer extends Worker {
    private static final long serialVersionUID = 5498121800003522503L;
    private int num1;
    private int num2;
    private int gcd;
    private int res1;
    private int res2;

    FractionReducer(UUID id, Integer num1, Integer num2){
        super(id, "Fraction Reduce");
        this.num1 = num1;
        this.num2 = num2;
    }

    @Override
    public void doWork(){
        int x = num1;
        int y = num2;
        gcd = calcGCD(x,y);
        res1 = num1/gcd;
        res2 = num2/gcd;
    }

    private static int calcGCD(int x, int y){
        if(y == 0){
            return x;
        }
        return calcGCD(y, x%y);
    }

    public int getNumerator(){
        return res1;
    }

    public int getDenominator(){
        return res2;
    }
}
