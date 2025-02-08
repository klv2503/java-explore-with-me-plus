package ru.practicum.compilations.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.compilations.dto.UpdateCompilationRequest;
import ru.practicum.compilations.model.Compilation;

@Component
public class UpdateCompilationRequestToCompilation {
    public static Compilation mapUpdateCompilationRequestToCompilation(UpdateCompilationRequest updateCompilationRequest,
                                                                       Long id) {
        return new Compilation(
                id,
                updateCompilationRequest.getEvents(),
                updateCompilationRequest.getPinned(),
                updateCompilationRequest.getTitle());
    }
}
