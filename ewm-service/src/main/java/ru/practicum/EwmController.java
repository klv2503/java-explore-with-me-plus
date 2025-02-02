package ru.practicum;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.controller.ClientController;

@RestController
@AllArgsConstructor
@RequestMapping(path = "/events")
public class EwmController {
    ClientController clientController;

    @GetMapping
    public void getEvents(HttpServletRequest request) {
        clientController.hit(request.getRemoteAddr(), request.getRequestURI());
    }
}
