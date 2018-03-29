package cn.moyada.dubbo.faker.ui.test.invoke;

import cn.moyada.dubbo.faker.core.Main;
import cn.moyada.dubbo.faker.ui.test.BaseTest;
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
        main.invoke(1, "[\"${1.model}\"", 1, 1, 1, true, false, null);
    }
}
