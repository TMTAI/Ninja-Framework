package controllers;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import filter.APIFilter;
import filter.SecureFilter;
import handle.GreenHopperReleaseHandler;
import models.ColumnConfigurationVO;
import ninja.Context;
import ninja.FilterWith;
import ninja.Result;
import ninja.Results;
import ninja.params.Param;
import org.apache.log4j.Logger;
import org.bson.Document;
import org.quartz.InterruptableJob;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.SchedulerException;
import org.quartz.impl.StdSchedulerFactory;
import util.Constant;

import java.util.List;

/**
 * Created by nnthat on 3/2/17.
 */
@Singleton
@FilterWith(APIFilter.class)
public class EpgDashboardController {
    final static Logger logger = Logger.getLogger(EpgDashboardController.class);

    @Inject
    GreenHopperReleaseHandler handler;

    @FilterWith(SecureFilter.class)
    public Result initEpgDashboard() {
//        Session session = context.getSession();
//        HTTPClientUtil HttpClient = HTTPClientUtil.getInstance();
//        String url = "https://greenhopper.app.alcatel-lucent.com/sr/jira.issueviews:searchrequest-xml/temp/SearchRequest.xml?" +
//                "jqlQuery=project+%3D+FNMS+AND+issuetype+in+%28Epic%2C+Story%2C+Sub-task%29+AND+fixVersion+%3D+1.2.0&tempMax=10000";
//        String data = HttpClient.getXmlDataByUrl(url, ResultsUtil.getSessionInfo(context).getCookies());
//        JSONObject results = XmlReleaseWithUrlService.getDataFromGreenHopper(data, session);
        return Results.html().status(200);
    }

    @FilterWith(SecureFilter.class)
    public Result getReleaseSummaryDataFromDB(@Param("name") String name) {
        Document results = handler.getGreenHopperReleaseByName(name);
        return Results.json().render(results);
    }

    @FilterWith(SecureFilter.class)
    public Result updateCacheData(@Param("name") String name) {
        StdSchedulerFactory schedFactory = new StdSchedulerFactory();
        try {
            JobDetail job = schedFactory.getScheduler().getJobDetail(new JobKey("UPDATE_GREENHOPPER_DATA_CACHE", "API"));
            ((InterruptableJob) job.getJobClass().newInstance()).interrupt();
            boolean interrupted = schedFactory.getScheduler().interrupt(job.getKey());
            if (interrupted) {
                logger.info("Interrupted Update Greenhopper data cache job");
            }
        } catch (SchedulerException e) {
            logger.error("Cannot interrupt Update Greenhopper data cache job", e);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
        handler.updateDataForAllRelease();
        Document results = handler.getGreenHopperReleaseByName(name);
        return Results.json().render(results);
    }

    @FilterWith(SecureFilter.class)
    public Result getColumnConfiguration(Context context) {
        String username = context.getSession().get(Constant.USERNAME);
        Document results = handler.getColumnConfiguration(username);
        return Results.json().render(results);
    }

    @FilterWith(SecureFilter.class)
    public Result saveColumnConfiguration(Context context, List<ColumnConfigurationVO> columnConfiguration) {
        Document newColumnConfiguration = new Document(Constant.USER, context.getSession().get(Constant.USERNAME))
                .append(Constant.CONFIGURATION, columnConfiguration);
        handler.saveColumnConfiguration(newColumnConfiguration);
        return Results.ok();
    }
}
