package picto.com.usermanager.domain.user.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserManagerHtmlController {
    @GetMapping("user-manager/index")
    public String index() {
        return "index";
    }

    @GetMapping("user-manager/signin")
    public String signin() {
        return "signin";
    }

    @GetMapping("user-manager/signup")
    public String signup() {
        return "signup";
    }

    @GetMapping("user-manager/public")
    public String publicPage() {
        return "public";
    }
}