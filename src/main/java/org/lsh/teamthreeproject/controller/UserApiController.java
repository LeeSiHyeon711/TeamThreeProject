package org.lsh.teamthreeproject.controller;

import jakarta.servlet.http.HttpSession;
import org.lsh.teamthreeproject.dto.UserDTO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
public class UserApiController {
    @GetMapping("/current")
    public UserDTO getCurrentUser(HttpSession session) {
        return (UserDTO) session.getAttribute("user");
    }
}
