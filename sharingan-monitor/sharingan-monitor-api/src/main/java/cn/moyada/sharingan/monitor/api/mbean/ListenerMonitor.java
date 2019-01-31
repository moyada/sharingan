package cn.moyada.sharingan.monitor.api.mbean;

import sun.management.Util;

import javax.management.ObjectName;

/**
 * @author xueyikang
 * @since 1.0
 **/
public class ListenerMonitor implements ListenerMonitorMBean { //}, DynamicMBean {

    private String state = State.STOP;

    private String listenerInfo;

    private long total = 0;

    private String address;

    public ListenerMonitor(String address) {
        this.address = address;
    }

    @Override
    public String printState() {
        return getState();
    }

    @Override
    public String getState() {
        return state;
    }

    @Override
    public void setState(String state) {
        this.state = state;
    }

    @Override
    public String getListener() {
        return listenerInfo;
    }

    @Override
    public void setListener(String info) {
        listenerInfo = info;
    }

    @Override
    public long getTotalRecord() {
        return total;
    }

    @Override
    public void setIncreaseRecord(int size) {
        this.total += size;
    }

    @Override
    public String getRecordAddress() {
        return address;
    }

    @Override
    public void setRecordAddress(String address) throws IllegalAccessException {
        throw new IllegalAccessException("can not change address when already used");
    }

    @Override
    public ObjectName getObjectName() {
        return Util.newObjectName("Sharingan:type=Monitor");
    }
}
