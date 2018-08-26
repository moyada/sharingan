package cn.moyada.faker.core.common;

/**
 * @author xueyikang
 * @create 2018-03-29 12:59
 */
public class Switch {

    private volatile boolean status;

    public Switch(boolean initStatus) {
        this.status = initStatus;
    }

    public boolean isOpen() {
        return status;
    }

    public boolean open() {
        return checkout(true);
    }

    public boolean close() {
        return checkout(false);
    }

    private boolean checkout(boolean status) {
        return this.status = status;
    }
}
