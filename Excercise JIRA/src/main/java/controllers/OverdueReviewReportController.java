package controllers;

import ninja.Result;
import ninja.Results;
import ninja.session.Session;

import static service.OverdueReviewReportService.getCruProjectfromServer;

public class OverdueReviewReportController {
    public Result getCruProjectList(Session session) {
        try {
            return Results.text().render(getCruProjectfromServer(session));
        } catch (Exception e) {
//            logger.error(e);
            return Results.internalServerError();
        }
    }
}
