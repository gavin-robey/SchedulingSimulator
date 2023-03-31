import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.Queue;

public class SchedulerFCFS implements Scheduler {
    private int contextSwitches = 0;
    private Platform platform;
    private Queue<Process> readyQueue = new LinkedList<>();
    private Process currentProcess;
    private Process cpu;

    /**
     * Constructor for the SchedulerFCFS class.
     * @param platform the platform where the scheduling algorithm will be run
     */
    public SchedulerFCFS(Platform platform) {
        this.platform = platform;
    }

    /**
     * Returns the number of context switches that have occurred.
     * @return the number of context switches
     */
    @Override
    public int getNumberOfContextSwitches() {
        return this.contextSwitches;
    }

    /**
     * Adds a new process to the ready queue.
     * @param p the process to be added to the ready queue
     */
    @Override
    public void notifyNewProcess(Process p) {
        readyQueue.add(p);
    }

    /**
     * Updates the state of the scheduler based on the current CPU state.
     * @param cpu the process currently running on the CPU
     * @return the process that should be scheduled next
     */
    @Override
    public Process update(Process cpu) {
        this.cpu = cpu;
        try {
            if (cpu == null) {
                currentProcess = readyQueue.remove();
                platform.log("Scheduled: " + currentProcess.getName());
                contextSwitches++; // initial loading of process therefore there is only one context switch
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
        } catch (NoSuchElementException e) {
            currentProcess = null;
        }
        return currentProcess;
    }

    /**
     * Formats the output log and performs necessary state updates after a process completes its execution or burst.
     * @param isBurstComplete true if the process has completed its burst
     * @param isExecutionComplete true if the process has completed its execution
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
        currentProcess = readyQueue.remove();

        platform.log("Scheduled: " + currentProcess.getName());
        contextSwitches++;
    }
}
