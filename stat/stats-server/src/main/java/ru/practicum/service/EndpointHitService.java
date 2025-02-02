package ru.practicum.service;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.dto.CreateEndpointHitDto;
import ru.practicum.model.EndpointHit;
import ru.practicum.repository.EndpointHitRepository;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class EndpointHitService {
    private final EndpointHitRepository endpointHitRepository;

    @Transactional
    public void saveHit(CreateEndpointHitDto endpointHitDto) {
        EndpointHit endpointHit = new EndpointHit();
        endpointHit.setApp(endpointHitDto.getApp());
        endpointHit.setUri(endpointHitDto.getUri());
        endpointHit.setIp(endpointHitDto.getIp());
        endpointHit.setTimestamp(endpointHitDto.getTimestamp() != null 
                ? endpointHitDto.getTimestamp() 
                : LocalDateTime.now());

        endpointHitRepository.save(endpointHit);
    }
}
