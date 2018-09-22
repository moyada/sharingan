package cn.moyada.sharingan.core.support;

/**
 * @author xueyikang
 * @since 1.0
 **/
public class DoubleRange {

    private String target;

    private double start;

    private double end;

    public DoubleRange(String target, double start, double end) {
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
                "start=" + start +
                ", end=" + end +
                '}';
    }
}
