package cn.moyada.sharingan.instrument.boost;

/**
 * @author xueyikang
 * @since 1.0
 **/
class NameUtil {

    static String getProxyName(String className) {
        int index = className.lastIndexOf('.');
        String simpleName = -1 == index ? className : className.substring(index + 1);
        if (!simpleName.contains("$")) {
            return className + "$Proxy";
        }
        return className + "_Proxy";
    }

    static String getSetFunction(String simpleName) {
        return "set" + toUpperCapitalName(simpleName);
    }

    static String genPrivateName(String simpleName) {
        return "_" + toLowerCapitalName(simpleName);
    }

    static String toLowerCapitalName(String simpleName) {
        char c = simpleName.charAt(0);
        c = Character.toLowerCase(c);
        return c + simpleName.substring(1);
    }

    static String toUpperCapitalName(String simpleName) {
        char c = simpleName.charAt(0);
        c = Character.toUpperCase(c);
        return c + simpleName.substring(1);
    }
}
