package io.moyada.sharingan.repository.mysql.test;


import io.moyada.sharingan.domain.request.InvokeResult;
import io.moyada.sharingan.domain.request.ResultRepository;
import io.moyada.sharingan.infrastructure.invoke.data.Result;
import io.moyada.sharingan.infrastructure.util.TimeUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class ResultRepositoryTest {

    @Autowired
    private ResultRepository resultRepository;

    @Test
    public void saveResultTest() {
        Result result = Result.success("ok");
        result.setArguments("param1");
        result.setResponseTime(20);
        result.setStartTime(TimeUtil.nowTimestamp());
        InvokeResult invokeResult = new InvokeResult("test", result);
        invokeResult.buildSuccess(result, true, null);
        resultRepository.saveResult(invokeResult);
    }

    @Test
    public void batchSaveResultTest() {
        Result failed = Result.failed("error");
        failed.setArguments("param1");
        failed.setResponseTime(20);
        failed.setStartTime(TimeUtil.nowTimestamp());

        InvokeResult invokeResult1 = new InvokeResult("test", failed);
        invokeResult1.buildFailure(failed);

        Result result = Result.success("ok");
        result.setArguments("param1");
        result.setResponseTime(20);
        result.setStartTime(TimeUtil.nowTimestamp());

        InvokeResult invokeResult2 = new InvokeResult("test", result);
        invokeResult2.buildSuccess(result, false, null);

        List<InvokeResult> list = new ArrayList<>();
        list.add(invokeResult1);
        list.add(invokeResult2);
        resultRepository.saveResult(list);
    }

    @Test
    public void findResultTest() {
        List<InvokeResult> data = resultRepository.findResult("test", 1, 50);
        Assertions.assertTrue(data.size() > 0);
    }
}
