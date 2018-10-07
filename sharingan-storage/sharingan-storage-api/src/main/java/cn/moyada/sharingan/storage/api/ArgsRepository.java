package cn.moyada.sharingan.storage.api;

import java.util.List;

/**
 * 参数获取
 * @author xueyikang
 * @since 0.0.1
 */
public interface ArgsRepository {

    /**
     * 获取领域数据量大小
     * @param appId
     * @param domain
     * @return
     */
    int countInvocationArgs(int appId, String domain);

    /**
     * 随机截取领域数据
     * @param appId
     * @param domain
     * @param total
     * @param size
     * @return
     */
    List<String> findRandomInvocationArgs(int appId, String domain, int total, int size);
}
