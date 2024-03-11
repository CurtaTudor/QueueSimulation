package BusinessLogic;

import java.io.PrintStream;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import GUI.SimulationFrame;
import Model.*;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

public class Scheduler {
    private List<Server> servers;

    private int maxNoServers;
    private int maxTasksPerServer;

    public PrintStream writeToFile;

    public SimulationFrame frame;

    private Strategy strategy;

    public Scheduler(int maxNoServers,int maxTasksPerServer,SelectionPolicy selectionPolicy,PrintStream writeToFile,SimulationFrame frame)
    {
        this.frame=frame;
        this.maxNoServers=maxNoServers;
        this.writeToFile=writeToFile;
        this.maxTasksPerServer=maxTasksPerServer;
        changeStrategy(selectionPolicy);
        this.servers=Collections.synchronizedList(new ArrayList<Server>(maxNoServers));
        for(int i=0;i<maxNoServers;i++){
            Server server=new Server(maxTasksPerServer,i+1,writeToFile,frame);
            servers.add(server);
            Thread serverThread=new Thread(server);
            serverThread.start();
        }
    }

    public boolean isEmpty()
    {
        for(Server s:servers){
            if(!s.getBlockingQueue().isEmpty() || s.getCurentTask().getID()!=0){
                return false;
            }
        }
        return true;
    }

    public void changeStrategy(SelectionPolicy policy)
    {
        if(policy==SelectionPolicy.SHORTEST_TIME){
            this.strategy=new ConcreteStrategyTime();
        }
        if(policy==SelectionPolicy.SHORTEST_QUEUE){
            this.strategy=new ConcreteStrategyQueue();
        }
    }

    public void dispatchTask(Task t) throws InterruptedException {
        strategy.addTask(servers,t);
    }

    public void endQueues()
    {
        for(int i=0;i<maxNoServers;i++){
            servers.get(i).setRunning();
        }
    }
}
