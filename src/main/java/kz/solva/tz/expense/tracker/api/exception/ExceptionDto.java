package kz.solva.tz.expense.tracker.api.exception;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ExceptionDto {

    @JsonProperty("status")
    private String status;


    @JsonProperty("message")
    private String message;

}
