package kz.solva.tz.expense.tracker.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import kz.solva.tz.expense.tracker.api.dto.enums.Currency;
import kz.solva.tz.expense.tracker.api.dto.enums.PaymentsCategory;
import lombok.Data;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.math.BigDecimal;

@Data
public class LimitReqest {

    @JsonProperty("currency")
    @NotBlank(message = "Currency is required")
    @Size(min = 3,max = 3, message = "Currency code must be 3 characters long")
    private Currency currency;

    @JsonProperty("accountNumber")
    @NotBlank(message = "Account number is required")
    @Size(min = 10,max = 10, message = "Account Number code must be 10 characters long")
    private String accountNumber;

    @JsonProperty("paymentCategory")
    @NotBlank(message = "Category is required")
    @Size(min = 3,max = 3, message = "Category code must be 3 characters long")
    private PaymentsCategory paymentCategory;

    @DecimalMin(value = "0.00", inclusive = false, message = "Limit must be greater than 0")
    @DecimalMax(value = "999999999999999999.99", message = "Limit cannot exceed 999999999999999999.99")
    @NotBlank(message = "Limit is required")
    private BigDecimal limit;
}
