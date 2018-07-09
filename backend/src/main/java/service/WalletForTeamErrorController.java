package service;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class WalletForTeamErrorController implements ErrorController {

    private static final String PATH = "/error";

    @CrossOrigin(origins = "${clientcors.url}")
    @RequestMapping(value=PATH)
    public String error() {
        return "index.html";
    }

    @Override
    public String getErrorPath() {
        return PATH;
    }
}
