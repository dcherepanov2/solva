package kz.solva.tz.expense.tracker.api.integration.fixer.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class FiftyTwoWeekData {
    private String low;
    private String high;

    @JsonProperty("low_change")
    private String lowChange;

    @JsonProperty("high_change")
    private String highChange;

    @JsonProperty("low_change_percent")
    private String lowChangePercent;

    @JsonProperty("high_change_percent")
    private String highChangePercent;

    private String range;

    // Getters and setters (or lombok annotations) here
}
