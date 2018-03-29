package cn.moyada.dubbo.faker.core.common;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author xueyikang
 * @create 2018-03-29 12:59
 */
public class Switch {

    private final AtomicBoolean status;

    public Switch(boolean initStatus) {
        this.status = new AtomicBoolean(initStatus);
    }

    public boolean isOpen() {
        return this.status.compareAndSet(true, true);
    }

    public boolean open() {
        return checkout(true);
    }

    public boolean close() {
        return checkout(false);
    }

    private boolean checkout(boolean status) {
        return this.status.compareAndSet(!status, status);
    }
}
