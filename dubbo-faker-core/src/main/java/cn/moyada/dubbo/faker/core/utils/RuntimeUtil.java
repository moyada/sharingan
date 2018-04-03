package cn.moyada.dubbo.faker.core.utils;

/**
 * @author xueyikang
 * @create 2018-04-02 22:33
 */
public class RuntimeUtil {

    public final static int MAX_POOL_SIZE;
    static {
        int cpuCore = Runtime.getRuntime().availableProcessors() * 2;
        MAX_POOL_SIZE = cpuCore % 8 == 0 ? cpuCore : cpuCore + (8 - cpuCore % 8);
    }

    public static int getActualSize(int expectSize) {
        return expectSize > MAX_POOL_SIZE ? MAX_POOL_SIZE : expectSize;
    }

    public static int getAllowThreadSize() {
        Long threadSize = allowThreadSize(1024);
        return threadSize.intValue();
    }

    private static Long allowThreadSize(long size) {
        long memoryBytes = Runtime.getRuntime().freeMemory();
        return (memoryBytes / size);
    }
}
