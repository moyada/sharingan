package cn.moyada.sharingan.common.utils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 线程工具类
 * @author xueyikang
 * @create 2018-04-03 22:44
 */
public class ThreadUtil {

    private static Map<String, Integer> threadIdMap = new ConcurrentHashMap<>();

    /**
     * 获取名称
     * @return
     */
    public static String getName() {
        return Thread.currentThread().getName();
    }

    /**
     * 获取线程名id
     * @return
     */
    public static int getInnerGroupId() {
        String tname = getName();
        Integer index = threadIdMap.get(tname);
        if(null != index) {
            return index;
        }
        index = tname.lastIndexOf('-');
        if(-1 == index) {
            return -1;
        }
        index = Integer.valueOf(tname.substring(index + 1));
        threadIdMap.put(tname, index);
        return index;
    }

    public static void clear() {
        threadIdMap.clear();
    }

    public static void main(String[] args) {

        String tname = "pool-test-thread-9";
        int index = tname.lastIndexOf('-');
        System.out.println(Integer.valueOf(tname.substring(index + 1)));
    }
}
