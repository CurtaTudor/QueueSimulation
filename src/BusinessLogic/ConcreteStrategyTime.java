package BusinessLogic;

import Model.*;
import java.util.*;

public class ConcreteStrategyTime implements Strategy{
    @Override
    public void addTask(List<Server> servers, Task t) throws InterruptedException {
        int minWaitingTime=1000000;
        for(Server s: servers){
            if(s.getWaitingTime().get()<minWaitingTime){
                minWaitingTime=s.getWaitingTime().get();
            }
        }
        for(Server s:servers){
            if(s.getWaitingTime().get()==minWaitingTime){
                s.addTask(t);
                return;
            }
        }
    }
}
