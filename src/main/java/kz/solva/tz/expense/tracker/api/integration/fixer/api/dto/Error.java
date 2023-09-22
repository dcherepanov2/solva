package kz.solva.tz.expense.tracker.api.integration.fixer.api.dto;

import lombok.Data;

@Data
public class Error {
    private int code;
    private String message;

}
