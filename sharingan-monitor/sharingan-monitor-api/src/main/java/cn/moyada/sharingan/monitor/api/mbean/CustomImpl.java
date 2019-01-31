package cn.moyada.sharingan.monitor.api.mbean;

import com.sun.jmx.mbeanserver.MXBeanSupport;

import javax.management.NotCompliantMBeanException;

/**
 * @author xueyikang
 * @since 1.0
 **/
public class CustomImpl extends MXBeanSupport {

    /**
     * <p>Construct an MXBean that wraps the given resource using the
     * given MXBean interface.</p>
     *
     * @param resource        the underlying resource for the new MXBean.
     * @param mxbeanInterface the interface to be used to determine
     *                        the MXBean's management interface.
     * @throws IllegalArgumentException if {@code resource} is null or
     *                                  if it does not implement the class {@code mxbeanInterface} or if
     *                                  that class is not a valid MXBean interface.
     */
    public <T> CustomImpl(T resource, Class<T> mxbeanInterface) throws NotCompliantMBeanException {
        super(resource, mxbeanInterface);
    }
}
