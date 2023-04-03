package SchedulingAlgorithms;
import java.util.Comparator;
import java.util.PriorityQueue;

import Driver.Platform;
import Driver.Process;

public class HelperPriority {
    private int contextSwitches = 0;
    protected Platform platform;
    private PriorityQueue<Process> readyQueue;
    private Process currentProcess;
    private Process cpu;
    
    /**
     * Constructs a new HelperPriority object.
     * @param platform the platform on which the scheduling algorithm is running
     * @param compare the comparator to be used to prioritize the processes in the ready queue
     */
    public HelperPriority(Platform platform, Comparator<Process> compare){
        this.platform = platform;
        this.readyQueue =  new PriorityQueue<>(compare);
    }

    /**
     * Gets the number of context switches that occurred.
     * @return the number of context switches that occurred
     */
    public int getNumberOfContextSwitches() {
        return this.contextSwitches;
    }

    /**
     * Notifies the helper that a new process has been created.
     * @param p the process that has been created
     */
    public void notifyNewProcess(Process p) {
        readyQueue.add(p);
    }

    /**
     * Updates the state of the helper based on the given CPU.
     * @param cpu the CPU that is executing the process
     * @return the process that is currently executing on the CPU
     */
    public Process update(Process cpu) {
        this.cpu = cpu;
        if (cpu == null) {
            currentProcess = readyQueue.poll();
            if (currentProcess != null) platform.log("Scheduled: " + currentProcess.getName());
            contextSwitches++;
        } else {
            if(cpu.isExecutionComplete() && cpu.isBurstComplete()){
                formatting(true, true);
            }else if(cpu.isExecutionComplete() && !cpu.isBurstComplete()){
                formatting(false, true);
            }else if(cpu.isBurstComplete()){
                formatting(true, false);
            } else {
                currentProcess = cpu;
            }
        }
        return currentProcess;
    }


    /**
     * Formats the log messages for a process that has finished executing.
     * @param isBurstComplete true if the burst is complete, false otherwise
     * @param isExecutionComplete true if the execution is complete, false otherwise
     */
    private void formatting(boolean isBurstComplete, boolean isExecutionComplete){
        if (isBurstComplete && isExecutionComplete) { 
            platform.log("Process " + cpu.getName() + " burst complete");
            platform.log("Process " + cpu.getName() + " execution complete");
        }else if (isExecutionComplete && !isBurstComplete) {
            platform.log("Process " + cpu.getName() + " execution complete");
        }else if(isBurstComplete && !isExecutionComplete){
            readyQueue.add(cpu);
            platform.log("Process " + cpu.getName() + " burst complete");
        }
        
        contextSwitches++;
        currentProcess = readyQueue.poll();

        if (currentProcess != null) {
            platform.log("Scheduled: " + currentProcess.getName());
            contextSwitches++;
        }
    }
}
