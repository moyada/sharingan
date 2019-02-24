package io.moyada.sharingan.application.data;

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

    private String param;

    private String body;

    private String header;

    public FunctionData(int id, String methodName, String questInfo, String expression) {
        this.id = id;
        this.methodName = methodName;
        this.questInfo = questInfo;
        this.expression = expression;
    }

    public FunctionData(int id, String methodName, String questInfo, String param, String header, String body) {
        this.id = id;
        this.methodName = methodName;
        this.questInfo = questInfo;
        this.param = param;
        this.header = header;
        this.body = body;
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
        if (null != expression) {
            return expression;
        }
        return param;
    }

    public String getBody() {
        return body;
    }

    public String getHeader() {
        return header;
    }
}
