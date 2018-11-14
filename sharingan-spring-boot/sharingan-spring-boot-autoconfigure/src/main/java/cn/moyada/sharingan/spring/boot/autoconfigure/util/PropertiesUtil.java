package cn.moyada.sharingan.spring.boot.autoconfigure.util;

import org.springframework.core.env.*;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * @author xueyikang
 * @since 1.0
 **/
public class PropertiesUtil {

    /**
     * 获取属性 map
     * @param environment
     * @param prefix
     * @return
     */
    public static Map<String, Object> getMap(Environment environment, String prefix) {
        Map<String, Object> map = null;
        int index = prefix.length();

        MutablePropertySources propSrcs = ((AbstractEnvironment) environment).getPropertySources();

        Iterator<PropertySource<?>> iterator = propSrcs.iterator();
        while (iterator.hasNext()) {

            PropertySource<?> propertySource = iterator.next();
            if (propertySource instanceof MapPropertySource) {

                MapPropertySource mapPropertySource = (MapPropertySource) propertySource;
                // 排除系统属性
                if (mapPropertySource.getName().startsWith("system")) {
                    continue;
                }

                Map<String, Object> sourceMap = mapPropertySource.getSource();
                if (sourceMap.isEmpty()) {
                    continue;
                }

                for (Map.Entry<String, Object> entry : sourceMap.entrySet()) {
                    String propName = entry.getKey();
                    // 过滤前缀
                    if (!propName.startsWith(prefix)) {
                        continue;
                    }

                    String param = RegexUtil.getParam(propName.substring(index));
                    if (null == param) {
                        continue;
                    }

                    if (null == map) {
                        map = new HashMap<>();
                    }
                    map.put(param, entry.getValue());
                }
            }
        }

        return map;
    }
}
