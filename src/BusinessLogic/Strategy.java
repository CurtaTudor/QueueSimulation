package BusinessLogic;

import java.util.*;
import Model.*;

public interface Strategy {
    public void addTask(List<Server> servers,Task t) throws InterruptedException;
}
