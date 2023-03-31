public class SchedulerRR implements Scheduler {
    public SchedulerRR(Platform platform, int i) {
    }

    @Override
    public int getNumberOfContextSwitches() {
        return 0;
    }

    @Override
    public void notifyNewProcess(Process p) {

    }

    @Override
    public Process update(Process cpu) {
        return null;
    }
}
