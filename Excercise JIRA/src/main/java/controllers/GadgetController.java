package controllers;

import models.gadget.Gadget;
import models.gadget.OverdueReviewsGadget;
import models.gadget.SonarStatisticsGadget;
import ninja.Result;
import ninja.Results;
import ninja.params.Param;
import ninja.params.SessionParam;
import ninja.session.Session;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import util.JSONUtil;

import java.util.List;

import static service.GadgetService.*;
import static service.OverdueReviewReportService.getReview;
import static service.SonarStatisticGadgetServive.getSonarStatistic;
import static util.Constant.AMS_OVERDUE_REVIEWS_REPORT_GADGET_KEY;
import static util.Constant.AMS_SONAR_STATISTICS_GADGET_KEY;
import static util.Constant.GREENHOPPER_GADGET_KEY;

public class GadgetController {

    public Result getGadgetList() {
        try {
            return Results.text().render(getGadgetListfromDB());
        } catch (Exception e) {
            return Results.internalServerError();
        }
    }

    public Result showGadgets(@Param("id") String dashboardId, Session session) {
        JSONArray sonarStatisticsGadget = new JSONArray();
        JSONArray overdueReviewGadget = new JSONArray();
        JSONArray greenHopperGadgets = new JSONArray();
        JSONObject result = new JSONObject();
        List<Gadget> dashboardGadgets;
        try {
            dashboardGadgets = getDashboardGadgetbyDashboardId(dashboardId);
            if (dashboardGadgets != null) {
                for (Gadget gadget : dashboardGadgets) {
                    Gadget.Type type = gadget.getType();
                    if (Gadget.Type.AMS_SONAR_STATISTICS_GADGET.equals(type)) {
                        sonarStatisticsGadget.put(getSonarStatistic(session, new JSONObject(((SonarStatisticsGadget) gadget).getData()), ((SonarStatisticsGadget) gadget).getId()));
                    } else if (Gadget.Type.AMS_OVERDUE_REVIEWS.equals(type)) {
                        overdueReviewGadget.put(getReview(session, new JSONObject(((OverdueReviewsGadget) gadget).getData()), ((OverdueReviewsGadget) gadget).getId()));
                    }
                }
            }

            result.put(AMS_SONAR_STATISTICS_GADGET_KEY, sonarStatisticsGadget);
            result.put(AMS_OVERDUE_REVIEWS_REPORT_GADGET_KEY, overdueReviewGadget);
        } catch (JSONException | NullPointerException e) {
            JSONObject er = new JSONObject();
            er.put("Err", e);
            e.printStackTrace();
            return Results.text().render(er);
        } catch (Exception e) {
//            logger.error("show_dashboard ", e);
            return Results.internalServerError();
        }
        return Results.text().render(result);
    }

    public Result addNewGadget(@Param("data") String data) {
        System.out.println(data);
        try {
            JSONObject dataObject = new JSONObject(data);
            String id = dataObject.getString("DashboardId");
            String type = dataObject.getString("GadgetType");
            JSONObject Info = dataObject.getJSONObject("Data");

            insertDashboardGadgettoDB(id, type, Info.toString());
            return Results.ok();
        } catch (Exception e) {
            return Results.internalServerError();
        }
    }

    public Result updateGadget(@Param("data") String data) {
        try {
            JSONObject dataObject = new JSONObject(data);
            String GadgetId = dataObject.getString("DashboardGadgetId");
            JSONObject updateData = dataObject.getJSONObject("Data");
            updateDashboardGadgettoDB(GadgetId, updateData.toString());
            return Results.ok();
        } catch (Exception e) {
//            logger.error(e);
            return Results.internalServerError();
        }
    }

    public Result deleteGadget(@SessionParam("username") String username, @Param("GadgetId") String id) {
        try {
//            logger.info("User: " + username + " deleted Gadget - " + id);
            deleteDashboardGadgetfromDB(id);
        } catch (Exception e) {
//            logger.error(e);
            return Results.internalServerError();
        }
        return Results.ok();
    }

    public Result clearCacheGadget(@Param("GadgetId") String id) {
        try {
            clearCacheGadgetfromDB(id);
        } catch (Exception e) {
//            logger.error(e);
            return Results.internalServerError();
        }
        return Results.ok();
    }
}
