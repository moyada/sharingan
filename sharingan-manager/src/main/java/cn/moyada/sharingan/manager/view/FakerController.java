package cn.moyada.sharingan.manager.view;


import cn.moyada.sharingan.common.utils.ThreadUtil;
import cn.moyada.sharingan.common.utils.TimeUtil;
import cn.moyada.sharingan.core.Main;
import cn.moyada.sharingan.core.common.QuestInfo;
import cn.moyada.sharingan.manager.vo.PageVO;
import cn.moyada.sharingan.manager.vo.Result;
import cn.moyada.sharingan.manager.vo.SelectVO;
import cn.moyada.sharingan.storage.api.InvocationRepository;
import cn.moyada.sharingan.storage.api.MetadataRepository;
import cn.moyada.sharingan.storage.api.domain.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;


/**
 * Created by xueyikang on 2017/12/22.
 */
@RestController
@RequestMapping(value = "/faker", produces = MediaType.APPLICATION_JSON_VALUE)
public class FakerController {

    private static final Logger logger = LogManager.getLogger(FakerController.class);

    @Autowired
    private Main main;

    @Autowired
    private MetadataRepository metadataRepository;

    @Autowired
    private InvocationRepository invocationRepository;

    private volatile boolean running = false;

    @RequestMapping(value = "/invoke.json", method = {RequestMethod.GET, RequestMethod.POST})
    public Result invoke(@RequestParam("invokeId") int invokeId,
                         @RequestParam("expression") String expression,
//                         @RequestParam(value = "poolSize", required = false) Integer poolSize,
                         @RequestParam(value = "qps", required = false) Integer qps,
                         @RequestParam(value = "loop", required = false) Integer loop,
                         @RequestParam(value = "random", required = false) Boolean random,
                         @RequestParam(value = "saveResult", required = false) Boolean saveResult,
                         @RequestParam(value = "resultParam", required = false) String resultParam) {
        if(running) {
            return Result.failed(401, "已有任务进行中.");
        }
        QuestInfo questInfo = new QuestInfo();
        questInfo.setFuncId(invokeId);
        questInfo.setExpression(expression);
        questInfo.setQps(qps);
        questInfo.setQuestNum(loop);
        questInfo.setRandom(random);
        questInfo.setSaveResult(saveResult);
        questInfo.setResultParam(resultParam);

        String msg = questInfo.checkoutSelf();
        if (null != msg) {
            return Result.failed(503, msg);
        }

        running = true;
        TimeUtil.doTimekeeping();
        try {
            String data = main.invoke(questInfo);
            return Result.success(data);
        }
        catch (Exception e) {
            String message = e.getMessage();
            logger.error(message);
            return Result.failed(500, message);
        }
        finally {
            running = false;
            TimeUtil.stopTimekeeping();
        }
    }

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
        invokerInfo.setFuncId(invokeId);
        invokerInfo.setExpression(invokeExpression);
        invokerInfo.setPoolSize(poolSize);
        invokerInfo.setQps(qps);
        invokerInfo.setQuestNum(loop);
        invokerInfo.setRandom(random == 1);
        invokerInfo.setSaveResult(saveResult);
        invokerInfo.setResultParam(resultParam);

        running = true;
        TimeUtil.doTimekeeping();
        try {
            String data = main.invoke(invokerInfo);
            return Result.success(data);
        }
        catch (Exception e) {
            String message = e.getMessage();
            logger.error(message);
            return Result.failed(500, message);
        }
        finally {
            running = false;
            TimeUtil.stopTimekeeping();
            ThreadUtil.clear();
        }
    }

    @RequestMapping(value = "/getAllInvoke.json", method = RequestMethod.GET)
    public Result getAllInvoke() {
        List<FunctionDO> all;
        try {
            all = metadataRepository.findFunctionByService(-1);
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
        List<AppDO> apps;
        try {
            apps = metadataRepository.findAllApp();
        } catch (Exception e) {
            e.printStackTrace();
            return Result.failed(500, e.getMessage());
        }
        return Result.success(apps.stream()
                .map(c -> new SelectVO(c.getId().toString(), c.getName()))
                .collect(Collectors.toList()));
    }


    @RequestMapping(value = "/getServiceByApp.json", method = RequestMethod.GET)
    public Result getClassByApp(@RequestParam(value = "appId") int appId) {
        List<ServiceDO> services;
        try {
            services = metadataRepository.findServiceByApp(appId);
        } catch (Exception e) {
            return Result.failed(500, e.getMessage());
        }
        return Result.success(services.stream()
                .map(c -> new SelectVO(c.getId().toString(), c.getName()))
                .collect(Collectors.toList()));
    }

    @RequestMapping(value = "/getMethodByService.json", method = RequestMethod.GET)
    public Result getMethodByClass(@RequestParam("serviceId") int serviceId) {
        List<FunctionDO> functions;
        try {
            functions = metadataRepository.findFunctionByService(serviceId);
        } catch (Exception e) {
            return Result.failed(500, e.getMessage());
        }
        return Result.success(functions);
    }

    @RequestMapping(value = "/getResult.json", method = RequestMethod.GET)
    public PageVO<InvocationResultDO> getMethodByFakerId(@RequestParam("fakerId") String fakerId,
                                                         @RequestParam(value = "pageIndex", required = false) Integer pageIndex,
                                                         @RequestParam(value = "pageSize", required = false) Integer pageSize) {
        pageIndex = null == pageIndex || pageIndex < 1 ? 1 : pageIndex;
        pageSize = null == pageSize || pageSize < 20 ? 20 : pageSize;

        PageVO<InvocationResultDO> page = new PageVO<>();
        page.setPageIndex(pageIndex);
        page.setPageSize(pageSize);

        InvocationReportDO report = invocationRepository.findReport(fakerId);
        if (null == report) {
            return page;
        }
        int total = report.getTotalInvoke();
        page.setTotal(total);
        if(0 == total) {
            page.setData(Collections.emptyList());
            return page;
        }

        page.setData(invocationRepository.findResult(fakerId, pageIndex, pageSize));
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