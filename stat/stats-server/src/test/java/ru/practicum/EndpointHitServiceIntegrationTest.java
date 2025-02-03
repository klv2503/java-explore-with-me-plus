package ru.practicum;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.controller.StatsController;
import ru.practicum.dto.ReadEndpointHitDto;
import ru.practicum.service.EndpointHitService;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(properties = "classpath:application-test.properties")
@ExtendWith(SpringExtension.class)
@Transactional(readOnly = true)
@Slf4j
public class EndpointHitServiceIntegrationTest {

    @Autowired
    private final EndpointHitService endpointHitService;

    LocalDateTime start = LocalDateTime.of(2022, 9, 6, 10, 0, 0);
    LocalDateTime end = LocalDateTime.of(2022, 9, 6, 12, 0, 0);

    @Autowired
    public EndpointHitServiceIntegrationTest(EndpointHitService endpointHitService, StatsController statsController) {
        this.endpointHitService = endpointHitService;
    }

    @Test
    public void getHitsWithoutUniqueAndUris() {
        Collection<ReadEndpointHitDto> dtoList = endpointHitService.getHits(start, end,
                Optional.empty(), false);

        assertAll(
                () -> assertEquals(3, dtoList.size())
        );
    }

    @Test
    public void getHitsWithUnique() {
        Collection<ReadEndpointHitDto> dtoList = endpointHitService.getHits(start, end,
                Optional.empty(), true);

        assertAll(
                () -> assertEquals(3, dtoList.size())
        );
    }

    @Test
    public void getHitsWithUris() {
        List<String> uris = List.of("/events");

        Collection<ReadEndpointHitDto> dtoList = endpointHitService.getHits(start, end,
                Optional.of(uris), false);

        assertAll(
                () -> assertEquals(1, dtoList.size())
        );
    }

    @Test
    public void getHitsWithUrisAndUnique() {
        List<String> uris = List.of("/events/1");

        Collection<ReadEndpointHitDto> dtoList = endpointHitService.getHits(start, end,
                Optional.of(uris), true);

        assertAll(
                () -> assertEquals(1, dtoList.size()),
                () -> assertEquals(1, dtoList.stream().toList().getFirst().getHits())
        );
    }
}
