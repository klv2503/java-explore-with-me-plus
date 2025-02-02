package ru.practicum.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.dto.CreateEndpointHitDto;
import ru.practicum.dto.ReadEndpointHitDto;
import ru.practicum.model.EndpointHit;
import ru.practicum.repository.EndpointHitRepository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class EndpointHitService {

    private final EndpointHitRepository endpointHitRepository;

    @Autowired
    public EndpointHitService(EndpointHitRepository endpointHitRepository) {
        this.endpointHitRepository = endpointHitRepository;
    }

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

    public Collection<ReadEndpointHitDto> getHits(LocalDateTime start, LocalDateTime end,
                                                                  List<String> uris, boolean unique) {

    return endpointHitRepository.get(start, end, uris, unique);
    }
}
