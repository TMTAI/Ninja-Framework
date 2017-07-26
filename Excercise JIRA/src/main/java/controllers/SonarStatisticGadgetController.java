package controllers;

import ninja.Result;
import ninja.Results;
import ninja.params.Param;
import ninja.session.Session;
import org.apache.log4j.Logger;
import org.json.JSONObject;
import util.Constant;

import static service.SonarStatisticGadgetServive.*;

public class SonarStatisticGadgetController {
    final static Logger logger = Logger.getLogger(SonarStatisticGadgetController.class);


//    @FilterWith(SecureFilter.class)
    public Result getIAComponents(Session session, @Param("data") String data) {
        try {
            return Results.text().render(getIAComponentsRespond(session, (new JSONObject(data)).getString(Constant.RELEASE_URL), null));
        } catch (Exception e) {
            logger.error(e);
            return Results.internalServerError();
        }
    }

    public Result getReleaseList() {
        try {
            return Results.text().render(getReleasesFromDB(null));
        } catch (Exception e) {
            logger.error(e);
            return Results.internalServerError();
        }
    }

    public Result getMetricList() {
        try {
            return Results.text().render(getMetricsFromDB());
        } catch (Exception e) {
            logger.error(e);
            return Results.internalServerError();
        }
    }


}
