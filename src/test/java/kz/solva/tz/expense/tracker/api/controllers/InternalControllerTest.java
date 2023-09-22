package kz.solva.tz.expense.tracker.api.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import kz.solva.tz.expense.tracker.api.dto.LimitReqest;
import kz.solva.tz.expense.tracker.api.dto.TransactionRequest;
import kz.solva.tz.expense.tracker.api.dto.TransactionResponse;
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
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@AutoConfigureMockMvc
@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestPropertySource("/application-test.properties")
class InternalControllerTest {
    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;
    private final ClientController clientController;
    private final String apiUri = "/internal";
    private final String completeTransaction = "/complete-transaction";

    @Autowired
    public InternalControllerTest(MockMvc mockMvc, ObjectMapper objectMapper, ClientController clientController) {
        this.mockMvc = mockMvc;
        this.objectMapper = objectMapper;
        this.clientController = clientController;
    }

    @Order(1)
    @Test
    void completeTransaction() throws Exception {
        String amount = "200000.00";
        BigDecimal startBalanceFrom = new BigDecimal("10000000.00");
        BigDecimal startBalanceTo = new BigDecimal("100000.00");
        String accountFrom = "1234567890";
        String accountTo = "1234567891";
        PaymentsCategory category = PaymentsCategory.PRODUCT;

        TransactionRequest request = new TransactionRequest();
        request.setAccountFrom(accountFrom);
        request.setAccountTo(accountTo);
        request.setSum(new BigDecimal(amount));
        request.setExpenseCategory(category);

        JSONObject jsonObject = sendRequestTransaction(request, MockMvcResultMatchers.status().isOk());
        String accountFromResponse = jsonObject.getString("accountFrom");
        String accountFromBalanceResponseStr = new JSONObject(accountFromResponse).getString("balance");
        BigDecimal accountFromBalanceResponse = new BigDecimal(accountFromBalanceResponseStr).setScale(2, RoundingMode.HALF_UP);

        assertEquals(startBalanceFrom.subtract(new BigDecimal(amount)), accountFromBalanceResponse);

        BigDecimal conversionSum = new BigDecimal(new JSONArray(jsonObject.getString("conversions")).getJSONObject(1).getString("conversionSum"));

        String accountToResponse = jsonObject.getString("accountTo");
        String accountToBalanceResponseStr = new JSONObject(accountToResponse).getString("balance");
        BigDecimal accountToBalanceResponse = new BigDecimal(accountToBalanceResponseStr).setScale(2, RoundingMode.HALF_UP);

        assertEquals(startBalanceTo.add(conversionSum), accountToBalanceResponse);
    }

    @Order(2)
    @Test
    void checkLimitExceededFlagIsTrue() throws Exception {
        String amount = "200000.00";
        String accountFrom = "1234567890";
        String accountTo = "1234567891";
        PaymentsCategory category = PaymentsCategory.PRODUCT;

        TransactionRequest request = new TransactionRequest();
        request.setAccountFrom(accountFrom);
        request.setAccountTo(accountTo);
        request.setSum(new BigDecimal(amount));
        request.setExpenseCategory(category);

        sendRequestTransaction(request, MockMvcResultMatchers.status().isOk());
        sendRequestTransaction(request, MockMvcResultMatchers.status().is4xxClientError());
        Pageable page = PageRequest.of(0, 20);

        List<TransactionResponse> transactions = clientController.getTransactions(page, accountFrom, true);

        boolean flagIsTrue = transactions.stream().map(x -> x.getLimitExceeded().getFlag())
                .anyMatch(x -> x.equals(true));

        assertTrue(flagIsTrue);
    }

    @Order(3)
    @Test
    void setNewLimitAndRepeatFailedTransaction() throws Exception {
        String amount = "200000.00";
        String accountFrom = "1234567890";
        String accountTo = "1234567891";
        PaymentsCategory category = PaymentsCategory.PRODUCT;

        TransactionRequest request = new TransactionRequest();
        request.setAccountFrom(accountFrom);
        request.setAccountTo(accountTo);
        request.setSum(new BigDecimal(amount));
        request.setExpenseCategory(category);

        LimitReqest limitReqest = new LimitReqest();
        limitReqest.setLimit(new BigDecimal("2000.00"));
        limitReqest.setPaymentCategory(category);
        limitReqest.setAccountNumber(accountFrom);
        limitReqest.setCurrency(Currency.USD);

        clientController.setNewLimit(limitReqest);
        JSONObject responseBody = sendRequestTransaction(request, MockMvcResultMatchers.status().isOk());

        JSONObject limitExceeded = new JSONObject(responseBody.getString("limitExceeded"));
        boolean flag = Boolean.parseBoolean(limitExceeded.getString("flag"));

        assertFalse(flag);
    }

    JSONObject sendRequestTransaction(TransactionRequest request, ResultMatcher check) throws Exception {

        String requestBody = objectMapper.writeValueAsString(request);

        ResultActions perform = mockMvc.perform(MockMvcRequestBuilders.post(apiUri + completeTransaction)
                        .contentType(MediaType.APPLICATION_JSON).content(requestBody))
                .andExpect(check);

        String responseBody = perform.andReturn().getResponse().getContentAsString();

        return new JSONObject(responseBody);
    }
}
