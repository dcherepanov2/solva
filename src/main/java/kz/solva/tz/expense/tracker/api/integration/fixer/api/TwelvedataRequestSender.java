package kz.solva.tz.expense.tracker.api.integration.fixer.api;

import kz.solva.tz.expense.tracker.api.integration.fixer.api.dto.QuoteResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class TwelvedataRequestSender implements RequestSender<QuoteResponse>{
    private final RestTemplate restTemplate;

    @Autowired
    public TwelvedataRequestSender(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public QuoteResponse get(Request request) {
        return restTemplate.getForEntity(request.getHttpUri(), QuoteResponse.class).getBody();
    }
}
