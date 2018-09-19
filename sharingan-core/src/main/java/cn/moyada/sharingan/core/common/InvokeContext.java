package cn.moyada.sharingan.core.common;

import cn.moyada.sharingan.module.Dependency;
import cn.moyada.sharingan.module.InvokeMetaData;

/**
 * 调用任务上下文
 */
public class InvokeContext {

    /**
     * 测试编码
     */
    private String fakerId;

    /**
     * 参数表达式
     */
    private String[] expression;

    /**
     * 协议
     */
    private String protocol;

    private QuestInfo questInfo;

    private Dependency dependency;

    private InvokeMetaData invokeMetaData;

    public String getFakerId() {
        return fakerId;
    }

    public void setFakerId(String fakerId) {
        this.fakerId = fakerId;
    }

    public String[] getExpression() {
        return expression;
    }

    public void setExpression(String[] expression) {
        this.expression = expression;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public QuestInfo getQuestInfo() {
        return questInfo;
    }

    public void setQuestInfo(QuestInfo questInfo) {
        this.questInfo = questInfo;
    }

    public Dependency getDependency() {
        return dependency;
    }

    public void setDependency(Dependency dependency) {
        this.dependency = dependency;
    }

    public InvokeMetaData getInvokeMetaData() {
        return invokeMetaData;
    }

    public void setInvokeMetaData(InvokeMetaData invokeMetaData) {
        this.invokeMetaData = invokeMetaData;
    }
}
