package Model;

import GUI.SimulationFrame;

import java.io.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class Server implements Runnable{
    private BlockingQueue<Task> tasks;
    private AtomicInteger waitingTime;
    private int nr;
    private Task curentTask;
    private PrintStream writeToFile;
    private AtomicBoolean running;
    private SimulationFrame frame;
    public Server(int maxTasksPerServer,int nr,PrintStream writeToFile,SimulationFrame frame)
    {
        this.frame=frame;
        this.writeToFile=writeToFile;
        this.running=new AtomicBoolean(false);
        this.tasks=new ArrayBlockingQueue<Task>(maxTasksPerServer);
        this.nr=nr;
        this.waitingTime=new AtomicInteger();
        this.curentTask=new Task(0,0,0);
    }

    public void addTask(Task newTask) throws InterruptedException {
        tasks.put(newTask);
        waitingTime.addAndGet(newTask.getServiceTime());
    }

    public void setRunning()
    {
        running.set(false);
    }

    public void run()
    {
        running.set(true);
        while(running.get())
        {
            try {
                if(curentTask.getID()==0 && tasks.isEmpty()){
                    synchronized(writeToFile) {
                        writeToFile.println("Queue " + this.nr + " is closed\n");
                        frame.textArea.append("Queue " + this.nr + " is closed\n\n");
                    }
                    Thread.sleep(1020);
                }else {
                    this.curentTask = tasks.poll(3, TimeUnit.SECONDS);
                    int curentServiceTime = curentTask.getServiceTime();
                    while (curentServiceTime > 0) {
                        synchronized (writeToFile) {
                            if(running.get()) {
                                printQueue(curentTask, curentServiceTime);
                            }
                        }
                        curentServiceTime--;
                        Thread.sleep(1020);
                        waitingTime.decrementAndGet();
                    }
                    this.curentTask = new Task(0, 0, 0);
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public Task[] getTasks()
    {
        Task[] t= new Task[tasks.size()];
        t=tasks.toArray(t);
        return t;
    }
    public AtomicInteger getWaitingTime() { return this.waitingTime; }

    public BlockingQueue<Task> getBlockingQueue() { return this.tasks; }

    public Task getCurentTask() { return this.curentTask; }

    public void printQueue(Task curentTask,int curentServiceTime) {
        if(curentTask.getID()==0 && tasks.isEmpty()){
            return;
        }
        writeToFile.print("Queue " + this.nr + ": (" + curentTask.getID() + "," + curentTask.getArrivalTime() + "," + curentServiceTime + "); ");
        frame.textArea.append("Queue " + this.nr + ": (" + curentTask.getID() + "," + curentTask.getArrivalTime() + "," + curentServiceTime + "); ");
        if(!tasks.isEmpty()) {
            Task[] t = getTasks();
            for (int i = 0; i < tasks.size(); i++) {
                writeToFile.print("(" + t[i].getID() + "," + t[i].getArrivalTime() + "," + t[i].getServiceTime() + "); ");
                frame.textArea.append("(" + t[i].getID() + "," + t[i].getArrivalTime() + "," + t[i].getServiceTime() + "); ");
            }
        }
        writeToFile.println('\n');
        frame.textArea.append("\n\n");
    }
}
