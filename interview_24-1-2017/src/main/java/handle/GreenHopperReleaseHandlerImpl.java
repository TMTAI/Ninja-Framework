package handle;

import com.google.gson.Gson;
import com.mongodb.client.FindIterable;
import manament.log.LoggerWapper;
import models.SessionInfo;
import ninja.Result;
import org.apache.xmlbeans.XmlException;
import org.bson.Document;
import org.json.JSONObject;
import service.HTTPClientUtil;
import service.XmlReleaseWithUrlService;
import util.Constant;

import java.io.IOException;
import java.util.*;

import static util.Constant.NAME;

/**
 * Created by nnthat on 3/15/17.
 */
public class GreenHopperReleaseHandlerImpl extends GreenHopperReleaseHandler {
    final static LoggerWapper logger = LoggerWapper.getLogger(GreenHopperReleaseHandlerImpl.class);


    @Override
    public Document updateGreenHopperReleaseData(String name, String url, Boolean display) {
        logger.info("Starting Update Greenhopper data cache for URL (" + url + ")");
        HTTPClientUtil HttpClient = HTTPClientUtil.getInstance();
        Document release = getGreenHopperReleaseByName(name);
        String data = null;
        Map<String, Object> results = new HashMap<>();
        try {
            if (release == null) {
                release = new Document(NAME, name).append(Constant.RELEASE_URL, url).append(Constant.RELEASE_DISPLAY, display);
            }
            data = HttpClient.getXmlDataByUrl(release.getString("url"));
            results = XmlReleaseWithUrlService.getDataFromGreenHopper(data);
            JSONObject resultsJson = new JSONObject(results);
            release.append("cacheData", resultsJson.toString()).append(Constant.UPDATE_DATE, new GregorianCalendar(Locale.getDefault()).getTimeInMillis());
            logger.info("Finishing Update Greenhopper data cache for URL (" + url + ")");
            return release;
        } catch (IOException | XmlException e) {
            logger.error(e);
        }
        logger.info("Finishing Update Greenhopper data cache for URL (" + url + ")");
        return release;
    }

    @Override
    public void updateGreenHopperReleaseData(Document release) {
        String url = release.getString("url");
        logger.info("Starting Update Greenhopper data cache for URL (" + url + ")");
        HTTPClientUtil HttpClient = HTTPClientUtil.getInstance();
        release.remove("cacheData");
        try {
            String data = null;
            data = HttpClient.getXmlDataByUrl(url);
            Map<String, Object> results = XmlReleaseWithUrlService.getDataFromGreenHopper(data);
            Gson resultsJson = new Gson();
            String result = resultsJson.toJson(results, LinkedHashMap.class);
            release.append("cacheData", result).append(Constant.UPDATE_DATE, new GregorianCalendar(Locale.getDefault()).getTimeInMillis());
            greenHopperReleaseService.updateRelease(release);
            logger.info("Finishing Update Greenhopper data cache for URL (" + url + ")");
        } catch (IOException | XmlException e) {
            logger.error(e);
        }
    }

    @Override
    public Result getGreenHopperRelease(String id, SessionInfo sessionInfo) {
        return null;
    }

    @Override
    public Document getGreenHopperReleaseByName(String name) {
        Document release = greenHopperReleaseService.findReleaseByName(name);
        return release;
    }

    @Override
    public void updateDataForAllRelease() {
        logger.info("Starting Update Greenhopper data cache for all URL");
        FindIterable<Document> allReleases = greenHopperReleaseService.findAll();
        for (Document temp : allReleases) {
            updateGreenHopperReleaseData(temp);
        }
        logger.info("Finishing Update Greenhopper data cache for all URL");
    }

    @Override
    public void saveColumnConfiguration(Document columnConfiguration) {
        greenHopperReleaseService.saveColumnConfiguration(columnConfiguration);
    }

    @Override
    public Document getColumnConfiguration(String userName) {
        return greenHopperReleaseService.getColumnConfiguration(userName);
    }

}
