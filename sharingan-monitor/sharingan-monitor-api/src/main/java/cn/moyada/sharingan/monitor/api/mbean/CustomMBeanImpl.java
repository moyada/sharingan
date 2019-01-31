package cn.moyada.sharingan.monitor.api.mbean;

import javax.management.*;
import java.lang.management.PlatformManagedObject;

/**
 * @author xueyikang
 * @since 1.0
 **/
public class CustomMBeanImpl implements DynamicMBean, PlatformManagedObject {

    @Override
    public ObjectName getObjectName() {
        return null;
    }

    @Override
    public Object getAttribute(String attribute) throws AttributeNotFoundException, MBeanException, ReflectionException {
        return null;
    }

    @Override
    public void setAttribute(Attribute attribute) throws AttributeNotFoundException, InvalidAttributeValueException, MBeanException, ReflectionException {

    }

    @Override
    public AttributeList getAttributes(String[] attributes) {
        return null;
    }

    @Override
    public AttributeList setAttributes(AttributeList attributes) {
        return null;
    }

    @Override
    public Object invoke(String actionName, Object[] params, String[] signature) throws MBeanException, ReflectionException {
        return null;
    }

    @Override
    public MBeanInfo getMBeanInfo() {
        return null;
    }
}
