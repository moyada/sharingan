package cn.moyada.dubbo.faker.api.utils;

import cn.moyada.dubbo.faker.api.exception.FakerInitException;

import java.io.FileInputStream;
import java.util.Properties;

/**
 * @author xueyikang
 * @create 2018-03-30 10:35
 */
public class PropertyUtil {

    @SuppressWarnings("unchecked")
    public static <T> T getProperty(String name, T defaultValue) {
        String value = System.getProperty(name);
        if(null != value) {
            value = value.trim();
            if(value.length() != 0) {
                return (T) value;
            }
        }

        return defaultValue;
    }

    public static String getPropertyOnFile(String name, String propertiesFile) {
        String appName = System.getProperty(name);
        if(null != appName) {
            appName = appName.trim();
            if(appName.length() != 0) {
                return appName;
            }
        }

        String path = Thread.currentThread().getContextClassLoader().getResource("").getPath() + propertiesFile;
        Properties properties = System.getProperties();
        try {
            properties.load(new FileInputStream(path));
        } catch (Exception e) {
            throw new FakerInitException("找不到对应配置文件: " + propertiesFile);
        }

        appName = properties.getProperty(name);
        if(null != appName) {
            appName = appName.trim();
            if(appName.length() != 0) {
                return appName;
            }
        }
        return null;
    }
}
