package cn.moyada.sharingan.core.common;

import cn.moyada.sharingan.common.utils.StringUtil;

import java.io.Serializable;

/**
 * 调用信息
 * @author xueyikang
 * @create 2018-04-05 15:17
 */
public class QuestInfo implements Serializable {

    private static final long serialVersionUID = -8508032991680018262L;

    /**
     * 调用方法编号
     */
    private Integer appId;

    /**
     * 调用方法编号
     */
    private Integer serviceId;

    /**
     * 调用方法编号
     */
    private Integer invokeId;

    /**
     * 参数表达式
     */
    private String expression;

    /**
     * 线程数
     */
    private Integer poolSize;

    /**
     * 每秒请求数
     */
    private Integer qps;

    /**
     * 请求次数
     */
    private Integer questNum;

    /**
     * 随机取参
     */
    private Boolean random;

    /**
     * 保存结果
     */
    private Boolean saveResult;

    /**
     * 过滤结果
     */
    private String resultParam;

    /**
     * 检查对象是否合法
     * @return
     */
    public String checkIllegal() {
        if (null == appId) {
            return "调用应用不能为空";
        }

        if (null == serviceId) {
            return "调用服务不能为空";
        }

        if (null == invokeId) {
            return "调用方法不能为空";
        }

        qps = null == qps || 1 > qps ? 1 : qps;
        questNum = null == questNum || 1 > questNum ? 1 : questNum;
        poolSize = Math.round((questNum * 1.0F) / qps);

        if(questNum < poolSize) {
            return "请求次数必须大于并发数";
        }
        if(questNum < qps) {
            return "请求次数必须大于每秒钟请求数";
        }

        saveResult = null == saveResult ? false : saveResult;
        resultParam = null == resultParam || resultParam.trim().length() == 0 ? null : resultParam.trim();
        random = null == random ? true : random;
        return null;
    }

    public Integer getAppId() {
        return appId;
    }

    public void setAppId(Integer appId) {
        this.appId = appId;
    }

    public Integer getServiceId() {
        return serviceId;
    }

    public void setServiceId(Integer serviceId) {
        this.serviceId = serviceId;
    }

    public Integer getInvokeId() {
        return invokeId;
    }

    public void setInvokeId(Integer invokeId) {
        this.invokeId = invokeId;
    }

    public String getExpression() {
        return expression;
    }

    public void setExpression(String expression) {
        this.expression = expression;
    }

    public Integer getPoolSize() {
        return poolSize;
    }

    public void setPoolSize(Integer poolSize) {
        this.poolSize = poolSize;
    }

    public Integer getQps() {
        return qps;
    }

    public void setQps(Integer qps) {
        this.qps = qps;
    }

    public Integer getQuestNum() {
        return questNum;
    }

    public void setQuestNum(Integer questNum) {
        this.questNum = questNum;
    }

    public Boolean isRandom() {
        return random;
    }

    public void setRandom(Boolean random) {
        this.random = random;
    }

    public Boolean isSaveResult() {
        return saveResult;
    }

    public void setSaveResult(Boolean saveResult) {
        this.saveResult = saveResult;
    }

    public String getResultParam() {
        return resultParam;
    }

    public void setResultParam(String resultParam) {
        if (!StringUtil.isEmpty(resultParam)) {
            this.resultParam = resultParam;
        }
    }
}
