package cn.moyada.sharingan.core.support;

/**
 * 浮点数范围策略
 * @author xueyikang
 * @since 1.0
 **/
public class DoubleRange {

    // 替换目标
    private String target;

    // 精确小数位
    private int precision;

    // 起始值
    private double start;

    // 结束值
    private double end;

    public DoubleRange(String target, int precision, double start, double end) {
        this.target = target;
        this.precision = precision;
        this.start = start;
        this.end = end;
    }

    /**
     * 是否为常量
     * @return
     */
    public boolean isConstant() {
        return start == end;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public int getPrecision() {
        return precision;
    }

    public void setPrecision(int precision) {
        this.precision = precision;
    }

    public double getStart() {
        return start;
    }

    public void setStart(double start) {
        this.start = start;
    }

    public double getEnd() {
        return end;
    }

    public void setEnd(double end) {
        this.end = end;
    }

    @Override
    public String toString() {
        return "DoubleRange{" +
                "target='" + target + '\'' +
                ", precision=" + precision +
                ", start=" + start +
                ", end=" + end +
                '}';
    }
}
