package cn.xueyikang.dubbo.faker.ui.test.invoke;

import cn.xueyikang.dubbo.faker.core.request.FakerRequest;
import cn.xueyikang.dubbo.faker.ui.test.BaseTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author xueyikang
 * @create 2017-12-30 11:51
 */
public class InvokeTest extends BaseTest {

    @Autowired
    private FakerRequest fakerRequest;
    @Test
    public void test() {
        fakerRequest.request(1, "1.model", 1, 1, 1);
    }
}
