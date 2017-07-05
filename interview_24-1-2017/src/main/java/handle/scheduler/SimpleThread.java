package handle.scheduler;

import handle.GreenHopperReleaseHandler;
import manament.log.LoggerWapper;
import org.bson.Document;

/**
 * Created by nnthat on 3/29/17.
 */
public class SimpleThread implements Runnable {
    final static LoggerWapper logger = LoggerWapper.getLogger(SimpleThread.class);
    private GreenHopperReleaseHandler handler;
    private String name;

    public SimpleThread(GreenHopperReleaseHandler handler, String name) {
        this.handler = handler;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public void run() {
        Document release = handler.getGreenHopperReleaseByName(name);
        handler.updateGreenHopperReleaseData(release);
    }
}
