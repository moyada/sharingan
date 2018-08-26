package cn.moyada.faker.core;

/**
 * 调用信息
 * @author xueyikang
 * @create 2018-04-05 15:17
 */
public class QuestInfo {

    /**
     * 调用方法编号
     */
    private int invokeId;

    /**
     * 参数表达式
     */
    private String invokeExpression;

    /**
     * 线程数
     */
    private int poolSize;

    /**
     * 每秒请求数
     */
    private int qps;

    /**
     * 请求次数
     */
    private int questNum;

    /**
     * 随机取参
     */
    private boolean random;

    /**
     * 保存结果
     */
    private boolean saveResult;

    /**
     * 过滤结果
     */
    private String resultParam;

    public int getInvokeId() {
        return invokeId;
    }

    public void setInvokeId(int invokeId) {
        this.invokeId = invokeId;
    }

    public String getInvokeExpression() {
        return invokeExpression;
    }

    public void setInvokeExpression(String invokeExpression) {
        this.invokeExpression = invokeExpression;
    }

    public int getPoolSize() {
        return poolSize;
    }

    public void setPoolSize(int poolSize) {
        this.poolSize = poolSize;
    }

    public int getQps() {
        return qps;
    }

    public void setQps(int qps) {
        this.qps = qps;
    }

    public int getQuestNum() {
        return questNum;
    }

    public void setQuestNum(int questNum) {
        this.questNum = questNum;
    }

    public boolean isRandom() {
        return random;
    }

    public void setRandom(boolean random) {
        this.random = random;
    }

    public boolean isSaveResult() {
        return saveResult;
    }

    public void setSaveResult(boolean saveResult) {
        this.saveResult = saveResult;
    }

    public String getResultParam() {
        return resultParam;
    }

    public void setResultParam(String resultParam) {
        this.resultParam = resultParam;
    }
}
