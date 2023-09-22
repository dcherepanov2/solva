package kz.solva.tz.expense.tracker.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import kz.solva.tz.expense.tracker.api.dto.enums.PaymentsCategory;
import lombok.Data;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.math.BigDecimal;

@Data
public class TransactionRequest {
    @JsonProperty("accountFrom")
    @NotBlank(message = "Account number is required")
    @Size(min = 10,max = 10, message = "Account Number code must be 10 characters long")
    private String accountFrom;

    @JsonProperty("accountTo")
    @NotBlank(message = "Account number is required")
    @Size(min = 10,max = 10, message = "Account Number code must be 10 characters long")
    private String accountTo;

    @JsonProperty("sum")
    @DecimalMin(value = "0.00", inclusive = false, message = "Sum must be greater than 0")
    @DecimalMax(value = "999999999999999999.99", message = "Sum cannot exceed 999999999999999999.99")
    @NotBlank(message = "Sum is required")
    private BigDecimal sum;

    @JsonProperty("paymentCategory")
    @NotBlank(message = "Category is required")
    @Size(min = 3,max = 3, message = "Category code must be 3 characters long")
    private PaymentsCategory expenseCategory;
}
