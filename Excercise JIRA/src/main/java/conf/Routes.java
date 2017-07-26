
package conf;


import controllers.*;
import ninja.AssetsController;
import ninja.Router;
import ninja.application.ApplicationRoutes;

public class Routes implements ApplicationRoutes {

    @Override
    public void init(Router router) {  
        
        router.GET().route("/").with(ApplicationController::index);

        router.GET().route("/login").with(LoginLogoutController::login);
        router.POST().route("/login").with(LoginLogoutController::loginPost);
        router.POST().route("/loginCru").with(LoginLogoutController::loginCru);
        router.GET().route("/logout").with(LoginLogoutController::logout);
        router.POST().route("/clearSession").with(LoginLogoutController::clearSession);



        //User - Project
        router.POST().route("/getUserInfo").with(ApplicationController::getUserInfo);
        router.POST().route("/getProjectList").with(ApplicationController::getProjectList);
        router.POST().route("/getGadgetList").with(GadgetController::getGadgetList);


        //Dashboard
        router.GET().route("/dashboard/new").with(DashboardController::new_dashboard);
        router.POST().route("/dashboard/new").with(DashboardController::new_dashboard_post);
        router.POST().route("/deleteDashboard").with(DashboardController::deleteDashboard);
        router.POST().route("/getDashboardInfo").with(DashboardController::getDashboardInfo);
        router.POST().route("/getDashboardList").with(DashboardController::getDashboardList);
        router.POST().route("/updateDashboardOption").with(DashboardController::updateDashboardOption);
        router.POST().route("/deleteAllDashboard").with(DashboardController::deleteAllDashboard);

        //

        router.POST().route("/addNewGadget").with(GadgetController::addNewGadget);
        router.POST().route("/updateGadget").with(GadgetController::updateGadget);
        router.POST().route("/clearCacheGadget").with(GadgetController::clearCacheGadget);
        router.POST().route("/deleteGadget").with(GadgetController::deleteGadget);
        router.POST().route("/showGadgets").with(GadgetController::showGadgets);

        //

        router.GET().route("/configuration").with(ConfigurationController::configuration);
        router.POST().route("/addNewRelease").with(ConfigurationController::addNewRelease);
        router.POST().route("/deleteRelease").with(ConfigurationController::deleteRelease);
        router.POST().route("/updateRelease").with(ConfigurationController::updateRelease);
        router.POST().route("/getPeriodList").with(ConfigurationController::getPeriodList);
        router.POST().route("/setPeriod").with(ConfigurationController::setPeriod);

        router.POST().route("/getIAComponents").with(SonarStatisticGadgetController::getIAComponents);
        router.POST().route("/getMetrics").with(SonarStatisticGadgetController::getMetricList);
        router.POST().route("/getReleaseList").with(SonarStatisticGadgetController::getReleaseList);

//        router.GET().route("/listproject").with(MyGadgetController::getProjectList");

        router.POST().route("/getCruProjectList").with(OverdueReviewReportController::getCruProjectList);

        ///////////////////////////////////////////////////////////////////////
        // Assets (pictures / javascript)
        ///////////////////////////////////////////////////////////////////////    
        router.GET().route("/assets/webjars/{fileName: .*}").with(AssetsController::serveWebJars);
        router.GET().route("/assets/{fileName: .*}").with(AssetsController::serveStatic);
        ///////////////////////////////////////////////////////////////////////
        // Index / Catchall shows index page
        ///////////////////////////////////////////////////////////////////////
        router.GET().route("/.*").with(ApplicationController::index);
    }

}
