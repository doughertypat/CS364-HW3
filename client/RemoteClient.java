/*
CSci364 - HW3, RemoteClient.java
Patrick Dougherty
patrick.r.dougherty@und.edu
26Mar2020
 */

package client;

import api.ClientManager;
import api.Worker;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class RemoteClient {
    private static ClientManager clientManager;
    private static String userID;
    private static String host = "localhost";
    private static Integer port = 1099;

    public static void main(String[] args) throws MalformedURLException, RemoteException, NotBoundException {
        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new SecurityManager());
        }
        switch (args.length) {
            case 3:
                port = Integer.parseInt(args[2]);
            case 2:
                host = args[1];
            case 1:
                userID = args[0];
                break;
            default:
                System.out.println("Usage: RemoteClient userID [RMIhost] [RMIport]");
                System.exit(0);
        }

        try {
             String url = "rmi://" + host + ":" + "/ClientManager";
             clientManager = (ClientManager) Naming.lookup(url);
             List<String> tasks = clientManager.register(userID);
             System.out.println("Available Tasks: " + tasks);
             for(int i=0; i<5; i++){
                 Worker workTask = clientManager.requestWork(userID, tasks.get(ThreadLocalRandom.current().nextInt(0,4)));
                 System.out.println("Task Selected: " + workTask.getTaskName());
                 workTask.doWork();
                 clientManager.submitResults(userID, workTask);
                 System.out.println("Task Submitted. Current Score: " + clientManager.getScore(userID));
             }
             System.out.println("You've completed five tasks! Exiting.");
         } catch (Exception e) {
             e.printStackTrace();
         }

    }


}
