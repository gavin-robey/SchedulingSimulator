import java.util.Comparator;
import java.util.PriorityQueue;

public class HelperPriority {
    private int contextSwitches = 0;
    protected Platform platform;
    private PriorityQueue<Process> readyQueue;
    
    public HelperPriority(Platform platform, Comparator<Process> compare){
        this.platform = platform;
        this.readyQueue =  new PriorityQueue<>(compare);
    }

    public int getNumberOfContextSwitches() {
        return this.contextSwitches;
    }

    public void notifyNewProcess(Process p) {
        readyQueue.add(p);
    }

    public Process update(Process cpu) {
        Process currentProcess;
        if (cpu == null) {
            currentProcess = readyQueue.poll();
            if (currentProcess != null) platform.log("Scheduled: " + currentProcess.getName());
        } else {
            if(cpu.isExecutionComplete() && cpu.isBurstComplete()){
                platform.log("Process " + cpu.getName() + " burst complete");
                platform.log("Process " + cpu.getName() + " execution complete");
                currentProcess = readyQueue.poll();
                if (currentProcess != null) platform.log("Scheduled: " + currentProcess.getName());
            }else if(cpu.isExecutionComplete() && !cpu.isBurstComplete()){
                platform.log("Process " + cpu.getName() + " execution complete");
                currentProcess = readyQueue.poll();
                if (currentProcess != null) platform.log("Scheduled: " + currentProcess.getName());
            }else if(cpu.isBurstComplete()){
                readyQueue.add(cpu);
                platform.log("Process " + cpu.getName() + " burst complete");
                currentProcess = readyQueue.poll();
                if (currentProcess != null) platform.log("Scheduled: " + currentProcess.getName());
            } else {
                currentProcess = cpu;
            }
        }
        return currentProcess;
    }
}
