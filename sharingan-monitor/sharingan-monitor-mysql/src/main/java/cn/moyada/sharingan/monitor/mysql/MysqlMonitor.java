package cn.moyada.sharingan.monitor.mysql;

import cn.moyada.sharingan.monitor.api.AbstractMonitor;

/**
 * @author xueyikang
 * @since 1.0
 **/
public class MysqlMonitor extends AbstractMonitor {

    public MysqlMonitor() {
        super(new MysqlWorker(3000, 200));
    }
}
