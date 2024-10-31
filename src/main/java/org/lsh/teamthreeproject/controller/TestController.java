package org.lsh.teamthreeproject.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TestController {
    @GetMapping("/basic")
    public String basic() {
        return "layout/basic";
    }
}
