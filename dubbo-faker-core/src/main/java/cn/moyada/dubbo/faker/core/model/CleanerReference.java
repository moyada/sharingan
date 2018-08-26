package cn.moyada.dubbo.faker.core.model;

import sun.misc.Cleaner;

/**
 * 引用对象清除
 * @author xueyikang
 * @create 2018-06-01 11:46
 */
public class CleanerReference<T> {

    private final T reference;
    /**
     * 对象对应清理类
     */
    private final Cleaner cleaner;

    public static <T> CleanerReference<T> create(T reference) {
        return new CleanerReference<>(reference);
    }

    public CleanerReference(T reference) {
        this.cleaner = Cleaner.create(reference, null);
        this.reference = reference;
    }

    /**
     * 在下一次垃圾回收清理对象
     */
    public void cleaner() {
        cleaner.clear();
        cleaner.clean();
    }

    @SuppressWarnings("unchecked")
    public T getReference() {
        return reference;
    }
}