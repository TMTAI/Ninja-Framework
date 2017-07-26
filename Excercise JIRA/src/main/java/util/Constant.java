package util;

/**
 * Created by tmtai on 7/14/2017.
 */
public class Constant {
    //database
    public static final String DATABASE_SCHEMA = "DATABASE_SCHEMA";
    public static final String DATABASE_HOST = "DATABASE_HOST";
    public static final String DATABASE_PORT = "DATABASE_PORT";
    public static final String DASHBOARD_ID = "dashboardId";
    public static final String DASHBOARD_GADGET_COLECCTION = "DashboardGadget";

    public static String MONGODB_ID = "_id";
    public static String MONGODB_SET = "$set";

    //
    public static final String LOGIN_LINK = "http://tiger.in.alcatel-lucent.com:8091/login.jsp";
    public static final String LINK_CRUCIBLE = "http://tiger.in.alcatel-lucent.com:8060";
    public static final String LINK_GET_CRU_PROJECTS = "http://tiger.in.alcatel-lucent.com:8060/json/cru/projectFinder.do?limit=99999&q=";
    public static final String LINK_GET_CRU_USERS = "http://tiger.in.alcatel-lucent.com:8060/json/fe/activeUserFinder.do?limit=99999&q=";
    public static final String LINK_GET_ODREVIEW_REPORTS = "http://tiger.in.alcatel-lucent.com:8060/rest-service/reviews-v1/filter/details?creator=&project=%s&states=Review";

    public static final String LINK_GET_JIRA_PERIODS = "http://bamboo.in.alcatel-lucent.com:8085/api/properties?format=json";
    public static final String LINK_GET_SONAR_STATISTIC = "http://bamboo.in.alcatel-lucent.com:8085/api/resources?format=json&metrics=%s&includetrends=true&resource=%s";
    public static final String LINK_GET_JIRA_USER_INFO = "http://tiger.in.alcatel-lucent.com:8091/rest/api/2/user?username=%s&expand=groups";
    public static final String LINK_GET_JIRA_PROJECTS = "http://tiger.in.alcatel-lucent.com:8091/rest/api/2/project";
    public static final String LINK_GET_JIRA_GROUPS = "http://tiger.in.alcatel-lucent.com:8091/rest/api/2/groups/picker?maxResults=10000";
    public static final String LINK_GET_JIRA_USERS_OF_GROUP = "http://tiger.in.alcatel-lucent.com:8091/rest/api/2/group?groupname=%s&expand=users";
    public static final String RESOURCE_BUNLE_HOST = "resourcebundle.host";
    public static final String RESOURCE_BUNLE_PATH = "resourcebundle.execution.path";
    public static final String RESOURCE_BUNLE_HOST_TYPE = "resourcebundle.host.type";
    public static final String RESOURCE_BUNLE_PROXY_IP = "resourcebundle.proxy.ip";
    public static final String RESOURCE_BUNLE_PROXY_PORT = "resourcebundle.proxy.port";
    public static final String RESOURCE_BUNLE_PROJECT_PATH = "resourcebundle.project.path";
    public static final String RESOURCE_BUNLE_LOGIN_PATH = "resourcebundle.login.path";
    public static final String RESOURCE_BUNLE_SEARCH_PATH = "resourcebundle.search.path";
    public static final String RESOURCE_BUNLE_SEARCH_MAXRECORDS = "resourcebundle.search.maxrecords";
    public static final String RESOURCE_BUNLE_SEARCH_MAXRECORDS_DEFAULT = "10000";
    public static final String RESOURCE_BUNLE_ISSUE_PATH = "resourcebundle.issue.path";
}
