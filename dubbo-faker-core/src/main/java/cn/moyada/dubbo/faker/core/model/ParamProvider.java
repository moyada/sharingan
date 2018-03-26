package cn.moyada.dubbo.faker.core.model;

import java.util.Map;
import java.util.Set;

/**
 * 参数表达式数据
 * @author xueyikang
 * @create 2017-12-30 19:16
 */
public class ParamProvider {

    /**
     * 参数表达式个数
     */
    private Set<String> rebuildParamSet;

    /**
     *
     */
    private Map<Integer, Map<String, String>> rebuildParamMap;

    public Set<String> getRebuildParamSet() {
        return rebuildParamSet;
    }

    public void setRebuildParamSet(Set<String> rebuildParamSet) {
        this.rebuildParamSet = rebuildParamSet;
    }

    public Map<Integer, Map<String, String>> getRebuildParamMap() {
        return rebuildParamMap;
    }

    public void setRebuildParamMap(Map<Integer, Map<String, String>> rebuildParamMap) {
        this.rebuildParamMap = rebuildParamMap;
    }
}
