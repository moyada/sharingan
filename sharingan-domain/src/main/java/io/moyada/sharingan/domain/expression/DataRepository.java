package io.moyada.sharingan.domain.expression;

import java.util.List;

/**
 * 参数获取
 * @author xueyikang
 * @since 0.0.1
 */
public interface DataRepository {

    /**
     * 获取领域数据量大小
     * @param appId
     * @param domain
     * @return
     */
    int count(int appId, String domain);

    /**
     * 随机截取领域数据
     * @param appId
     * @param domain
     * @param size
     * @return
     */
    List<String> findRandomArgs(int appId, String domain, int total, int size);
}
