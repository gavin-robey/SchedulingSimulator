import java.util.Comparator;
import java.util.PriorityQueue;

public class SchedulerSRTF implements Scheduler {
    private int contextSwitches = 0;
    private Platform platform;
    private PriorityQueue<Process> readyQueue = new PriorityQueue<>(new ProcessComparator());

    public SchedulerSRTF(Platform platform) {
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
            }else if(readyQueue.peek() == null){
                currentProcess = cpu;
            }else if(cpu.getRemainingBurst() >= readyQueue.peek().getRemainingBurst()){
                readyQueue.add(cpu);
                platform.log("Process " + cpu.getName() + " Preemptively removed");
                currentProcess = readyQueue.poll();
                if (currentProcess != null) platform.log("Scheduled: " + currentProcess.getName());
            }else {
                currentProcess = cpu;
            }
        }
        return currentProcess;
    }

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
