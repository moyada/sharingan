package io.moyada.sharingan.spring.parser;


import io.moyada.sharingan.monitor.api.Monitor;
import io.moyada.sharingan.spring.parser.util.StringUtil;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.TypedStringValue;
import org.springframework.beans.factory.support.ManagedMap;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.beans.factory.xml.BeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * @author xueyikang
 * @since 0.0.1
 **/
public class MonitorBeanDefinitionParser implements BeanDefinitionParser {

    private static final Class<?> target = Monitor.class;

    @Override
    public BeanDefinition parse(Element element, ParserContext parserContext) {
        String id = element.getAttribute("id");
        String name = element.getAttribute("name");
        String enable = element.getAttribute("enable");
        String type = element.getAttribute("type");


        checkType(type);

        if (StringUtil.isEmpty(name)) {
            throw new IllegalStateException("<sharingan:application ... /> properties name can not be null.");
        }

        if (StringUtil.isEmpty(name)) {
            name = StringUtil.getSimpleName(type);
        }

        RootBeanDefinition beanDefinition = new RootBeanDefinition();

        if (StringUtil.isEmpty(id)) {
            id = target.getName();
        } else {
            if (parserContext.getRegistry().containsBeanDefinition(id)) {
                throw new IllegalStateException("Duplicate spring bean id " + id);
            }
        }

        if (parserContext.getRegistry().containsBeanDefinition(id)) {
            int index = 1;
            while (parserContext.getRegistry().containsBeanDefinition(id + "-" + index)) {
                index++;
            }
            id = id + "-" + index;
        }
        parserContext.getRegistry().registerBeanDefinition(id, beanDefinition);

        ManagedMap parameterMap = parseParameters(element.getChildNodes());

        beanDefinition.setBeanClass(target);
        beanDefinition.setLazyInit(false);
        beanDefinition.getPropertyValues().addPropertyValue("id", id);
        beanDefinition.getPropertyValues().addPropertyValue("name", name);
        beanDefinition.getPropertyValues().addPropertyValue("type", type);
        beanDefinition.getPropertyValues().addPropertyValue("enable", enable);
        beanDefinition.getPropertyValues().addPropertyValue("parameters", parameterMap);
        return beanDefinition;
    }

    @SuppressWarnings("unchecked")
    private static ManagedMap parseParameters(NodeList nodeList) {
        if (nodeList != null && nodeList.getLength() > 0) {
            ManagedMap parameters = null;
            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);
                if (node instanceof Element) {
                    if ("parameter".equals(node.getNodeName())
                            || "parameter".equals(node.getLocalName())) {
                        if (parameters == null) {
                            parameters = new ManagedMap();
                        }
                        String key = ((Element) node).getAttribute("key");
                        String value = ((Element) node).getAttribute("value");
                        parameters.put(key, new TypedStringValue(value, String.class));
                    }
                }
            }
            return parameters;
        }
        return null;
    }

    private void checkAddress(String address) {
        if (StringUtil.isEmpty(address)) {
            throw new IllegalStateException("<sharingan:monitor ... /> properties address can not be null.");
        }
        if (!address.startsWith("http://") && !address.startsWith("https://")) {
            throw new IllegalStateException("[Init Sharingan Monitor Error] address can not be null.");
        }
    }

    private void checkType(String type) {
        if (StringUtil.isEmpty(type)) {
            throw new IllegalStateException("<sharingan:monitor ... /> properties type can not be null.");
        }
    }
}