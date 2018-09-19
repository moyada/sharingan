package cn.moyada.sharingan.storage.mysql;

import cn.moyada.sharingan.storage.api.ArgsRepository;
import cn.moyada.sharingan.storage.mysql.dao.ArgsDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Random;

@Repository
public class ArgsRepositoryImpl implements ArgsRepository {

    private final Random random = new Random();

    @Autowired
    private ArgsDAO argsDAO;

    @Override
    public int countInvocationArgs(int appId, String domain) {
        return argsDAO.countByAppAndDomain(appId, domain);
    }

    @Override
    public List<String> findRandomInvocationArgs(int appId, String domain, int total, int size) {
        int limit = 0;
        if (total > size) {
            limit = random.nextInt(total - size);
        }

        return argsDAO.findByAppAndDomain(appId, domain, limit, size);
    }
}
