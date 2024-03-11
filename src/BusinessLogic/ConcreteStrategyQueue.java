package BusinessLogic;

import Model.*;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class ConcreteStrategyQueue implements Strategy{
    @Override
    public void addTask(List<Server> servers, Task t) throws InterruptedException {
        int minimQueue=100000;
        for(Server s:servers){
            int curentQueueTime= s.getWaitingTime().get();
            if(curentQueueTime<minimQueue){
                minimQueue=curentQueueTime;
            }
        }
        for(Server s:servers){
            int curentQueueTime=s.getWaitingTime().get();
            if(curentQueueTime==minimQueue){
                s.addTask(t);
                return;
            }
        }
    }
}
