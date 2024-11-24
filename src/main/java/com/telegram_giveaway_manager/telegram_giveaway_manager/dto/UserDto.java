package com.telegram_giveaway_manager.telegram_giveaway_manager.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.validation.annotation.Validated;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Validated
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDto {
    @JsonProperty("chat_id")
    private Long chatId;
    @NotNull(message = "firstName can not be null!")
    @NotEmpty(message = "firstName can not be empty!")
    private String firstName;
    @NotNull(message = "lastName can not be null!")
    @NotEmpty(message = "lastName can not be empty!")
    private String lastName;
    @NotEmpty(message = "Phone number can not be empty!")
    private String phone;
    @NotEmpty(message = "Instagram ID number can not be empty!")
    private String instagramId;
    @NotEmpty(message = "Ticket number can not be empty!")
    private Long ticketNumber;
    @NotNull(message = "success cannot be null")
    private String success;
}
