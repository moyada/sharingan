package cn.xueyikang.dubbo.faker.ui.test.spi;

import cn.xueyikang.dubbo.faker.ui.test.BaseTest;
import com.souche.car.model.api.model.ModelService;
import com.souche.car.model.common.model.ModelDTO;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author xueyikang
 * @create 2017-12-30 12:17
 */
public class ModelTest extends BaseTest {

    @Autowired
    private ModelService modelService;

    @Test
    public void test() {
        ModelDTO model = modelService.getModelByCode("14284-n");
        System.out.println(model);
    }
}
