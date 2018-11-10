package cn.moyada.sharingan.manager.view;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;

/**
 * @author xueyikang
 * @since 1.0
 **/
@RestController
public class IndexController {

    @GetMapping("/")
    public String home(ServerWebExchange exchange) {
        //forward:index为相对路径，forward:/index为绝对路径，和servlet forward类似
        return "forward:index";
    }
}
