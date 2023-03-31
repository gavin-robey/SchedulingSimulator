import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.Queue;

public class SchedulerFCFS implements Scheduler {
    private int contextSwitches = 0;
    private Platform platform;
    private Queue<Process> readyQueue = new LinkedList<>();

    public SchedulerFCFS(Platform platform) {
        this.platform = platform;
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
    // Fix context switching code
    public Process update(Process cpu) {
        Process currentProcess;
        try {
            if (cpu == null) {
                currentProcess = readyQueue.remove();
                platform.log("Scheduled: " + currentProcess.getName());
            } else {
                if(cpu.isExecutionComplete() && cpu.isBurstComplete()){
                    platform.log("Process " + cpu.getName() + " burst complete");
                    platform.log("Process " + cpu.getName() + " execution complete");
                    currentProcess = readyQueue.remove();
                    platform.log("Scheduled: " + currentProcess.getName());
                }else if(cpu.isExecutionComplete() && !cpu.isBurstComplete()){
                    platform.log("Process " + cpu.getName() + " execution complete");
                    currentProcess = readyQueue.remove();
                    platform.log("Scheduled: " + currentProcess.getName());
                }else if(cpu.isBurstComplete()){
                    readyQueue.add(cpu);
                    platform.log("Process " + cpu.getName() + " burst complete");
                    currentProcess = readyQueue.remove();
                    platform.log("Scheduled: " + currentProcess.getName());
                } else {
                    currentProcess = cpu;
                }
            }
        } catch (NoSuchElementException e) {
            currentProcess = null;
        }
        return currentProcess;
    }
}
