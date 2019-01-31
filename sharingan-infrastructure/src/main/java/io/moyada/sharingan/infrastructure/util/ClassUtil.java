package io.moyada.sharingan.infrastructure.util;

import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author xueyikang
 * @since 1.0
 **/
public class ClassUtil {

    public static Class<?> getGenericType(final Object object, Class<?> parameterizedSuperclass, String typeParamName) {
        final Class<?> thisClass = object.getClass();

        List<Class<?>> classes = new ArrayList<>();
        Class<?> currentClass = thisClass;
        for (;;) {
            classes.add(currentClass);
            if (currentClass.getSuperclass() == parameterizedSuperclass) {
                break;
            }
            currentClass = currentClass.getSuperclass();
            if (currentClass == null) {
                break;
            }
        }

        for (int i = classes.size() - 1; i >= 0; i--) {
            currentClass = classes.get(i);
            int typeParamIndex = -1;
            TypeVariable<?>[] typeParams = currentClass.getSuperclass().getTypeParameters();
            for (int j = 0; j < typeParams.length; j++) {
                if (typeParamName.equals(typeParams[j].getName())) {
                    typeParamIndex = j;
                    break;
                }
            }

            if (typeParamIndex < 0) {
                throw new IllegalStateException(
                        "unknown type parameter '" + typeParamName + "': " + parameterizedSuperclass);
            }
            Type genericSuperType = currentClass.getGenericSuperclass();
            if (!(genericSuperType instanceof ParameterizedType)) {
                return Object.class;
            }

            Type[] actualTypeParams = ((ParameterizedType) genericSuperType).getActualTypeArguments();
            Type actualTypeParam = actualTypeParams[typeParamIndex];

            // 包装类
            if (actualTypeParam instanceof ParameterizedType) {
                actualTypeParam = ((ParameterizedType) actualTypeParam).getRawType();
            }
            // 类
            if (actualTypeParam instanceof Class) {
                return (Class<?>) actualTypeParam;
            }
            // 数组
            if (actualTypeParam instanceof GenericArrayType) {
                Type componentType = ((GenericArrayType) actualTypeParam).getGenericComponentType();
                if (componentType instanceof ParameterizedType) {
                    componentType = ((ParameterizedType) componentType).getRawType();
                }
                if (componentType instanceof Class) {
                    return Array.newInstance((Class<?>) componentType, 0).getClass();
                }
            }

            // 还是泛型
            if (actualTypeParam instanceof TypeVariable) {
                // Resolved type parameter points to another type parameter.
                TypeVariable<?> v = (TypeVariable<?>) actualTypeParam;
                if (!(v.getGenericDeclaration() instanceof Class)) {
                    return Object.class;
                }

                parameterizedSuperclass = (Class<?>) v.getGenericDeclaration();
                if (!parameterizedSuperclass.isAssignableFrom(thisClass)) {
                    return Object.class;
                }
                typeParamName = v.getName();
            }
        }

        return Object.class;
    }
}
