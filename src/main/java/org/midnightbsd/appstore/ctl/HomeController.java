package org.midnightbsd.appstore.ctl;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import jakarta.servlet.http.HttpSession;

/**
 * @author Lucas Holt
 */
@Controller
@RequestMapping("/")
public final class HomeController {

    @GetMapping
    public String home(Model model, HttpSession session) {

        return "index";
    }
}
