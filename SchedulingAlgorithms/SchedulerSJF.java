package SchedulingAlgorithms;
import java.util.Comparator;

import Driver.Platform;
import Driver.Process;

public class SchedulerSJF extends HelperPriority implements Scheduler {
    private static ProcessComparator compare = new ProcessComparator();
    
    public SchedulerSJF(Platform platform) {
        super(platform, compare);
    }

    @Override
    public int getNumberOfContextSwitches() {
        return super.getNumberOfContextSwitches();
    }

    @Override
    public void notifyNewProcess(Process p) {
        super.notifyNewProcess(p);
    }

    @Override
    public Process update(Process cpu) {
        return super.update(cpu);
    }

    private static class ProcessComparator implements Comparator<Process> {

        @Override
        public int compare(Process p1, Process p2) {
            return Integer.compare(p1.getBurstTime(), p2.getBurstTime());
        }
    }
}
