package io.moyada.sharingan.domain.request;

import io.moyada.sharingan.infrastructure.util.AssertUtil;
import io.moyada.sharingan.infrastructure.util.RuntimeUtil;
import io.moyada.sharingan.infrastructure.util.StringUtil;

/**
 * @author xueyikang
 * @since 1.0
 **/
public class QuestInfo {

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
    private Integer functionId;

    /**
     * 参数表达式
     */
    private String expression;

    /**
     * 线程数
     */
    private Integer concurrent;

    /**
     * 每秒请求数
     */
    private Integer qps;

    /**
     * 请求次数
     */
    private Integer quest;

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
    private String filterResult;

    public QuestInfo(Integer appId, Integer serviceId, Integer functionId,
                     String expression,
                     Integer concurrent, Integer qps, Integer quest,
                     Boolean random, Boolean saveResult, String filterResult) {

        this.setAppId(appId);
        this.setServiceId(serviceId);
        this.setFunctionId(functionId);
        this.setExpression(expression);
        this.setConcurrent(concurrent);
        this.setQps(qps);
        this.setQuest(quest);
        this.setRandom(random);
        this.setSaveResult(saveResult);
        this.setFilterResult(filterResult);
    }

    public String getExpression() {
        return expression;
    }

    public Integer getAppId() {
        return appId;
    }

    public Integer getServiceId() {
        return serviceId;
    }

    public Integer getFunctionId() {
        return functionId;
    }

    public Integer getConcurrent() {
        return concurrent;
    }

    public Integer getQps() {
        return qps;
    }

    public Integer getQuest() {
        return quest;
    }

    public Boolean getRandom() {
        return random;
    }

    public Boolean getSaveResult() {
        return saveResult;
    }

    public String getFilterResult() {
        return filterResult;
    }

    private void setAppId(Integer appId) {
        AssertUtil.checkoutNotNull(appId, "serviceId error");
        AssertUtil.checkoutPositive(appId);
        this.appId = appId;
    }

    private void setServiceId(Integer serviceId) {
        AssertUtil.checkoutNotNull(serviceId, "serviceId error");
        AssertUtil.checkoutPositive(serviceId);
        this.serviceId = serviceId;
    }

    private void setFunctionId(Integer functionId) {
        AssertUtil.checkoutNotNull(functionId, "functionId error");
        AssertUtil.checkoutPositive(functionId);
        this.functionId = functionId;
    }

    private void setExpression(String expression) {
        this.expression = expression;
    }

    private void setConcurrent(Integer concurrent) {
        if (null == concurrent) {
            concurrent = RuntimeUtil.getDoubleCore();
        } else {
            AssertUtil.checkoutPositive(appId);
        }
        this.concurrent = concurrent;
    }

    private void setQps(Integer qps) {
        if (null == qps) {
            qps = 1;
        } else {
            AssertUtil.checkoutPositive(qps);
        }
        this.qps = qps;
    }

    private void setQuest(Integer quest) {
        if (null == quest) {
            quest = 1;
        } else {
            AssertUtil.checkoutPositive(quest);
        }
        this.quest = quest;
    }

    private void setRandom(Boolean random) {
        this.random = null == random ? true : random;
    }

    private void setSaveResult(Boolean saveResult) {
        this.saveResult = null == saveResult ? true : saveResult;
    }

    private void setFilterResult(String filterResult) {
        this.filterResult = StringUtil.isEmpty(filterResult) ? null : filterResult;
    }
}
