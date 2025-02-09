package ru.practicum.compilations.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.compilations.dto.CompilationDto;
import ru.practicum.compilations.dto.Filter;
import ru.practicum.compilations.dto.NewCompilationDto;
import ru.practicum.compilations.dto.UpdateCompilationRequest;
import ru.practicum.compilations.model.Compilation;
import ru.practicum.compilations.repository.CompilationRepository;
import ru.practicum.events.dto.EventShortDto;
import ru.practicum.events.mapper.EventMapper;
import ru.practicum.events.repository.EventRepository;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static ru.practicum.compilations.mapper.CompilationToCompilationDto.mapCompilationToCompilationDto;
import static ru.practicum.compilations.mapper.CompilationToCompilationDto.mapToListCompilationDto;
import static ru.practicum.compilations.mapper.NewCompilationDtoToCompilationMapper.mapNewCompilationDtoToCompilation;

@Service
@Slf4j
@RequiredArgsConstructor
public class CompilationServiceImpl implements CompilationService {

    private final CompilationRepository compilationRepository;
    private final EventRepository eventRepository;

    @Override
    public CompilationDto getById(Long compId) {
        log.info("Get compilation with id {}", compId);
        Compilation compilation = compilationRepository.findById(compId)
                .orElseThrow(() -> new EntityNotFoundException("Compilation with id=" + compId +
                        " was not found"));
        return mapCompilationToCompilationDto(compilation, getEventsListForDto(compilation));
    }

    @Override
    public CompilationDto add(NewCompilationDto newCompilationDto) {
        log.info("Add compilation {}", newCompilationDto);
        Compilation newCompilation = compilationRepository
                .save(mapNewCompilationDtoToCompilation(newCompilationDto));
        return mapCompilationToCompilationDto(newCompilation, getEventsListForDto(newCompilation));
    }

    @Override
    public CompilationDto update(Long compId, UpdateCompilationRequest updateCompilationRequest) {
        log.info("Update compilation with id {}", compId);
        Compilation compilation = getCompilation(compId);

        if (updateCompilationRequest.getEvents() != null) {
            compilation.setEvents(updateCompilationRequest.getEvents());
        }

        if (updateCompilationRequest.getPinned() != null) {
            compilation.setPinned(updateCompilationRequest.getPinned());
        }

        if (updateCompilationRequest.getTitle() != null) {
            compilation.setTitle(updateCompilationRequest.getTitle());
        }
        Compilation updateCompilation = compilationRepository.save(compilation);
        return mapCompilationToCompilationDto(updateCompilation, getEventsListForDto(updateCompilation));
    }

    @Override
    public void delete(Long compId) {
        log.info("Delete compilation with id {}", compId);
        if (!compilationRepository.existsById(compId)) {
            throw new EntityNotFoundException("Compilation with id=" + compId +
                    " was not found");
        }

        compilationRepository.deleteById(compId);
    }

    @Override
    public List<CompilationDto> get(Filter params) {
        log.info("Get compilations with filter {}", params);
        int page = params.getFrom() / params.getSize();
        Pageable pageable = PageRequest.of(page, params.getSize());
        Page<Compilation> response = params.getPinned() ? compilationRepository.findAllByPinnedTrue(pageable) :
                compilationRepository.findAll(pageable);
        List<Compilation> compilations = response.getContent().stream().toList();

        List<EventShortDto> eventShortDtoList = getEventShortDtoForListDto(compilations);

        return mapToListCompilationDto(compilations, eventShortDtoList);
    }


    //вспомогательные методы
    private Compilation getCompilation(Long compId) {
        return compilationRepository.findById(compId)
                .orElseThrow(() -> new EntityNotFoundException("Compilation with id=" + compId +
                        " was not found"));
    }

    private List<EventShortDto> getEventShortDtoForListDto(List<Compilation> compilations) {
        Set<Long> eventsId = compilations.stream()
                .map(compilation -> compilation.getEvents().stream().toList())
                .flatMap(List::stream)
                .collect(Collectors.toSet());

        List<EventShortDto> eventShortDtoList = eventRepository.findAllById(eventsId).stream()
                .map(EventMapper::toEventShortDto)
                .toList();
        return eventShortDtoList;
    }

    private List<EventShortDto> getEventsListForDto(Compilation compilation) {
        return eventRepository.findAllById(compilation.getEvents().stream().toList())
                .stream().map(EventMapper::toEventShortDto).toList();
    }

}
