package org.student.auth;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MyController {

    @GetMapping("/secured")
    public String secured() {
        return "This is a secured endpoint. You need to be authenticated to see this.";
    }
}
