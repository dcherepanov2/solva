package kz.solva.tz.expense.tracker.api.integration.fixer.api;

import kz.solva.tz.expense.tracker.api.dto.enums.Currency;
import lombok.Getter;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

@Getter
public class TwelvedataRequest implements Request {

    private final String host;

    private final URI uri;

    private final String params;

    private TwelvedataRequest(Builder builder) {
        this.host = builder.host;
        this.uri = builder.uri;
        this.params = builder.param;
    }

    @Override
    public String getHttpUri() {
        return host + uri.getValue() + params;
    }

    private static class SymbolsParam {
        private final String paramName = "symbol=";
        private final Currency from;
        private final Currency to;

        public SymbolsParam(Currency from, Currency to) {
            this.from = from;
            this.to = to;
        }

        @Override
        public String toString() {
            return this.paramName + this.from.getValue() + "/" + this.to.getValue();
        }
    }

    private static class IntervalParam {

        private static final Map<String, Duration> INTERVALS = new HashMap<>();

        private final String paramName = "interval=";
        private final String duration;

        static {
            INTERVALS.put("1min", Duration.ofMinutes(1));
            INTERVALS.put("5min", Duration.ofMinutes(5));
            INTERVALS.put("10min", Duration.ofMinutes(10));
            INTERVALS.put("15min", Duration.ofMinutes(15));
            INTERVALS.put("30min", Duration.ofMinutes(30));
            INTERVALS.put("45min", Duration.ofMinutes(45));
            INTERVALS.put("1h", Duration.ofHours(1));
            INTERVALS.put("2h", Duration.ofHours(2));
            INTERVALS.put("4h", Duration.ofHours(4));
            INTERVALS.put("8h", Duration.ofHours(8));
            INTERVALS.put("1day", Duration.ofDays(1));
            INTERVALS.put("1week", Duration.ofDays(7));
            INTERVALS.put("1month", Duration.ofDays(30));
        }

        public IntervalParam(Duration duration) {
            this.duration = getIntervalString(duration);
        }

        private static String getIntervalString(Duration duration) {
            for (Map.Entry<String, Duration> entry : INTERVALS.entrySet()) {
                if (entry.getValue().equals(duration)) {
                    return entry.getKey();
                }
            }
            throw new IllegalArgumentException("");
        }


        @Override
        public String toString() {
            return this.paramName + this.duration;
        }
    }

    public static class Builder {

        private String host;

        private URI uri;

        private String param = "?";

        public Builder host(String host) {
            this.host = host;
            return this;
        }

        public Builder accessKey(String accessKey) {
            if (!uri.equals(URI.QUOTE))
                throw new IllegalArgumentException("");
            if (!this.param.equals("?")) {
                this.param = this.param.concat("&");
            }
            this.param = this.param + "apikey=" + accessKey;
            return this;
        }

        public Builder symbolParam(Currency base, Currency to) {
            if (!uri.equals(URI.QUOTE))
                throw new IllegalArgumentException("");
            if (!this.param.equals("?")) {
                this.param = this.param.concat("&");
            }
            SymbolsParam symbolsParam = new SymbolsParam(base,to);
            this.param = this.param + symbolsParam;
            return this;
        }

        public Builder interval(Duration duration) {
            if (!uri.equals(URI.QUOTE))
                throw new IllegalArgumentException("");
            if (!this.param.equals("?")) {
                this.param = this.param.concat("&");
            }
            IntervalParam intervalParam = new IntervalParam(duration);
            this.param = this.param + intervalParam;
            return this;
        }

        public Builder uri(URI uri) {
            this.uri = uri;
            return this;
        }

        public TwelvedataRequest build() {
            return new TwelvedataRequest(this);
        }
    }
}
