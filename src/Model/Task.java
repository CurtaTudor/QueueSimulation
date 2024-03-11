package Model;

public class Task implements Comparable<Task>{
    private int ID;
    private int arrivalTime;
    private int serviceTime;

    public Task(int ID,int arrivalTime,int serviceTime)
    {
        this.ID=ID;
        this.arrivalTime=arrivalTime;
        this.serviceTime=serviceTime;
    }

    public int compareTo(Task t)
    {
        if(arrivalTime==t.getArrivalTime()){
            return 0;
        }
        if(arrivalTime>t.getArrivalTime()){
            return 1;
        }
        return -1;
    }

    public int getServiceTime() { return this.serviceTime; }
    public int getArrivalTime() { return this.arrivalTime; }
    public int getID() { return this.ID; }
}
