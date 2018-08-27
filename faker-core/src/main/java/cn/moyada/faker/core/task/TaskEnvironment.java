package cn.moyada.faker.core.task;

import cn.moyada.faker.core.common.QuestInfo;
import cn.moyada.faker.module.Dependency;
import cn.moyada.faker.module.InvokeMetadata;

public class TaskEnvironment {

    private String fakerId;

    private QuestInfo questInfo;

    private Dependency dependency;

    private InvokeMetadata invokeMetadata;

    public String getFakerId() {
        return fakerId;
    }

    public void setFakerId(String fakerId) {
        this.fakerId = fakerId;
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

    public InvokeMetadata getInvokeMetadata() {
        return invokeMetadata;
    }

    public void setInvokeMetadata(InvokeMetadata invokeMetadata) {
        this.invokeMetadata = invokeMetadata;
    }
}
