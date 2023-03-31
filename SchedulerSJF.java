import java.util.ArrayList;
import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.Queue;

public class SchedulerSJF implements Scheduler {
    private int contextSwitches = 0;
    private ArrayList<Process> processes = new ArrayList<>();
    private Platform platform;
    private Queue<Process> readyQueue = new LinkedList<>();
    private boolean notComplete = true;
    
    public SchedulerSJF(Platform platform) {
        this.platform = platform;
    }

    @Override
    public int getNumberOfContextSwitches() {
        return this.contextSwitches;
    }

    @Override
    public void notifyNewProcess(Process p) {
        processes.add(p);
        
    }

    private void sort(){
        this.processes.sort((o1, o2) -> Integer.valueOf(o1.getBurstTime()).compareTo(Integer.valueOf(o2.getBurstTime())));
        for(Process process : processes){
            this.readyQueue.add(process);
        }
    }

    @Override
    // Fix context switching code
    public Process update(Process cpu) {

        // Ensures Sort only runs once, this way the queue is not updated multiple times
        if(notComplete){
            sort();
            this.notComplete = false;
        }

        Process currentProcess;
        try {
            if (cpu == null) {
                contextSwitches++;
                currentProcess = readyQueue.remove();
                platform.log("Scheduled: " + currentProcess.getName());
            } else {
                if (cpu.isBurstComplete()) {
                    platform.log("Process " + cpu.getName() + " burst complete");
                    if (!cpu.isExecutionComplete()) {
                        readyQueue.add(cpu);
                    }else{
                        contextSwitches++;
                        platform.log("Process " + cpu.getName() + " execution complete");
                    }
                    contextSwitches++;
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
