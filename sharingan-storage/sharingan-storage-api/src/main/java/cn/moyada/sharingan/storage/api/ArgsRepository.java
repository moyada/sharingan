package cn.moyada.sharingan.storage.api;

import java.util.List;

public interface ArgsRepository {

    int countInvocationArgs(int appId, String domain);

    List<String> findRandomInvocationArgs(int appId, String domain, int total, int size);
}
