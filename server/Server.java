/*
CSci364 - HW3, Server.java
Patrick Dougherty
patrick.r.dougherty@und.edu
26Mar2020
 */
package server;

import api.ClientManager;
import api.Worker;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class Server extends UnicastRemoteObject implements ClientManager {
    private HashMap<String, Integer> clients = new HashMap<String, Integer>();
    private HashMap<String, List<UUID>> typeToID;
    private HashMap<UUID, TaskData> tasks = new HashMap<>();
    private List<String> taskTypes;
    private String host = "localhost";
    private int port = 1099;


    public Server(List<String> list, HashMap<String, List<UUID>> map) throws RemoteException {
        super();
        this.taskTypes = list;
        this.typeToID = map;
    }

    private class TaskData {
        private String taskName;
        private Object taskInput;
        private Object taskOutput;

        public TaskData(String taskName) {
            this.taskName = taskName;
        }

        public void setTaskName(String taskName) {
            this.taskName = taskName;
        }

        public void setTaskInput(Object taskInput) {
            this.taskInput = taskInput;
        }

        public void setTaskOutput(Object taskOutput) {
            this.taskOutput = taskOutput;
        }

        public String getTaskName() {
            return taskName;
        }

        public Object getTaskInput() {
            return taskInput;
        }

        public Object getTaskOutput() {
            return taskOutput;
        }
    }

    public void setPort(int port){
        this.port = port;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort(){
        return port;
    }

    public String getHost(){
        return host;
    }

    @Override
    public List<String> register(String userId) throws RemoteException {
        if(!clients.containsKey(userId)){
            this.clients.put(userId, 0);
            System.out.println("\nNew volunteer registered! Current Volunteer list:");
            for(String user : clients.keySet()){
                System.out.println(user + ": " + clients.get(user));
            }
        }
        return taskTypes;
    }

    @Override
    public Worker requestWork(String userId, String taskName) throws RemoteException {
        if(!clients.containsKey(userId)){
            return null; //Give them nothing. They shouldn't be requesting work without registering!
        }
        UUID taskID = UUID.randomUUID();
        switch (taskName) {
            case "Sort":
                Random rdS = new Random();
                int numSortTerms = Math.abs(rdS.nextInt() % 5)+1;
                Integer[] arrSort = new Integer[numSortTerms];
                for(int i = 0; i < numSortTerms; i++){
                    arrSort[i] = Math.abs(rdS.nextInt() % 1001);
                }
                SortWorker<Integer> sw = new SortWorker<>(taskID, arrSort);
                typeToID.get(taskName).add(taskID);
                List<Integer> numList = new ArrayList<>();
                Collections.addAll(numList, arrSort);
                tasks.put(taskID, new TaskData(taskName));
                tasks.get(taskID).setTaskInput(numList);
                return sw;
            case "Sum Reduce":
                Random rdSR = new Random();
                int numSumTerms = Math.abs(rdSR.nextInt() % 5) + 1;
                int sumType = Math.abs(rdSR.nextInt() % 3);
                switch (sumType){
                    case 0:
                        Integer[] arrIntSum = new Integer[numSumTerms];
                        for(int i = 0; i < numSumTerms; i++){
                            arrIntSum[i] = ThreadLocalRandom.current().nextInt(0,1000);
                        }
                        SumReducer srwInt = new SumReducer(taskID, arrIntSum);
                        typeToID.get(taskName).add(taskID);
                        List<Integer> intList = Arrays.asList(arrIntSum);
                        tasks.put(taskID, new TaskData(taskName));
                        tasks.get(taskID).setTaskInput(intList);
                        return srwInt;
                    case 1:
                        Float[] arrFloatSum = new Float[numSumTerms];
                        for(int i = 0; i < numSumTerms; i++){
                            arrFloatSum[i] = (float)ThreadLocalRandom.current().nextDouble(0,1000);
                        }
                        SumReducer srwFloat = new SumReducer(taskID, arrFloatSum);
                        typeToID.get(taskName).add(taskID);
                        List<Float> floatList = Arrays.asList(arrFloatSum);
                        tasks.put(taskID, new TaskData(taskName));
                        tasks.get(taskID).setTaskInput(floatList);
                        return srwFloat;
                    case 2:
                        Double[] arrDoubleSum = new Double[numSumTerms];
                        for(int i = 0; i < numSumTerms; i++){
                            arrDoubleSum[i] = ThreadLocalRandom.current().nextDouble(0,1000);
                        }
                        SumReducer srwDouble = new SumReducer(taskID, arrDoubleSum);
                        typeToID.get(taskName).add(taskID);
                        List<Double> doubleList = Arrays.asList(arrDoubleSum);
                        tasks.put(taskID, new TaskData(taskName));
                        tasks.get(taskID).setTaskInput(doubleList);
                        return srwDouble;
                    default:
                        return null;
                }
             case "Prime Check":
                 Integer num = Math.abs(ThreadLocalRandom.current().nextInt());
                 PrimeChecker pcw = new PrimeChecker(taskID, num);
                 typeToID.get(taskName).add(taskID);
                 tasks.put(taskID, new TaskData(taskName));
                 tasks.get(taskID).setTaskInput(num);
                 return pcw;

            case "Fraction Reduce":
                int num1 = ThreadLocalRandom.current().nextInt();
                int num2 = ThreadLocalRandom.current().nextInt();
                FractionReducer frw = new FractionReducer(taskID, num1, num2);
                typeToID.get(taskName).add(taskID);
                tasks.put(taskID, new TaskData(taskName));
                List<Integer> input = new ArrayList<>();
                input.add(num1);
                input.add(num2);
                tasks.get(taskID).setTaskInput(input);
                return frw;

            default:
                return null;
        }
    }

    @Override
    public void submitResults(String userId, Worker answer) throws RemoteException {
        String taskType = answer.getTaskName();
        //System.out.println(taskType);
        switch(taskType){
            case "Sort":
                SortWorker<Integer> ans = (SortWorker<Integer>) answer;
                List<Integer> result = ans.getList();
                tasks.get(ans.getTaskId()).setTaskOutput(result);
                break;
            case "Sum Reduce":
                SumReducer ansSum = (SumReducer) answer;
                switch(ansSum.getType()) {
                    case "int":
                        List<Integer> resSumInt = Arrays.asList((Integer) ansSum.getResult());
                        tasks.get(ansSum.getTaskId()).setTaskOutput(resSumInt);
                        break;
                    case "float":
                        List<Float> resSumFloat = Arrays.asList((Float) ansSum.getResult());
                        tasks.get(ansSum.getTaskId()).setTaskOutput(resSumFloat);
                        break;
                    case "double":
                        List<Double> resSumDouble = Arrays.asList((Double) ansSum.getResult());
                        tasks.get(ansSum.getTaskId()).setTaskOutput(resSumDouble);
                        break;
                }

                break;
            case "Prime Check":
                PrimeChecker ansPrime = (PrimeChecker) answer;
                boolean resPrime = ansPrime.getResult();
                tasks.get(ansPrime.getTaskId()).setTaskOutput(resPrime);
                break;

            case "Fraction Reduce":
                FractionReducer ansFrac = (FractionReducer) answer;
                int num1 = ansFrac.getNumerator();
                int num2 = ansFrac.getDenominator();
                List<Integer> output = new ArrayList<>();
                output.add(num1);
                output.add(num2);
                tasks.get(ansFrac.getTaskId()).setTaskOutput(output);
                break;

            default:
                break;
        }
        System.out.println();
        System.out.println(taskType + " task completed. Current results: ID, input, output");
        for(UUID taskID : typeToID.get(taskType)) {
            System.out.println(taskID + ", " +
                    tasks.get(taskID).getTaskInput() + ", " +
                    tasks.get(taskID).getTaskOutput()
            );
        }
        Integer score = clients.get(userId);
        clients.put(userId, score + 1);

    }

    @Override
    public int getScore(String userId) throws RemoteException {
        return clients.get(userId);
    }

    private static List<String> initalizeList() {
        List<String> list = new ArrayList<>();
        list.add("Sort");
        list.add("Sum Reduce");
        list.add("Prime Check");
        list.add("Fraction Reduce");
        return list;
    }

    private static HashMap<String, List<UUID>> initializeMap(List<String> list) {
        HashMap<String, List<UUID>> map = new HashMap<>();
        for(String taskType : list){
            map.put(taskType, new ArrayList<>());
        }
        return map;
    }

    public static void main(String[] args) {
        if (System.getSecurityManager() == null){
            System.setSecurityManager(new SecurityManager());
        }
        try {

            List<String> list = initalizeList();
            Server remoteObject = new Server(list, initializeMap(list));
            switch (args.length) {
                case 2:
                    remoteObject.setPort(Integer.parseInt(args[1]));
                case 1:
                    remoteObject.setHost(args[0]);
                case 0:
                    break;
                default:
                    System.out.println("Usage: Server [RMIhost] [RMIport]");
                    System.exit(0);
            }
            Naming.rebind("rmi://" + remoteObject.getHost() +
                            ":" + remoteObject.getPort() +
                            "/ClientManager", remoteObject
                            );
            System.out.println("Server Ready");
        } catch (Exception e) {
            System.out.println("Server Error: " + e.getMessage());
        }
    }

}
