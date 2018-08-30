package cn.moyada.faker.dashboard.view;


import cn.moyada.faker.common.utils.ThreadUtil;
import cn.moyada.faker.core.Main;
import cn.moyada.faker.core.common.QuestInfo;
import cn.moyada.faker.dashboard.vo.PageVO;
import cn.moyada.faker.dashboard.vo.Result;
import cn.moyada.faker.dashboard.vo.SelectVO;
import cn.moyada.faker.manager.FakerManager;
import cn.moyada.faker.manager.domain.LogDO;
import cn.moyada.faker.manager.domain.MethodInvokeDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;


/**
 * Created by xueyikang on 2017/12/22.
 */
@RestController(value = "/faker")
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
public class FakerController {

    @Autowired
    private Main main;

    @Autowired
    private FakerManager fakerManager;

    private volatile boolean running = false;

    @RequestMapping(value = "/invokeDubbo.json", method = {RequestMethod.GET, RequestMethod.POST})
    public Result invokeDubbo(@RequestParam("invokeId") int invokeId,
                              @RequestParam("invokeExpression") String invokeExpression,
                              @RequestParam(value = "poolSize", required = false) Integer poolSize,
                              @RequestParam(value = "qps", required = false) Integer qps,
                              @RequestParam(value = "loop", required = false) Integer loop,
                              @RequestParam(value = "random", required = false, defaultValue = "1") Integer random,
                              @RequestParam(value = "saveResult", required = false) Boolean saveResult,
                              @RequestParam(value = "resultParam", required = false) String resultParam) {
        if(running) {
            return Result.failed(401, "已有任务进行中");
        }

        qps = null == qps || 1 > qps ? 1 : qps;
        loop = null == loop || 1 > loop ? 1 : loop;
        poolSize = Math.round((loop * 1.0F) / qps);
        if(loop < poolSize) {
            return Result.failed(503, "请求次数必须大于并发数");
        }
        if(loop < qps) {
            return Result.failed(503, "请求次数必须大于每秒钟请求数");
        }
        saveResult = null == saveResult ? false : saveResult;
        resultParam = null == resultParam || resultParam.trim().length() == 0 ? null : resultParam.trim();

        QuestInfo invokerInfo = new QuestInfo();
        invokerInfo.setInvokeId(invokeId);
        invokerInfo.setInvokeExpression(invokeExpression);
        invokerInfo.setPoolSize(poolSize);
        invokerInfo.setQps(qps);
        invokerInfo.setQuestNum(loop);
        invokerInfo.setRandom(random == 1);
        invokerInfo.setSaveResult(saveResult);
        invokerInfo.setResultParam(resultParam);

        running = true;
        try {
            String data = main.invoke(invokerInfo);
            return Result.success(data);
        }
        catch (Exception e) {
            return Result.failed(500, e.getMessage());
        }
        finally {
            running = false;
            ThreadUtil.clear();
        }
    }

    @RequestMapping(value = "/getAllInvoke.json", method = RequestMethod.GET)
    public Result getAllInvoke() {
        List<MethodInvokeDO> all;
        try {
            all = fakerManager.getAll();
        } catch (Exception e) {
            return Result.failed(500, e.getMessage());
        }
        return Result.success(all.stream()
                .map(item -> new SelectVO(item.getId().toString(),
                        item.getClassName() + ", " +
                                item.getMethodName() + ", " +
                                item.getParamType() + ", " +
                                item.getReturnType()
                ))
                .collect(Collectors.toList()));
    }

    @RequestMapping(value = "/getAllApp.json", method = RequestMethod.GET)
    public Result getAllApp() {
        try {
            return Result.success(fakerManager.getAllApp());
        } catch (Exception e) {
            e.printStackTrace();
            return Result.failed(500, e.getMessage());
        }
    }


    @RequestMapping(value = "/getClassByApp.json", method = RequestMethod.GET)
    public Result getClassByApp(@RequestParam("appId") int appId) {
        List<String> classList;
        try {
            classList = fakerManager.getClassByApp(appId);
        } catch (Exception e) {
            return Result.failed(500, e.getMessage());
        }
        return Result.success(classList.stream()
                .map(c -> new SelectVO(c, c))
                .collect(Collectors.toList()));
    }

    @RequestMapping(value = "/getMethodByClass.json", method = RequestMethod.GET)
    public Result getMethodByClass(@RequestParam("className") String className) {
        try {
            return Result.success(fakerManager.getMethodByClass(className));
        } catch (Exception e) {
            return Result.failed(500, e.getMessage());
        }
    }

    @RequestMapping(value = "/getMethodByFakerId.json", method = RequestMethod.GET)
    public PageVO<LogDO> getMethodByFakerId(@RequestParam("fakerId") String fakerId,
                                            @RequestParam(value = "pageIndex", required = false) Integer pageIndex,
                                            @RequestParam(value = "pageSize", required = false) Integer pageSize) {
        pageIndex = null == pageIndex || pageIndex < 1 ? 1 : pageIndex;
        pageSize = null == pageSize || pageSize < 20 ? 20 : pageSize;

        PageVO<LogDO> page = new PageVO<>();
        page.setPageIndex(pageIndex);
        page.setPageSize(pageSize);

        int total = fakerManager.countMethodByFakerId(fakerId);
        page.setTotal(total);
        if(0 == total) {
            page.setData(Collections.emptyList());
            return page;
        }

        page.setData(fakerManager.getMethodByFakerId(fakerId, pageIndex, pageSize));
        return page;
    }

    @RequestMapping(value = "/kill/{fakerId}", method = RequestMethod.GET)
    public Result kill(@PathVariable(value = "fakerId", required = true) String fakerId) {
        try {
            return Result.success(fakerId + " 关闭成功");
        }
        catch (Exception e) {
            return Result.failed(500, "指定线程池不存在 " + fakerId);
        }
    }
}