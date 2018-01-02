package cn.moyada.dubbo.faker.ui.test.dao;

import cn.moyada.dubbo.faker.core.manager.FakerManager;
import cn.moyada.dubbo.faker.core.model.LogDO;
import cn.moyada.dubbo.faker.ui.test.BaseTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.Timestamp;
import java.time.Instant;

/**
 * @author xueyikang
 * @create 2017-12-30 07:12
 */
public class FakerTest extends BaseTest {

    @Autowired
    private FakerManager fakerManager;

    @Test
    public void logTest() {
        LogDO logDO = new LogDO();
        logDO.setFakerId("5345545234d");
        logDO.setCode(200);
        logDO.setMessage("66666");
        logDO.setInvokeTime(Timestamp.from(Instant.now()));
        logDO.setSpendTime(222L);
        fakerManager.saveLog(logDO);
    }
}
