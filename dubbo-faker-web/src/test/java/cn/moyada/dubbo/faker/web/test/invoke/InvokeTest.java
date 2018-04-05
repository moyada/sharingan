package cn.moyada.dubbo.faker.web.test.invoke;

import cn.moyada.dubbo.faker.core.Main;
import cn.moyada.dubbo.faker.core.model.InvokerInfo;
import cn.moyada.dubbo.faker.web.test.BaseTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author xueyikang
 * @create 2017-12-30 11:51
 */
public class InvokeTest extends BaseTest {

    @Autowired
    private Main main;
    @Test
    public void test() {
        InvokerInfo invokerInfo = new InvokerInfo();
        invokerInfo.setInvokeId(1);
        invokerInfo.setInvokeExpression("[\"${1.model}\"");
        invokerInfo.setPoolSize(1);
        invokerInfo.setQps(1);
        invokerInfo.setQuestNum(1);
        invokerInfo.setRandom(true);
        invokerInfo.setSaveResult(false);
        invokerInfo.setResultParam(null);
        main.invoke(invokerInfo);
    }
}
