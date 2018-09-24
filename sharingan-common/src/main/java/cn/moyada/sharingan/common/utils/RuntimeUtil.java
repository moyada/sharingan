package cn.moyada.sharingan.common.utils;

/**
 * @author xueyikang
 * @create 2018-04-02 22:33
 */
public class RuntimeUtil {

    public final static int MAX_POOL_SIZE;
    static {
        int cpuCore = Runtime.getRuntime().availableProcessors();
        MAX_POOL_SIZE = cpuCore * 2; // % 8 == 0 ? cpuCore : cpuCore + (8 - cpuCore % 8);
    }

    /**
     * 返回线程数，不过超CPU核心数*2
     * @param expectSize
     * @return
     */
    public static int getActualPoolSize(int expectSize) {
        return expectSize > MAX_POOL_SIZE ? MAX_POOL_SIZE : expectSize;
    }

    public static int getMaxPoolSize() {
        return MAX_POOL_SIZE;
    }

    /**
     * 获取当前最大可容量线程数
     * @return
     */
    public static int getAllowThreadSize() {
        Long threadSize = allowThreadSize(1024);
        return threadSize.intValue();
    }

    private static Long allowThreadSize(long size) {
        long memoryBytes = Runtime.getRuntime().freeMemory();
        return (memoryBytes / size);
    }
}
