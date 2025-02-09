package ru.practicum.compilations.mapper;

import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.compilations.dto.CompilationDto;
import ru.practicum.compilations.model.Compilation;
import ru.practicum.events.dto.EventShortDto;

import java.util.List;

@Component
@NoArgsConstructor
public class CompilationToCompilationDto {
    public static CompilationDto mapCompilationToCompilationDto(Compilation compilation, List<EventShortDto> events) {
        return new CompilationDto(
                events,
                compilation.getId(),
                compilation.getPinned(),
                compilation.getTitle());
    }

    public static List<CompilationDto> mapToListCompilationDto(List<Compilation> compilationList,
                                                               List<EventShortDto> events) {
        return compilationList.stream()
                .map(compilation -> mapCompilationToCompilationDto(compilation,
                        getEvents(compilation.getEvents().stream().toList(), events)))
                .toList();
    }

    //вспомогательные методы

    private static List<EventShortDto> getEvents(List<Long> ids, List<EventShortDto> eventShortDtoList) {
        return eventShortDtoList.stream()
                .filter(eventShortDto -> ids.contains(eventShortDto.getId()))
                .toList();
    }
}
