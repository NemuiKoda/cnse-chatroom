package hskl.cnse.chat.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Controller
public class HomeController {

    //@GetMapping("/")
    public String home() {
        return "index";
    }

    @GetMapping("/secured")
    public String secured() {
        return "chat";
    }
}
