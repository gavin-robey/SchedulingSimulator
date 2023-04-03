package SchedulingAlgorithms;
import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.Queue;

import Driver.Platform;
import Driver.Process;

public class SchedulerRR implements Scheduler {
    private int contextSwitches = 0;
    private Platform platform;
    private Queue<Process> readyQueue = new LinkedList<>();
    private int timeQuantum;
    private int quantumCounter = 1;
    private Process currentProcess;
    private Process cpu;
    
    /**
     * Creates a new instance of the SchedulerRR class.
     *
     * @param platform the platform that the scheduler runs on.
     * @param timeQuantum the fixed time quantum for the round-robin algorithm.
     */
    public SchedulerRR(Platform platform, int i) {
        this.platform = platform;
        this.timeQuantum = i;
    }

    /**
     * Returns the number of context switches that occurred during the scheduling process.
     *
     * @return the number of context switches that occurred during the scheduling process.
     */
    @Override
    public int getNumberOfContextSwitches() {
        return this.contextSwitches;
    }

    /**
     * Adds a new process to the scheduler's ready queue.
     *
     * @param p the process to be added to the scheduler's ready queue.
     */
    @Override
    public void notifyNewProcess(Process p) {
        readyQueue.add(p);
    }

    /**
     * Updates the scheduler's current process, given the current CPU process.
     *
     * @param cpu the current CPU process.
     * @return the next process to be executed by the CPU.
     */
    @Override
    public Process update(Process cpu) {
        this.cpu = cpu;
        try {
            if (cpu == null) {
                currentProcess = readyQueue.remove();
                platform.log("Scheduled: " + currentProcess.getName());
                contextSwitches++;
            } else {
                if(cpu.isExecutionComplete() && quantumCounter == timeQuantum){
                    formatting(true, true);
                }else if(cpu.isExecutionComplete() && quantumCounter != timeQuantum){
                    formatting(false, true);
                }else if(quantumCounter == timeQuantum){
                    formatting(true, false);
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

    /**
     * Formats the output of the scheduler based on whether the current process has completed execution or the time quantum has expired.
     *
     * @param isTimeQuantum a boolean indicating whether the time quantum has expired.
     * @param isExecutionComplete a boolean indicating whether the current process has completed execution.
     */
    private void formatting(boolean isTimeQuantum, boolean isExecutionComplete){
        quantumCounter = 1;
        if (isTimeQuantum && isExecutionComplete) { 
            platform.log("Process " + cpu.getName() + " Time quantum complete");
            platform.log("Process " + cpu.getName() + " execution complete");
        }else if (isExecutionComplete && !isTimeQuantum) {
            platform.log("Process " + cpu.getName() + " execution complete");
        }else if(isTimeQuantum && !isExecutionComplete){
            readyQueue.add(cpu);
            platform.log("Process " + cpu.getName() + " Time quantum complete");
        }

        contextSwitches++;
        currentProcess = readyQueue.remove();

        platform.log("Scheduled: " + currentProcess.getName());
        contextSwitches++;
    }
}
