package ru.practicum.users.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserShortDto {

    @NotNull(message = "Field: name. Error: must not be blank. Value: null")
    @NotBlank(message = "Field: name. Error: must not be blank. Value: empty")
    @Size(min = 2, max = 200, message = "Field: name. Error: length must be between 2 and 200 symbols")
    private String name;

    @Email(message = "Field: email. Error: must be correct email.")
    @NotBlank(message = "Field: email. Error: must not be blank. Value: empty")
    @Size(max = 50, message = "Field: email. Error: too long email, must be not longer as 50 symbols.")
    private String email;

}
