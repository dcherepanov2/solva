package kz.solva.tz.expense.tracker.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Map;

@AllArgsConstructor
public class ErrorValidateFieldDto {
    @Getter
    @Schema(description = "Статус ошибки")
    private final Integer status;

    @Getter
    @Schema(description = "Все ошибки, которые могут произойти в ходе валидации запроса")
    private final Map<String, String> errors;
}
