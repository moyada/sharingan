package cn.moyada.sharingan.storage.mysql.dao;

import java.util.List;

public interface ArgsDAO {

    int countByAppAndDomain(int appId, String domain);

    List<String> findByAppAndDomain(int appId, String domain, int limit, int size);
}
