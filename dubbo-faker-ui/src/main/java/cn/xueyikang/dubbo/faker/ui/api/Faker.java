package cn.xueyikang.dubbo.faker.ui.api;

import cn.xueyikang.dubbo.faker.core.request.FakerRequest;
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

import javax.xml.transform.Result;

/**
 * Created by xueyikang on 2017/12/22.
 */
@Api(value = "faker")
@Controller
@RequestMapping("/faker")
public class Faker {

    @Autowired
    private FakerRequest fakerRequest;

    @ApiOperation(value = "调用虚拟请求", httpMethod = "GET", produces = "application/json")
    @ApiResponse(code = 200, message = "success", response = Result.class)
    @ResponseBody
    @RequestMapping(value = "invoke", method = RequestMethod.GET, produces = "application/json")
    public void invoke(@ApiParam(name = "appId", required = true, value = "项目编号") @RequestParam("appId") int appId,
                         @ApiParam(name = "invokeId", required = true, value = "请求编号") @RequestParam("invokeId") int invokeId,
                         @ApiParam(name = "type", required = true, value = "参数类别") @RequestParam("type") String type
                                ) {
        fakerRequest.request(appId, invokeId, type);
    }
}