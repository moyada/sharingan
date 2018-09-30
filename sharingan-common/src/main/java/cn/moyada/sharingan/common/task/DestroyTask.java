package cn.moyada.sharingan.common.task;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;

/**
 * @author xueyikang
 * @since 1.0
 **/
@Component
public class DestroyTask implements InitializingBean {

    @Autowired
    private DefaultListableBeanFactory beanFactory;

    private volatile boolean stop;

    private List<String> destroyBeans;

    public DestroyTask() {
        this.destroyBeans = new CopyOnWriteArrayList<>();
        this.stop = true;
        new Thread(new Task()).start();
    }

    public void addDestroyBean(String beanName) {
        destroyBeans.add(beanName);
    }

    @Override
    public void afterPropertiesSet() {
        this.stop = false;
    }

    class Task implements Runnable {

        @Override
        public void run() {
            while (stop) {
                try {
                    TimeUnit.SECONDS.sleep(10L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            for (String destroyBean : destroyBeans) {
                try {
                    Object bean = beanFactory.getBean(destroyBean);
                    beanFactory.removeBeanDefinition(destroyBean);
                    beanFactory.destroyBean(bean);
                } catch (Exception e) {
                    // pass
                    continue;
                }
            }

//            try {
//                Object bean = beanFactory.getBean("destroyTask");
//                beanFactory.removeBeanDefinition("destroyTask");
//                beanFactory.destroyBean(bean);
//            } catch (Exception e) {
//                // pass
//            }
        }
    }

}
