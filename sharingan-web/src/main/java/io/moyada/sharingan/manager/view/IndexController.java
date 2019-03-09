package io.moyada.sharingan.manager.view;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

/**
 * @author xueyikang
 * @since 1.0
 **/
@Controller
public class IndexController {

    @GetMapping("/")
    public RedirectView home(RedirectAttributes attributes) {
        return new RedirectView("index.html");
    }
}
