package ru.practicum;

import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.controller.ClientController;

@ExtendWith(MockitoExtension.class)
class EwmControllerTest {

    @Mock
    private ClientController clientController;

    @InjectMocks
    private EwmController ewmController;

    @Test
    void testGetEvents() {
        HttpServletRequest mockRequest = Mockito.mock(HttpServletRequest.class);
        Mockito.when(mockRequest.getRemoteAddr()).thenReturn("192.168.1.1");
        Mockito.when(mockRequest.getRequestURI()).thenReturn("/events");

        ewmController.getEvents(mockRequest);

        Mockito.verify(clientController).hit("192.168.1.1", "/events");
    }
}


