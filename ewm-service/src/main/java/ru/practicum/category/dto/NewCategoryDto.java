package ru.practicum.category.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewCategoryDto {

    @NotNull(message = "Field: name. Error: must not be blank. Value: null")
    @NotBlank(message = "Field: name. Error: must not be blank. Value: empty")
    @Size(max = 50, message = "Field: name. Error: length must be not longer as 50 symbols")
    String name;
}
