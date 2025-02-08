package ru.practicum.compilations.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.compilations.dto.Filter;
import ru.practicum.compilations.dto.NewCompilationDto;
import ru.practicum.compilations.dto.UpdateCompilationRequest;
import ru.practicum.compilations.model.Compilation;
import ru.practicum.compilations.repository.CompilationRepository;

import java.util.List;

import static ru.practicum.compilations.mapper.NewCompilationDtoToCompilationMapper.mapNewCompilationDtoToCompilation;

@Service
@Slf4j
@RequiredArgsConstructor
public class CompilationServiceImpl implements CompilationService {

    private final CompilationRepository compilationRepository;

    @Override
    public Compilation getById(Long compId) {
        log.info("Get compilation with id {}", compId);
        return compilationRepository.findById(compId)
                .orElseThrow(() -> new EntityNotFoundException("Compilation with id=" + compId +
                        " was not found"));
    }

    @Override
    public Compilation add(NewCompilationDto newCompilationDto) {
        log.info("Add compilation {}", newCompilationDto);
        return compilationRepository.
                save(mapNewCompilationDtoToCompilation(newCompilationDto));
    }

    @Override
    public Compilation update(Long compId, UpdateCompilationRequest updateCompilationRequest) {
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

        return compilationRepository.save(compilation);
    }

    @Override
    public void delete(Long compId) {
        log.info("Delete compilation with id {}", compId);
        if(!compilationRepository.existsById(compId)) {
            throw new EntityNotFoundException("Compilation with id=" + compId +
                    " was not found");
        }

        compilationRepository.deleteById(compId);
    }

    @Override
    public List<Compilation> get(Filter params) {
        log.info("Get compilations with filter {}", params);
        int page = params.getFrom() / params.getSize();
        Pageable pageable = PageRequest.of(page, params.getSize());
        Page<Compilation> response = params.getPinned() ? compilationRepository.findAllByPinnedTrue(pageable) :
                compilationRepository.findAll(pageable);
        List<Compilation> compilations = response.getContent().stream().toList();
        return compilations;
    }

    //вспомогательные методы
    private Compilation getCompilation(Long compId) {
        return compilationRepository.findById(compId)
                .orElseThrow(() -> new EntityNotFoundException("Compilation with id=" + compId +
                        " was not found"));
    }
}
