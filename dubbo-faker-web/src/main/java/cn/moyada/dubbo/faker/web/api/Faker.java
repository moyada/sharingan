package cn.moyada.dubbo.faker.web.api;

import cn.moyada.dubbo.faker.core.Main;
import cn.moyada.dubbo.faker.core.http.HttpInvoke;
import cn.moyada.dubbo.faker.core.manager.FakerManager;
import cn.moyada.dubbo.faker.core.model.InvokerInfo;
import cn.moyada.dubbo.faker.core.model.domain.LogDO;
import cn.moyada.dubbo.faker.core.model.domain.MethodInvokeDO;
import cn.moyada.dubbo.faker.core.utils.JsonUtil;
import cn.moyada.dubbo.faker.core.utils.ThreadUtil;
import cn.moyada.dubbo.faker.web.model.PageVO;
import cn.moyada.dubbo.faker.web.model.Result;
import cn.moyada.dubbo.faker.web.model.SelectVO;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

// import javax.xml.transform.Result;

/**
 * Created by xueyikang on 2017/12/22.
 */
@Api(value = "faker")
@Controller
@RequestMapping("/faker")
public class Faker {

    @Autowired
    private Main main;

    @Autowired
    private FakerManager fakerManager;

    private volatile boolean running = false;

    @ApiOperation(value = "调用虚拟请求", httpMethod = "GET", produces = "application/json")
    @ApiResponse(code = 200, message = "success", response = Result.class)
    @ResponseBody
    @RequestMapping(value = "invokeDubbo", method = RequestMethod.GET, produces = "application/json")
    public Result invokeDubbo(
                       @ApiParam(name = "invokeId", required = true, value = "请求编号", defaultValue = "1") @RequestParam("invokeId") int invokeId,
                       @ApiParam(name = "invokeExpression", required = true, value = "参数表达式", defaultValue = "[\"${1.model}\"]") @RequestParam("invokeExpression") String invokeExpression,
                       @ApiParam(name = "poolSize", value = "并发数") @RequestParam(value = "poolSize", required = false) Integer poolSize,
                       @ApiParam(name = "qps", value = "每秒钟请求数") @RequestParam(value = "qps", required = false) Integer qps,
                       @ApiParam(name = "loop", value = "请求次数") @RequestParam(value = "loop", required = false) Integer loop,
                       @ApiParam(name = "random", value = "随机请求") @RequestParam(value = "random", required = false, defaultValue = "1") Integer random,
                       @ApiParam(name = "saveResult", value = "保存结果", defaultValue = "false") @RequestParam(value = "saveResult", required = false) Boolean saveResult,
                       @ApiParam(name = "resultParam", value = "选定保存结果参数") @RequestParam(value = "resultParam", required = false) String resultParam
                                ) {
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

        InvokerInfo invokerInfo = new InvokerInfo();
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

    @ApiOperation(value = "调用虚拟请求", httpMethod = "GET", produces = "application/json")
    @ApiResponse(code = 200, message = "success", response = Result.class)
    @ResponseBody
    @RequestMapping(value = "invokeHttp", method = RequestMethod.GET, produces = "application/json")
    public Result invokeHttp(
                       @ApiParam(name = "invokeInfo", required = true, value = "请求信息，多个以逗号分隔",
                               defaultValue = "[{ \"url\": \"http://test.dubbo-faker/api/get\", \"method\": \"get\", \"header\": \"\", \"cookie\": \"{\"JSESSIONID\": \"ByOK3vjFD72aPnrF7C2HmdnV6TZcEbzWoWiBYEnLerjQ99zWpBng\"}\", \"param\": \"\"}]")
                       @RequestParam("invokeInfo") String invokeInfo,

                       @ApiParam(name = "poolSize", value = "并发数") @RequestParam(value = "poolSize", required = false) Integer poolSize,
                       @ApiParam(name = "qps", value = "每秒钟请求数") @RequestParam(value = "qps", required = false) Integer qps,
                       @ApiParam(name = "loop", value = "请求次数") @RequestParam(value = "loop", required = false) Integer loop,
                       @ApiParam(name = "saveResult", value = "保存结果", defaultValue = "false") @RequestParam(value = "saveResult", required = false) Boolean saveResult,
                       @ApiParam(name = "resultParam", value = "选定保存结果参数") @RequestParam(value = "resultParam", required = false) String resultParam
                                ) {
        poolSize = null == poolSize || 1 > poolSize ? 1 : poolSize;
        qps = null == qps || 1 > qps ? 1 : qps;
        loop = null == loop || 1 > loop ? 1 : loop;
        if(loop < poolSize) {
            return Result.failed(503, "请求次数必须大于并发数");
        }
        if(loop < qps) {
            return Result.failed(503, "请求次数必须大于每秒钟请求数");
        }
        List<HttpInvoke> httpInvokes = JsonUtil.toList(invokeInfo, HttpInvoke.class);

        saveResult = null == saveResult ? false : saveResult;
        resultParam = null == resultParam || resultParam.trim().length() == 0 ? null : resultParam.trim();
        return Result.success(null);
    }

    @ApiOperation(value = "获取全部请求方法信息", httpMethod = "GET")
    @ApiResponse(code = 200, message = "success", response = Result.class)
    @ResponseBody
    @RequestMapping(value = "getAllInvoke", method = RequestMethod.GET)
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

    @ApiOperation(value = "获取全部项目", httpMethod = "GET")
    @ApiResponse(code = 200, message = "success", response = Result.class)
    @ResponseBody
    @RequestMapping(value = "getAllApp", method = RequestMethod.GET)
    public Result getAllApp() {
        try {
            return Result.success(fakerManager.getAllApp());
        } catch (Exception e) {
            e.printStackTrace();
            return Result.failed(500, e.getMessage());
        }
    }

    @ApiOperation(value = "获取接口", httpMethod = "GET")
    @ApiResponse(code = 200, message = "success", response = Result.class)
    @ResponseBody
    @RequestMapping(value = "getClassByApp", method = RequestMethod.GET)
    public Result getClassByApp(@ApiParam(name = "appId", required = true, value = "项目编号") @RequestParam("appId") int appId) {
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

    @ApiOperation(value = "获取方法", httpMethod = "GET")
    @ApiResponse(code = 200, message = "success", response = Result.class)
    @ResponseBody
    @RequestMapping(value = "getMethodByClass", method = RequestMethod.GET)
    public Result getMethodByClass(@ApiParam(name = "className", required = true, value = "类名") @RequestParam("className") String className) {
        try {
            return Result.success(fakerManager.getMethodByClass(className));
        } catch (Exception e) {
            return Result.failed(500, e.getMessage());
        }
    }

    @ApiOperation(value = "获取结果", httpMethod = "GET")
    @ApiResponse(code = 200, message = "success", response = Result.class)
    @ResponseBody
    @RequestMapping(value = "getMethodByFakerId", method = RequestMethod.GET)
    public PageVO<LogDO> getMethodByFakerId(@ApiParam(name = "fakerId", required = true, value = "请求序号") @RequestParam("fakerId") String fakerId,
                                            @ApiParam(name = "pageIndex", value = "页数") @RequestParam(value = "pageIndex", required = false) Integer pageIndex,
                                            @ApiParam(name = "pageSize", value = "页面大小") @RequestParam(value = "pageSize", required = false) Integer pageSize) {
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

    @ApiOperation(value = "关闭线程池", httpMethod = "GET")
    @ApiImplicitParam(name = "fakerId", value = "用户ID", required = true, dataType = "String")
    @ApiResponse(code = 200, message = "success", response = Result.class)
    @ResponseBody
    @RequestMapping(value = "kill/{fakerId}", method = RequestMethod.GET)
    public Result kill(@PathVariable(value = "fakerId", required = true) String fakerId) {
        try {
            return Result.success(fakerId + " 关闭成功");
        }
        catch (Exception e) {
            return Result.failed(500, "指定线程池不存在 " + fakerId);
        }
    }
}