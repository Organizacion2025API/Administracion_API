package org.esfe.ApiApexManagent.controladores;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/status")
@Tag(name = "status", description = "pruebas de estado del sistema")
public class StatusController {

    @RequestMapping
    public String status() {
        return "Api corrienddo..";
    }
    
}
