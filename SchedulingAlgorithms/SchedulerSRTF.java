package SchedulingAlgorithms;
import java.util.Comparator;
import java.util.PriorityQueue;

import Driver.Platform;
import Driver.Process;

public class SchedulerSRTF implements Scheduler {
    private int contextSwitches = 0;
    private Platform platform;
    private PriorityQueue<Process> readyQueue = new PriorityQueue<>(new ProcessComparator());
    private Process currentProcess;
    private Process cpu;

    /**
     * Constructs a new SchedulerSRTF object with a given platform.
     *
     * @param platform the platform to be used
     */
    public SchedulerSRTF(Platform platform) {
        this.platform = platform;
    }

    /**
     * Gets the number of context switches that have occurred.
     *
     * @return the number of context switches
     */
    @Override
    public int getNumberOfContextSwitches() {
        return this.contextSwitches;
    }

    /**
     * Notifies the scheduler of a new process that has been created.
     *
     * @param p the new process to be added to the ready queue
     */
    @Override
    public void notifyNewProcess(Process p) {
        readyQueue.add(p);
    }

    /**
     * Updates the state of the scheduler and returns the next process to be executed.
     *
     * @param cpu the process that was just executing
     * @return the next process to be executed
     */
    @Override
    public Process update(Process cpu) {
        this.cpu = cpu;
        if (cpu == null) {
            currentProcess = readyQueue.poll();
            if (currentProcess != null) {
                platform.log("Scheduled: " + currentProcess.getName());
                contextSwitches++;
            }
        } else {
            if(cpu.isExecutionComplete() && cpu.isBurstComplete()){
                formatting(true, true, false);
            }else if(cpu.isExecutionComplete() && !cpu.isBurstComplete()){
                formatting(false, true, false);
            }else if(cpu.isBurstComplete()){
                formatting(true, false, false);
            }else if(readyQueue.peek() == null){
                currentProcess = cpu;
            }else if(cpu.getRemainingBurst() >= readyQueue.peek().getRemainingBurst()){
                formatting(false, false, true);   
            }else {
                currentProcess = cpu; // Sets the current process to the process passed in
            }
        }

        return currentProcess;
    }

    /**
     * Formats the output and updates the ready queue according to the current process.
     *
     * @param isBurstComplete true if the current process has completed its burst, false otherwise
     * @param isExecutionComplete true if the current process has completed its execution, false otherwise
     * @param needsRemoval true if the current process needs to be removed preemptively from the CPU, false otherwise
     */
    private void formatting(boolean isBurstComplete, boolean isExecutionComplete, boolean needsRemoval){
        if (isBurstComplete && isExecutionComplete) { 
            platform.log("Process " + cpu.getName() + " burst complete");
            platform.log("Process " + cpu.getName() + " execution complete");
        }else if (isExecutionComplete && !isBurstComplete) {
            platform.log("Process " + cpu.getName() + " execution complete");
        }else if(isBurstComplete && !isExecutionComplete){
            readyQueue.add(cpu); // Adds process back since process execution is not complete
            platform.log("Process " + cpu.getName() + " burst complete");
        }else if(needsRemoval){
            readyQueue.add(cpu); // Adds process back since process execution is not complete
            platform.log("Process " + cpu.getName() + " Preemptively removed");
        }
        
        contextSwitches++;
        currentProcess = readyQueue.poll();

        if (currentProcess != null) {
            platform.log("Scheduled: " + currentProcess.getName());
            contextSwitches++;
        }
    }

    /**
     * Compares two processes by their remaining burst time
     */
    class ProcessComparator implements Comparator<Process> {

        @Override
        public int compare(Process p1, Process p2) {
            int cmp = Integer.compare(p1.getRemainingBurst(), p2.getRemainingBurst());
            if (cmp == 0) {
                // If the remaining burst times are equal, compare the arrival times
                cmp = Integer.compare(p1.getStartTime(), p2.getStartTime());
            }
            return cmp;
        }
    }
}
