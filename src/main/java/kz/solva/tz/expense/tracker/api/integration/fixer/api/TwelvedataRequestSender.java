package kz.solva.tz.expense.tracker.api.integration.fixer.api;

import kz.solva.tz.expense.tracker.api.integration.fixer.api.dto.QuoteResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service("twelvedataRequestSender")
public class TwelvedataRequestSender implements RequestSender{
    private final RestTemplate restTemplate;

    @Autowired
    public TwelvedataRequestSender(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public QuoteResponse getQuote(Request request) {
        return restTemplate.getForEntity(request.getHttpUri(), QuoteResponse.class).getBody();
    }
}
