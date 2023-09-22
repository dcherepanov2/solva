package kz.solva.tz.expense.tracker.api.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import kz.solva.tz.expense.tracker.api.dto.LimitReqest;
import kz.solva.tz.expense.tracker.api.dto.enums.Currency;
import kz.solva.tz.expense.tracker.api.dto.enums.PaymentsCategory;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestPropertySource("/application-test.properties")
class ClientControllerTest {
    private final MockMvc mockMvc;

    private final ObjectMapper objectMapper;

    private final String apiUri = "/api/v1/client";

    private final String setNewLimitUri = "/set-new-limit";

    private final String getTransactionsUri = "/transactions";

    private final String exchangeRatesUri = "/exchange-rates";

    @Autowired
    public ClientControllerTest(MockMvc mockMvc, ObjectMapper objectMapper) {
        this.mockMvc = mockMvc;
        this.objectMapper = objectMapper;
    }

    @Order(1)
    @Test
    void setNewLimitRequest() throws Exception {
        LimitReqest limitReqest = new LimitReqest();
        limitReqest.setLimit(new BigDecimal("4000.00"));
        limitReqest.setCurrency(Currency.USD);
        limitReqest.setPaymentCategory(PaymentsCategory.PRODUCT);
        limitReqest.setAccountNumber("1234567890");
        String requestBody = objectMapper.writeValueAsString(limitReqest);
        ResultActions perform = mockMvc.perform(MockMvcRequestBuilders.post(apiUri + setNewLimitUri)
                        .contentType(MediaType.APPLICATION_JSON).content(requestBody))
                .andExpect(MockMvcResultMatchers.status().isOk());

        String responseBody = perform.andReturn().getResponse().getContentAsString();

        JSONObject jsonObject = new JSONObject(responseBody);
        String limitAmount = jsonObject.getString("limitAmount");
        String limitReqestAmount = limitReqest.getLimit().setScale(1, RoundingMode.HALF_UP).toString();

        assertEquals(limitReqestAmount, limitAmount);
    }

    @Order(2)
    @Test
    void returnOldLimit() throws Exception {
        LimitReqest limitReqest = new LimitReqest();
        limitReqest.setLimit(new BigDecimal("1000.00"));
        limitReqest.setCurrency(Currency.USD);
        limitReqest.setPaymentCategory(PaymentsCategory.PRODUCT);
        limitReqest.setAccountNumber("1234567890");
        String requestBody = objectMapper.writeValueAsString(limitReqest);
        ResultActions perform = mockMvc.perform(MockMvcRequestBuilders.post(apiUri + setNewLimitUri)
                        .contentType(MediaType.APPLICATION_JSON).content(requestBody))
                .andExpect(MockMvcResultMatchers.status().isOk());

        String responseBody = perform.andReturn().getResponse().getContentAsString();

        JSONObject jsonObject = new JSONObject(responseBody);
        String limitAmount = jsonObject.getString("limitAmount");
        String limitReqestAmount = limitReqest.getLimit().setScale(1, RoundingMode.HALF_UP).toString();

        assertEquals(limitReqestAmount, limitAmount);
    }

    @Order(2)
    @Test
    void getTransactionsTest() throws Exception {
        String accountFrom = "1234567892";
        Boolean limitExceedRequest = true;
        String page = "0";
        String size = "20";

        JSONArray jsonArray = getTransactions(accountFrom, page, size, MockMvcResultMatchers.status().isOk());

        JSONObject jsonObject = jsonArray.getJSONObject(0);
        String limitExceed = jsonObject.getString("limitExceeded");

        JSONObject limitExceededJson = new JSONObject(limitExceed);
        String flagFromLimitExceededJson = limitExceededJson.getString("flag");

        assertEquals(limitExceedRequest.toString(), flagFromLimitExceededJson);
    }


    @Order(2)
    @Test
    void exchangeRatesTest() throws Exception {
        String baseCurrency = "USD";
        String toCurrency = "KZT";
        String date = "20.09.2023";

        StringBuilder params = new StringBuilder();
        params.append("?base=").append(baseCurrency);
        params.append("&");
        params.append("to=").append(toCurrency);
        params.append("&");
        params.append("date=").append(date);

        ResultActions perform = mockMvc.perform(MockMvcRequestBuilders.get(apiUri + exchangeRatesUri + params))
                .andExpect(MockMvcResultMatchers.status().isOk());

        String responseBody = perform.andReturn().getResponse().getContentAsString();

        JSONObject jsonObject = new JSONObject(responseBody);
        String limitAmount = jsonObject.getString("rate");

        assertEquals("473.56", limitAmount);
    }

    JSONArray getTransactions(String accountFrom, String page, String size, ResultMatcher result) throws Exception {
        Boolean limitExceedRequest = true;
        StringBuilder params = new StringBuilder();
        params.append("?accountFrom=").append(accountFrom);
        params.append("&");
        params.append("limit_exceed=").append(limitExceedRequest);
        params.append("&");
        params.append("page=").append(page);
        params.append("&");
        params.append("size=").append(size);
        ResultActions perform = mockMvc.perform(MockMvcRequestBuilders.get(apiUri + getTransactionsUri + params))
                .andExpect(result);

        String responseBody = perform.andReturn().getResponse().getContentAsString();

        return new JSONArray(responseBody);
    }

}
