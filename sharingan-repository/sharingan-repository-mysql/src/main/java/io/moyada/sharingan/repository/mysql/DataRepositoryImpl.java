package io.moyada.sharingan.repository.mysql;


import io.moyada.sharingan.domain.expression.DataRepository;
import io.moyada.sharingan.repository.mysql.dao.DataDAO;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Random;

public class DataRepositoryImpl implements DataRepository {

    private final Random random = new Random();

    @Autowired
    private DataDAO dataDAO;

    @Override
    public int count(int appId, String domain) {
        return dataDAO.countByAppAndDomain(appId, domain);
    }

    @Override
    public List<String> findRandomArgs(int appId, String domain, int total, int size) {
        int limit = 0;
        if (total > size) {
            limit = random.nextInt(total - size);
        }

        return dataDAO.findByAppAndDomain(appId, domain, limit, size);
    }
}
