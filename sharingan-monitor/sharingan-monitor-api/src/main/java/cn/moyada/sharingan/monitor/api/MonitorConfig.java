package cn.moyada.sharingan.monitor.api;

/**
 * @author xueyikang
 * @since 1.0
 **/
public class MonitorConfig {

    private int intervalTime;

    private int thresholdSize;

    public int getIntervalTime() {
        return intervalTime;
    }

    public void setIntervalTime(int intervalTime) {
        this.intervalTime = intervalTime;
    }

    public int getThresholdSize() {
        return thresholdSize;
    }

    public void setThresholdSize(int thresholdSize) {
        this.thresholdSize = thresholdSize;
    }
}
