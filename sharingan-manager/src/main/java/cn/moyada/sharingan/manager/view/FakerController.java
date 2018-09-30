package cn.moyada.sharingan.manager.view;


import cn.moyada.sharingan.common.utils.TimeUtil;
import cn.moyada.sharingan.core.Main;
import cn.moyada.sharingan.core.common.QuestInfo;
import cn.moyada.sharingan.manager.cache.CacheService;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;


/**
 * 页面接口
 * Created by xueyikang on 2017/12/22.
 */
@RestController
@RequestMapping(value = "/api", produces = MediaType.APPLICATION_JSON_VALUE)
public class FakerController {

    private static final Logger logger = LogManager.getLogger(FakerController.class);

    @Autowired
    private Main main;

    @Autowired
    private MetadataRepository metadataRepository;

    @Autowired
    private InvocationRepository invocationRepository;

    @Autowired
    private CacheService cacheService;

    // 是否已有任务在运行
    private volatile boolean running = false;

    @RequestMapping(value = "/invoke.json", method = {RequestMethod.GET, RequestMethod.POST})
    public Result invoke(@RequestParam("appId") int appId,
                         @RequestParam("serviceId") int serviceId,
                         @RequestParam("invokeId") int invokeId,
                         @RequestParam("expression") String expression,
//                         @RequestParam(value = "poolSize", required = false) Integer poolSize,
                         @RequestParam(value = "qps", required = false) Integer qps,
                         @RequestParam(value = "total", required = false) Integer total,
                         @RequestParam(value = "random", required = false) Boolean random,
                         @RequestParam(value = "saveResult", required = false) Boolean saveResult,
                         @RequestParam(value = "resultParam", required = false) String resultParam) {
        if(running) {
            return Result.failed(401, "已有任务进行中.");
        }
        QuestInfo questInfo = new QuestInfo();
        questInfo.setAppId(appId);
        questInfo.setServiceId(serviceId);
        questInfo.setInvokeId(invokeId);
        questInfo.setExpression(expression);
        questInfo.setQps(qps);
        questInfo.setQuestNum(total);
        questInfo.setRandom(random);
        questInfo.setSaveResult(saveResult);
        questInfo.setResultParam(resultParam);

        String msg = questInfo.checkIllegal();
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
            e.printStackTrace();
            String message = e.getMessage();
            logger.error(message);
            return Result.failed(500, message);
        }
        finally {
            running = false;
            TimeUtil.stopTimekeeping();
        }
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
                .map(c -> new SelectVO(c.getId().toString(), c.getName() + " [" + c.getProtocol() + "]"))
                .collect(Collectors.toList()));
    }

    @RequestMapping(value = "/getMethodByService.json", method = RequestMethod.GET)
    public Result getMethodByClass(@RequestParam("serviceId") int serviceId) {
        List<FunctionDO> functions;
        List<HttpDO> https;
        try {
            functions = metadataRepository.findFunctionByService(serviceId);
            https = metadataRepository.findHttpByService(serviceId);
        } catch (Exception e) {
            return Result.failed(500, e.getMessage());
        }

        List<SelectVO> list = new ArrayList<>();
        if (!functions.isEmpty()) {
        list.addAll(functions.stream()
                .map(c -> new SelectVO(c.getId().toString() + "-" + c.getExpression(),
                        c.getMethodName() + ", " + c.getParamType() + "," + c.getReturnType()))
                .collect(Collectors.toList()));
        }

        if (!https.isEmpty()) {
        list.addAll(https.stream()
                .map(c -> new SelectVO(c.getId().toString() + "-" + c.getExpression(),
                        c.getMethodType() + " " + c.getMethodName() + " [" + c.getParam() + "], [" + c.getHeader() + "]"))
                .collect(Collectors.toList()));
        }

        return Result.success(list);
    }

    @RequestMapping(value = "/getReport.json", method = RequestMethod.GET)
    public Result getReport(@RequestParam("fakerId") String fakerId) {
        InvocationReportDO report = cacheService.getReport(fakerId);
        return Result.success(report);
    }

    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/getResult.json", method = RequestMethod.GET)
    public PageVO<InvocationResultDO> getResult(@RequestParam("fakerId") String fakerId,
                                                @RequestParam(value = "pageIndex", required = false) Integer pageIndex,
                                                @RequestParam(value = "pageSize", required = false) Integer pageSize) {
        pageIndex = null == pageIndex || pageIndex < 1 ? 1 : pageIndex;
        pageSize = null == pageSize || pageSize < 20 ? 20 : pageSize;

        InvocationReportDO report = cacheService.getReport(fakerId);
        if (null == report) {
            return PageVO.emptyPage();
        }
        PageVO<InvocationResultDO> page = new PageVO<>();
        page.setPageIndex(pageIndex);
        page.setPageSize(pageSize);

        int total = report.getTotalInvoke();
        page.setTotal(total);
        if(0 == total) {
            page.setData(Collections.emptyList());
            return page;
        }

        page.setData(invocationRepository.findResult(fakerId, pageIndex, pageSize));
        return page;
    }
}