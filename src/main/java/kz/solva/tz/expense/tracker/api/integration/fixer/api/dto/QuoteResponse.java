package kz.solva.tz.expense.tracker.api.integration.fixer.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class QuoteResponse {
    private String symbol;
    private String name;
    private String exchange;
    private String datetime;
    private long timestamp;
    private String open;
    private String high;
    private String low;
    private String close;
    private Error error;

    @JsonProperty("previous_close")
    private String previousClose;

    private String change;

    @JsonProperty("percent_change")
    private String percentChange;

    @JsonProperty("average_volume")
    private String averageVolume;

    @JsonProperty("is_market_open")
    private boolean isMarketOpen;

    @JsonProperty("fifty_two_week")
    private FiftyTwoWeekData fiftyTwoWeekData;

}
