package util.gadget;

import manament.log.LoggerWapper;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by nnthat on 3/3/17.
 */
public class UrlUtility {
    final static LoggerWapper logger = LoggerWapper.getLogger(UrlUtility.class);
    private static UrlUtility INSTANCE = new UrlUtility();

    private UrlUtility() {
    }

    public static UrlUtility getInstance() {
        return INSTANCE;
    }

    public static Map<String, String> getParameter(String url) throws UnsupportedEncodingException {
        Map<String, String> results = new HashMap<>();
        String result = URLDecoder.decode(url, "UTF-8");
        result = result.replace("https://greenhopper.app.alcatel-lucent.com/sr/jira.issueviews:searchrequest-xml/temp/SearchRequest.xml?jqlQuery=", "");
        result = result.substring(0, result.indexOf("&"));
        String[] parameterList = result.split("AND");
        for (int i = 0; i < parameterList.length; i++) {
            String[] temp = parameterList[i].split("=");
            results.put(temp[0].replace(" ", ""), temp[1].replace(" ", ""));
        }
        return results;
    }
}
