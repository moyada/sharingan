package cn.moyada.dubbo.faker.filter.common;

/**
 * 项目信息上下文
 * @author xueyikang
 * @create 2018-03-30 01:53
 */
public class Context {

    private static AppInfo appInfo;

    public static void setAppInfo(Integer appId, String appName) {
        appInfo = new AppInfo();
        appInfo.appId = appId;
        appInfo.appName = appName;
    }

    public static AppInfo getAppInfo() {
        return appInfo;
    }

    public static class AppInfo {

        private Integer appId;

        private String appName;

        public Integer getAppId() {
            return appId;
        }

        public String getAppName() {
            return appName;
        }
    }
}