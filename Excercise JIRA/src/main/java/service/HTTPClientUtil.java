package service;

import org.apache.log4j.Logger;
import util.Constant;
import util.PropertiesUtil;

import java.net.InetSocketAddress;
import java.net.Proxy;

/**
 * Created by tmtai on 7/17/2017.
 */
public class HTTPClientUtil {
    private static HTTPClientUtil instance;
    private Logger logger;
    private HTTPClientUtil(){

        logger = Logger.getLogger(HTTPClientUtil.class);
    }

    public static HTTPClientUtil getInstance(){
        if (instance == null){
            instance = new HTTPClientUtil();
        }
        return instance;
    }

    public Proxy getProxy(){
        String proxyIP = PropertiesUtil.getString(Constant.RESOURCE_BUNLE_PROXY_IP);
        String proxyPortStr = PropertiesUtil.getString(Constant.RESOURCE_BUNLE_PROXY_PORT);

        int proxyPort;

        if (proxyIP == null || proxyPortStr == null) {
            return null;
        }
        try {
            proxyPort = Integer.parseInt(proxyPortStr);
        } catch (NumberFormatException e) {
            logger.info("Incorrect proxy port address");
            return null;
        }
        Proxy proxy = new Proxy(java.net.Proxy.Type.HTTP, new InetSocketAddress(proxyIP, proxyPort));
        return proxy;
    }
}
