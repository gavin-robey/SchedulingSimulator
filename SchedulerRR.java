import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.Queue;

public class SchedulerRR implements Scheduler {
    private int contextSwitches = 0;
    private Platform platform;
    private Queue<Process> readyQueue = new LinkedList<>();
    private int timeQuantum;
    private int quantumCounter = 1;
    
    public SchedulerRR(Platform platform, int i) {
        this.platform = platform;
        this.timeQuantum = i;
    }

    @Override
    public int getNumberOfContextSwitches() {
        return this.contextSwitches;
    }

    @Override
    public void notifyNewProcess(Process p) {
        readyQueue.add(p);
    }

    @Override
    public Process update(Process cpu) {
        Process currentProcess;
        try {
            if (cpu == null) {
                currentProcess = readyQueue.remove();
                platform.log("Scheduled: " + currentProcess.getName());
            } else {
                if(cpu.isExecutionComplete() && quantumCounter == timeQuantum){
                    quantumCounter = 1;
                    platform.log("Process " + cpu.getName() + " Time quantum complete");
                    platform.log("Process " + cpu.getName() + " execution complete");
                    currentProcess = readyQueue.remove();
                    platform.log("Scheduled: " + currentProcess.getName());
                }else if(cpu.isExecutionComplete() && quantumCounter != timeQuantum){
                    quantumCounter = 1;
                    platform.log("Process " + cpu.getName() + " execution complete");
                    currentProcess = readyQueue.remove();
                    platform.log("Scheduled: " + currentProcess.getName());
                }else if(quantumCounter == timeQuantum){
                    quantumCounter = 1;
                    readyQueue.add(cpu);
                    platform.log("Process " + cpu.getName() + " Time quantum complete");
                    currentProcess = readyQueue.remove();
                    platform.log("Scheduled: " + currentProcess.getName());
                } else {
                    quantumCounter++;
                    currentProcess = cpu;
                }
            }
        } catch (NoSuchElementException e) {
            currentProcess = null;
        }

        return currentProcess;
    }
}
