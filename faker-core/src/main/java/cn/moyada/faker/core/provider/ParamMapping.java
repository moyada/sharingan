package cn.moyada.faker.core.provider;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 参数表达式数据
 * @author xueyikang
 * @create 2017-12-30 19:16
 */
public class ParamMapping {

    /**
     * 参数表达式集合
     */
    private Set<String> rebuildParamSet;

    /**
     * 每个位置参数的表达式映射关系
     * <序号, 映射规则>
     */
    private Map<Integer, Mapping> rebuildParamMap;

    public Set<String> getRebuildParamSet() {
        return rebuildParamSet;
    }

    public void setRebuildParamSet(Set<String> rebuildParamSet) {
        this.rebuildParamSet = rebuildParamSet;
    }

    public Map<Integer, Mapping> getRebuildParamMap() {
        return rebuildParamMap;
    }

    public void setRebuildParamMap(Map<Integer, Mapping> rebuildParamMap) {
        this.rebuildParamMap = rebuildParamMap;
    }

    public static class Mapping {

        /**
         * 参数表达式,映射集合
         */
        private  Map<String, TypeCount> paramMap = new HashMap<>();

        public void put(String find, String mapping) {
            TypeCount typeCount = paramMap.get(find);
            if(null == typeCount) {
                paramMap.put(find, new TypeCount(mapping));
                return;
            }

            typeCount.count = typeCount.count + 1;
        }

        public Map<String, TypeCount> getParamMap() {
            return paramMap;
        }
    }

    public static class TypeCount {

        /**
         * 映射类别
         */
        private String type;

        /**
         * 映射次数
         */
        private int count;

        public TypeCount(String type) {
            this.type = type;
            this.count = 1;
        }

        public String getType() {
            return type;
        }

        public int getCount() {
            return count;
        }
    }
}
