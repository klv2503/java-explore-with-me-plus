package ru.practicum.compilations.service;

import ru.practicum.compilations.dto.Filter;
import ru.practicum.compilations.dto.NewCompilationDto;
import ru.practicum.compilations.dto.UpdateCompilationRequest;
import ru.practicum.compilations.model.Compilation;

import java.util.List;

public interface CompilationService {
    Compilation getById(Long compId);

    Compilation add(NewCompilationDto newCompilationDto);

    Compilation update(Long compId, UpdateCompilationRequest updateCompilationRequest);

    void delete(Long compId);

    List<Compilation> get(Filter params);
}
