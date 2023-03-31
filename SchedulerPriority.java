import java.util.Comparator;
public class SchedulerPriority extends HelperPriority implements Scheduler {
    private static ProcessComparator compare = new ProcessComparator();
    
    public SchedulerPriority(Platform platform) {
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
            return Integer.compare(p1.getPriority(), p2.getPriority());
        }
    }
}
