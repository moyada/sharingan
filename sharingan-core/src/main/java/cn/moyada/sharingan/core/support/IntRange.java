package cn.moyada.sharingan.core.support;

/**
 * 整数范围策略
 * @author xueyikang
 * @since 1.0
 **/
public class IntRange {

    // 替换目标
    private String target;

    // 起始值
    private int start;

    // 结束值
    private int end;

    public IntRange(String target, int start, int end) {
        this.target = target;
        this.start = start;
        this.end = end;
    }

    /**
     * 是否为常数
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

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getEnd() {
        return end;
    }

    public void setEnd(int end) {
        this.end = end;
    }

    @Override
    public String toString() {
        return "IntRange{" +
                "start=" + start +
                ", end=" + end +
                '}';
    }
}
