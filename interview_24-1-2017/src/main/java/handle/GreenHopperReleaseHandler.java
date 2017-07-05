package handle;

import models.SessionInfo;
import ninja.Context;
import ninja.Result;
import org.bson.Document;

/**
 * Created by nnthat on 3/15/17.
 */
public abstract class GreenHopperReleaseHandler extends Handler {
    public abstract Document updateGreenHopperReleaseData(String data, String url, Boolean display);

    public abstract void updateGreenHopperReleaseData(Document release);

    public abstract Result getGreenHopperRelease(String id, SessionInfo sessionInfo);

    public abstract Document getGreenHopperReleaseByName(String name);

    public abstract void updateDataForAllRelease();

    public abstract void saveColumnConfiguration(Document columnConfiguration);

    public abstract Document getColumnConfiguration(String userName);
}
