package io.moyada.sharingan.application;

/**
 * @author xueyikang
 * @since 1.0
 **/
public class FunctionData {

    private int id;

    /**
     * 方法名
     */
    private String methodName;

    /**
     * 请求信息
     */
    private String questInfo;

    /**
     * 表达式
     */
    private String expression;

    public FunctionData(int id, String methodName, String questInfo, String expression) {
        this.id = id;
        this.methodName = methodName;
        this.questInfo = questInfo;
        this.expression = expression;
    }

    public int getId() {
        return id;
    }

    public String getMethodName() {
        return methodName;
    }

    public String getQuestInfo() {
        return questInfo;
    }

    public String getExpression() {
        return expression;
    }
}
