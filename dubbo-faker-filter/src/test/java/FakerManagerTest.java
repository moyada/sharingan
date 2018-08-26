import cn.moyada.dubbo.faker.filter.domain.RealParamDO;
import cn.moyada.dubbo.faker.filter.manager.FakerManager;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashSet;
import java.util.Set;

/**
 * @author xueyikang
 * @create 2018-04-02 15:17
 */
public class FakerManagerTest extends BaseTest {

    @Autowired
    FakerManager fakerManager;

    @Test
    public void test() {
        Set<RealParamDO> set = new HashSet<>();

        RealParamDO realParamDO = new RealParamDO();
        realParamDO.setAppId(1);
        realParamDO.setType("test");
        realParamDO.setParamValue("test");

        set.add(realParamDO);
        try {
            fakerManager.batchSave(set);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
