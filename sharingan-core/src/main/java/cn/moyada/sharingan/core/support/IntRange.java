package cn.moyada.sharingan.core.support;

/**
 * @author xueyikang
 * @since 1.0
 **/
public class IntRange {

    private String target;

    private int start;

    private int end;

    public IntRange(String target, int start, int end) {
        this.target = target;
        this.start = start;
        this.end = end;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public boolean isConstant() {
        return start == end;
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
