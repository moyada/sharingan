package io.moyada.sharingan.manager.view;


import io.moyada.sharingan.application.data.FunctionData;
import io.moyada.sharingan.application.InvokeService;
import io.moyada.sharingan.application.ConfigService;
import io.moyada.sharingan.application.ResultService;
import io.moyada.sharingan.application.data.PageData;
import io.moyada.sharingan.domain.metadada.AppData;
import io.moyada.sharingan.domain.metadada.ServiceData;
import io.moyada.sharingan.domain.request.InvokeReport;
import io.moyada.sharingan.domain.request.QuestInfo;
import io.moyada.sharingan.manager.vo.Result;
import io.moyada.sharingan.manager.vo.SelectVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

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

    private static final Logger logger = LoggerFactory.getLogger(FakerController.class);

    @Autowired
    private InvokeService invokeService;

    @Autowired
    private ConfigService configService;

    @Autowired
    private ResultService resultService;

//     是否已有任务在运行
//    private volatile boolean running = false;

    @RequestMapping(value = "/invoke.json", method = {RequestMethod.GET, RequestMethod.POST})
    public Result<?> invoke(@RequestParam("appId") int appId,
                         @RequestParam("serviceId") int serviceId,
                         @RequestParam("invokeId") int invokeId,
                         @RequestParam(value = "expression", defaultValue = "{}") String expression,
                         @RequestParam(value = "concurrent", defaultValue = "1") Integer concurrent,
                         @RequestParam(value = "qps", required = false, defaultValue = "1") Integer qps,
                         @RequestParam(value = "total", required = false, defaultValue = "1") Integer total,
                         @RequestParam(value = "random", required = false) Boolean random,
                         @RequestParam(value = "save", required = false) Boolean saveResult,
                         @RequestParam(value = "resultParam", required = false) String resultParam) {
//        if(running) {
//            return Result.failed(401, "已有任务进行中.");
//        }

        QuestInfo questInfo;
        try {
            questInfo = new QuestInfo(appId, serviceId, invokeId, expression, concurrent, qps, total, random, saveResult, resultParam);
        } catch (Exception e) {
            return Result.failed(501, e.getMessage());
        }

        io.moyada.sharingan.infrastructure.invoke.data.Result<String> result = invokeService.getInvokeData(questInfo);

        if (!result.isSuccess()) {
            return Result.failed(500, result.getException());
        }
        return Result.success(result.getResult());
    }

    @GetMapping(value = "/getAllApp.json")
    public Result<?> getAllApp() {
        List<AppData> apps;
        try {
            apps = configService.getApp();
        } catch (Exception e) {
            e.printStackTrace();
            return Result.failed(500, e.getMessage());
        }
        return Result.success(apps.stream()
                .map(c -> new SelectVO(c.getId().toString(), c.getName()))
                .collect(Collectors.toList()));
    }

    @GetMapping(value = "/getServiceByApp.json")
    public Result<?> getClassByApp(@RequestParam(value = "appId") int appId) {
        List<ServiceData> services;
        try {
            services = configService.getService(appId);
        } catch (Exception e) {
            return Result.failed(500, e.getMessage());
        }
        return Result.success(services.stream()
                .map(c -> new SelectVO(c.getId().toString(), c.getName() + " [" + c.getProtocol().name() + "]"))
                .collect(Collectors.toList()));
    }

    @GetMapping(value = "/getMethodByService.json")
    public Result<?> getMethodByClass(@RequestParam("serviceId") int serviceId) {
        List<FunctionData> data = configService.getFunction(serviceId);
        if (data.isEmpty()) {
            return Result.success(Collections.emptyList());
        }

        List<SelectVO> collect = data.stream()
                .map(d -> new SelectVO(d.getId() + "-" + d.getExpression(), d.getMethodName() + d.getQuestInfo()))
                .collect(Collectors.toList());

        return Result.success(collect);
    }

    @GetMapping(value = "/getReport.json")
    public Result<?> getReport(@RequestParam("fakerId") String fakerId) {
        InvokeReport report = resultService.getReport(fakerId);
        return Result.success(report);
    }

    @GetMapping(value = "/getResult.json")
    public PageData<?> getResult(@RequestParam("fakerId") String fakerId,
                                            @RequestParam(value = "pageIndex", required = false) Integer pageIndex,
                                            @RequestParam(value = "pageSize", required = false) Integer pageSize) {
        pageIndex = null == pageIndex || pageIndex < 1 ? 1 : pageIndex;
        pageSize = null == pageSize || pageSize < 20 ? 20 : pageSize;

        return resultService.getPage(fakerId, pageIndex, pageSize);
    }
}