package io.moyada.sharingan.expression.test.repository;

import io.moyada.sharingan.domain.expression.DataRepository;
import io.moyada.sharingan.infrastructure.util.UUIDUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * @author xueyikang
 * @since 1.0
 **/
public class DataRepositoryTest implements DataRepository {

    @Override
    public int count(int appId, String domain) {
        return 200;
    }

    @Override
    public List<String> findRandomArgs(int appId, String domain, int total, int size) {
        List<String> data = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            data.add(UUIDUtil.getUUID() + i);
        }
        return data;
    }
}
