package handle.scheduler;

import handle.GreenHopperReleaseHandlerImpl;
import manament.log.LoggerWapper;
import org.quartz.InterruptableJob;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.UnableToInterruptJobException;

/**
 * Created by nnthat on 3/15/17.
 */
public class GreenHopperReleaseDataJob implements InterruptableJob {
    final static LoggerWapper logger = LoggerWapper.getLogger(GreenHopperReleaseDataJob.class);
    private GreenHopperReleaseHandlerImpl handler = new GreenHopperReleaseHandlerImpl();
    private Thread currentThread;

    @Override
    public void interrupt() throws UnableToInterruptJobException {
        if (currentThread != null) {
            currentThread.interrupt();
        }
    }

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        currentThread = Thread.currentThread();
        try {
            handler.updateDataForAllRelease();
        } finally {
            currentThread = null;
        }
    }
}
