package util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Logger;

public class PropertiesUtil {
    public static Properties prop = new Properties();
    private static PropertiesUtil INSTANCE = new PropertiesUtil();

    private PropertiesUtil() {
        prop.putAll(load("messages.properties"));
        prop.putAll(load("databases.properties"));
        prop.putAll(load("internal_conf.properties"));
    }

    public static String getString(String name) {
        String result = prop.getProperty(name);
        return result;
    }

    public static String getString(String name, String defaultValue) {
        String result = prop.getProperty(name, defaultValue);
        return result;
    }

    public static PropertiesUtil getInstance() {
        return INSTANCE;
    }

    public static int getInt(String name) {
        String numberStr = getString(name);
        int result = 0;
        if (numberStr != null) {
            try {
                result = Integer.parseInt(numberStr);
            } catch (NumberFormatException e) {
                Logger.getLogger("Cannot parse %s to integer", e.getMessage());
            }
        }
        return result;
    }

    public static int getInt(String name, int defaultValue) {
        String numberStr = getString(name, String.valueOf(defaultValue));
        int result = 0;
        if (numberStr != null) {
            try {
                result = Integer.parseInt(numberStr);
            } catch (NumberFormatException e) {
                Logger.getLogger("Cannot parse %s to integer", e.getMessage());
            }
        }
        return result;
    }

    private Properties load(String file) {
        Properties prop = new Properties();
        try (InputStream input = PropertiesUtil.class.getClassLoader().getResourceAsStream(file)) {
            prop.load(input);
        } catch (IOException e) {
            Logger.getLogger("Cannot open messages.proerties");
            // ignore exception
        }
        return prop;
    }
}