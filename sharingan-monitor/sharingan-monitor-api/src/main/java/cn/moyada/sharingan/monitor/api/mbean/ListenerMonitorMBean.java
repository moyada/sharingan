package cn.moyada.sharingan.monitor.api.mbean;

import jdk.Exported;

import java.lang.management.PlatformManagedObject;

/**
 * @author xueyikang
 * @since 1.0
 **/
@Exported
public interface ListenerMonitorMBean extends PlatformManagedObject {

    /**
     * 获取监听器状态
     * @return
     */
    String printState();

    String getState();

    /**
     * 设置监听器状态
     * @param state 状态描述
     */
    void setState(String state);

    /**
     * 获取监听信息
     * @return
     */
    String getListener();

    /**
     * 设置监听信息
     * @param info
     */
    void setListener(String info);

    /**
     * 获取总监听次数
     * @return
     */
    long getTotalRecord();

    /**
     * 递增监听次数
     * @param size
     */
    void setIncreaseRecord(int size);

    /**
     * 获取监听数据位置
     * @return
     */
    String getRecordAddress();

    /**
     * 设置监听数据信息
     * @param address
     */
    void setRecordAddress(String address) throws IllegalAccessException;
}
