package BusinessLogic;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

import GUI.SimulationFrame;
import Model.*;

public class SimulationManager implements Runnable {
    public int timeLimit;
    public int maxServiceTime;
    public int minServiceTime;
    public int maxArrivalTime;
    public int minArrivalTime;
    public int numberOfServers;
    public int numberOfClients;

    public AtomicInteger startOk;

    public SelectionPolicy selectionPolicy=SelectionPolicy.SHORTEST_TIME;

    private Scheduler scheduler;
    private List<Task> generatedTasks;
    private SimulationFrame frame;

    public PrintStream writeToFile;

    public SimulationManager() throws FileNotFoundException {
        File file=new File("log.txt");
        writeToFile=new PrintStream(new FileOutputStream(file,true));
        frame=new SimulationFrame();
        frame.addStartListener(new StartListener());
        frame.setVisible(true);
        this.startOk=new AtomicInteger(0);
        while(startOk.get()==0);

        generateNRandomTasks();
        writeToFile.println("Time: 0");
        frame.textArea.append("Time: 0\n");
        printWaitingClients();

        this.scheduler = new Scheduler(numberOfServers, numberOfClients, selectionPolicy,writeToFile,frame);
    }

    private void generateNRandomTasks()
    {
        this.generatedTasks=Collections.synchronizedList(new ArrayList<Task>());
        for(int i=1;i<=numberOfClients;i++){
            Random rand=new Random();
            int arrivalTime=rand.nextInt(maxArrivalTime-minArrivalTime+1)+minArrivalTime;
            int serviceTime=rand.nextInt(maxServiceTime-minServiceTime+1)+minServiceTime;
            Task t = new Task(i,arrivalTime,serviceTime);
            this.generatedTasks.add(t);
        }
        Collections.sort(generatedTasks);
    }

    public void printWaitingClients()
    {
        synchronized (writeToFile) {
            frame.textArea.append("Waiting Clients: ");
            writeToFile.print("Waiting Clients: ");
            for (Task t : generatedTasks) {
                writeToFile.print("(" + t.getID() + "," + t.getArrivalTime() + "," + t.getServiceTime() + "); ");
                frame.textArea.append("(" + t.getID() + "," + t.getArrivalTime() + "," + t.getServiceTime() + "); ");
            }
            writeToFile.println("\n");
            frame.textArea.append("\n\n");
        }
    }


    @Override
    public void run()
    {
        int curentTime=0;
        while(curentTime<=timeLimit && (!generatedTasks.isEmpty() || !scheduler.isEmpty())) {
            synchronized (writeToFile) {
                if(curentTime!=0) {
                    writeToFile.println("Time: " + curentTime);
                    frame.textArea.append("Time: " + curentTime + "\n");
                }
            }
            Iterator<Task> it = generatedTasks.iterator();
            while (it.hasNext()) {
                Task curentTask = it.next();
                if (curentTask.getArrivalTime() == curentTime) {
                    try {
                        scheduler.dispatchTask(curentTask);
                        it.remove();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
            if (!generatedTasks.isEmpty() && curentTime!=0) {
                printWaitingClients();
            }
            curentTime++;
            try {
                Thread.sleep(1000);
                frame.textArea.setText("");
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        scheduler.endQueues();
        frame.textArea.append("Simulation ended");
    }

    class StartListener implements ActionListener {
        public void actionPerformed(ActionEvent e)
        {
            try {
                timeLimit=Integer.parseInt(frame.getSimulationTimeInput());
                numberOfClients=Integer.parseInt(frame.getNoOfClients());
                numberOfServers=Integer.parseInt(frame.getNoOfServers());
                minArrivalTime=Integer.parseInt(frame.getMinArrivalTimeInput());
                maxArrivalTime=Integer.parseInt(frame.getMaxArrivalTimeInput());
                minServiceTime=Integer.parseInt(frame.getMinServiceTimeInput());
                maxServiceTime=Integer.parseInt(frame.getMaxServiceTimeInput());
                startOk.set(1);
            }catch(NumberFormatException ex){
                frame.showError(ex.getMessage());
            }
        }
    }


    public static void main(String[] args) throws FileNotFoundException {
        SimulationManager gen=new SimulationManager();
        Thread t = new Thread(gen);
        t.start();
    }

}
