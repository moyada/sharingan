package cn.moyada.dubbo.faker.ui.api;

import cn.moyada.dubbo.faker.core.manager.FakerManager;
import cn.moyada.dubbo.faker.core.model.LogDO;
import cn.moyada.dubbo.faker.core.model.MethodInvokeDO;
import cn.moyada.dubbo.faker.core.request.FakerRequest;
import cn.moyada.dubbo.faker.ui.model.PageVO;
import cn.moyada.dubbo.faker.ui.model.Result;
import cn.moyada.dubbo.faker.ui.model.SelectVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

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
    private FakerRequest fakerRequest;

    @Autowired
    private FakerManager fakerManager;

    @ApiOperation(value = "调用虚拟请求", httpMethod = "GET", produces = "application/json")
    @ApiResponse(code = 200, message = "success", response = Result.class)
    @ResponseBody
    @RequestMapping(value = "invoke", method = RequestMethod.GET, produces = "application/json")
    public Result invoke(
                       @ApiParam(name = "invokeId", required = true, value = "请求编号", defaultValue = "1") @RequestParam("invokeId") int invokeId,
                       @ApiParam(name = "invokeExpression", required = true, value = "参数表达式", defaultValue = "[\"${1.model}\"]") @RequestParam("invokeExpression") String invokeExpression,
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
        saveResult = null == saveResult ? false : saveResult;
        resultParam = null == resultParam || resultParam.trim().length() == 0 ? null : resultParam.trim();
        String data = fakerRequest.request(invokeId, invokeExpression, poolSize, qps, loop, saveResult, resultParam);
        return Result.success(data);
    }

    @ApiOperation(value = "获取全部请求方法信息", httpMethod = "GET")
    @ApiResponse(code = 200, message = "success", response = Result.class)
    @ResponseBody
    @RequestMapping(value = "getAllInvoke", method = RequestMethod.GET)
    public List<SelectVO> getAllInvoke() {
        List<MethodInvokeDO> all = fakerManager.getAll();
        return all.stream()
                .map(item -> new SelectVO(item.getId().toString(),
                        item.getClassName() + ", " +
                                item.getMethodName() + ", " +
                                item.getParamType() + ", " +
                                item.getReturnType()
                ))
                .collect(Collectors.toList());
    }

    @ApiOperation(value = "获取全部项目", httpMethod = "GET")
    @ApiResponse(code = 200, message = "success", response = Result.class)
    @ResponseBody
    @RequestMapping(value = "getAllApp", method = RequestMethod.GET)
    public List<MethodInvokeDO> getAllApp() {
        return fakerManager.getAllApp();
    }

    @ApiOperation(value = "获取接口", httpMethod = "GET")
    @ApiResponse(code = 200, message = "success", response = Result.class)
    @ResponseBody
    @RequestMapping(value = "getClassByApp", method = RequestMethod.GET)
    public List<SelectVO> getClassByApp(@ApiParam(name = "appId", required = true, value = "项目编号") @RequestParam("appId") int appId) {
        List<String> classList = fakerManager.getClassByApp(appId);
        return classList.stream()
                .map(c -> new SelectVO(c, c))
                .collect(Collectors.toList());
    }

    @ApiOperation(value = "获取方法", httpMethod = "GET")
    @ApiResponse(code = 200, message = "success", response = Result.class)
    @ResponseBody
    @RequestMapping(value = "getMethodByClass", method = RequestMethod.GET)
    public List<MethodInvokeDO> getMethodByClass(@ApiParam(name = "className", required = true, value = "类名") @RequestParam("className") String className) {
        return fakerManager.getMethodByClass(className);
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
}