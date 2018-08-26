package cn.moyada.faker.common.utils;

import cn.moyada.faker.common.model.CleanerReference;
import com.sun.istack.internal.Nullable;

import java.security.AccessController;
import java.security.PrivilegedAction;

/**
 * 对象清理工具
 * @author xueyikang
 * @create 2018-06-01 11:38
 */
public class CleanerUtil {

    /**
     * 创建对象引用清理类
     * @param reference 对象引用
     * @param <T> 对象类型
     * @return 清理类
     */
    public static <T> CleanerReference<T> create(T reference) {
        return CleanerReference.create(reference);
    }

    /**
     * 通过清理对象清理引用
     * @param cleaner 清理类
     */
    public static <T> boolean cleaner(CleanerReference<T> cleaner) {
        return AccessController.doPrivileged(new PrivilegedAction<T>() {
            @Override
            @Nullable
            public T run() {
                try {
                    cleaner.cleaner();
                    return null;
                }
                catch (Throwable e) {
                    return cleaner.getReference();
                }
            }
        }) == null;
    }
}
