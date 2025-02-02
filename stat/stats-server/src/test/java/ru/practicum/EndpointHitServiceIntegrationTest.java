package ru.practicum;

import lombok.AllArgsConstructor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.dto.ReadEndpointHitDto;
import ru.practicum.service.EndpointHitService;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(properties = "classpath:application-test.properties")
@ExtendWith(SpringExtension.class)
@Transactional(readOnly = true)
public class EndpointHitServiceIntegrationTest {

    @Autowired
    private final EndpointHitService endpointHitService;

    @Autowired
    public EndpointHitServiceIntegrationTest(EndpointHitService endpointHitService) {
        this.endpointHitService = endpointHitService;
    }

    @Test
    public void getHitsWithoutUnique() {
        LocalDateTime start = LocalDateTime.of(2022, 9, 6, 10,0, 0);
        LocalDateTime end = LocalDateTime.of(2022, 9, 6, 12, 0,0);

        Collection<ReadEndpointHitDto> dtoList = endpointHitService.getHits(start, end,
                Optional.empty(), false);

        assertAll(
                () -> assertEquals(2, dtoList.size())
        );
    }

    @Test
    public void getHitsWithUnique() {

    }
}
