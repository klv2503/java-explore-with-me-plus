package ru.practicum.validation;

import ru.practicum.dto.TakeHitsDto;

import java.time.LocalDateTime;

public class PairOfDateValidation {

    public static boolean isValid(TakeHitsDto value) {
        if (value == null)
            return false;

        LocalDateTime firstDate = value.getStart();
        LocalDateTime secondDate = value.getEnd();

        //обе даты должны быть не null
        if (firstDate == null || secondDate == null)
            return false;

        //вторая дата должна быть позже первой
        return secondDate.isAfter(firstDate);

    }

}