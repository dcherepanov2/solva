package kz.solva.tz.expense.tracker.api.integration.fixer.api;

import kz.solva.tz.expense.tracker.api.integration.fixer.api.dto.QuoteResponse;

public interface RequestSender {
    QuoteResponse getQuote(Request request);
}
