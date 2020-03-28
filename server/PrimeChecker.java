/*
CSci364 - HW3, PrimeChecker.java
Patrick Dougherty
patrick.r.dougherty@und.edu
26Mar2020
 */

package server;

import api.Worker;

import java.util.UUID;

public class PrimeChecker extends Worker {
    private static final long serialVersionUID = -3066936230633060046L;
    private Integer num;
    private boolean prime = true;

    PrimeChecker(UUID id, Integer num){
        super(id, "Prime Check");
        this.num = num;
    }

    @Override
    public void doWork(){
        int half = num/2;
        if(num==0||num==1){
            prime = false;
        }
        else {
            for(int i=2; i<half; i++){
                if(num%i==0){
                    prime = false;
                    break;
                }
            }
        }
    }

    public boolean getResult(){
        return prime;
    }
}
