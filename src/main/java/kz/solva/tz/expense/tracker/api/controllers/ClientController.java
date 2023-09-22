package kz.solva.tz.expense.tracker.api.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import kz.solva.tz.expense.tracker.api.data.CurrencyExchangeRateEntity;
import kz.solva.tz.expense.tracker.api.data.ExpenseLimit;
import kz.solva.tz.expense.tracker.api.data.ExpenseLimitResponse;
import kz.solva.tz.expense.tracker.api.dto.*;
import kz.solva.tz.expense.tracker.api.dto.enums.Currency;
import kz.solva.tz.expense.tracker.api.exception.AccountNotFoundException;
import kz.solva.tz.expense.tracker.api.exception.ExceptionDto;
import kz.solva.tz.expense.tracker.api.exception.TwelvedataApiException;
import kz.solva.tz.expense.tracker.api.service.CurrencyRateTrackerService;
import kz.solva.tz.expense.tracker.api.service.LimitService;
import kz.solva.tz.expense.tracker.api.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

//TODO: сделать docker-compose файлик

//TODO: проверить все ошибки на правильный путь расположения


@Tag(name = "API для внешних запросов от клиента")
@RestController
@Validated
@RequestMapping("/api/v1/client")
public class ClientController {
    private final CurrencyRateTrackerService currencyRateTrackerServiceImpl;

    @Value("${client.transaction.max.size}")
    private Integer maxPageSize;

    @Qualifier("limitServiceImpl")
    private final LimitService limitServiceImpl;

    @Qualifier("transactionServiceImpl")
    private final TransactionService<TransactionRequest> transactionServiceImpl;

    @Autowired
    public ClientController(CurrencyRateTrackerService currencyRateTrackerServiceImpl, LimitService limitServiceImpl, TransactionService<TransactionRequest> transactionServiceImpl) {
        this.currencyRateTrackerServiceImpl = currencyRateTrackerServiceImpl;
        this.limitServiceImpl = limitServiceImpl;
        this.transactionServiceImpl = transactionServiceImpl;
    }

    @Operation(summary = "Эндпоинт для получения информации о курсе валютных пар")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Запрос успешно выполнен",
                    content = @Content(schema = @Schema(implementation = CurrencyExchangeRateResponse.class))),
            @ApiResponse(responseCode = "400",
                    description = "В случае, если пользователь не передал один обязательных из параметров)",
                    content = @Content(schema = @Schema(implementation = ExceptionDto.class))),
            @ApiResponse(responseCode = "500",
                    description = "Внутренняя ошибка сервера",
                    content = @Content(schema = @Schema(implementation = ExceptionDto.class)))
    })
    @GetMapping("/exchange-rates")
    public CurrencyExchangeRateResponse exchangeRates(@Parameter(description = "Базовая валюта", required = true)
                                                      @RequestParam("base") Currency baseCurrency,
                                                      @Parameter(description = "Валюта конвертации по отношению к единице базовой валюты", required = true)
                                                      @RequestParam("to") Currency toCurrency,
                                                      @Parameter(description = "Дата конвертации", required = true)
                                                      @RequestParam("date") @DateTimeFormat(pattern = "dd.MM.yyyy") LocalDate date) throws TwelvedataApiException {
        CurrencyExchangeRateEntity currencyExchangeEntity = currencyRateTrackerServiceImpl.getCurrencyExchangeRate(baseCurrency, toCurrency, date);
        return new CurrencyExchangeRateResponse(currencyExchangeEntity);
    }

    @Operation(summary = "Эндпоинт для установки нового лимита на счет")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Запрос успешно выполнен",
                    content = @Content(schema = @Schema(implementation = ExpenseLimitResponse.class))),
            @ApiResponse(responseCode = "400",
                    description = "В случае, если пользователь не передал один из обязательных параметров в теле запроса)",
                    content = @Content(schema = @Schema(implementation = ExceptionDto.class))),
            @ApiResponse(responseCode = "404",
                    description = "В случае если аккаунт не найден",
                    content = @Content(schema = @Schema(implementation = ExceptionDto.class)))
    })
    @PostMapping("/set-new-limit")
    public ExpenseLimitResponse setNewLimit(@RequestBody @Valid LimitReqest request) throws AccountNotFoundException {
        ExpenseLimit expenseLimit = limitServiceImpl.setNewLimit(request);
        return new ExpenseLimitResponse(expenseLimit);
    }

    @Operation(summary = "Эндпоинт для получения информации о транзакциях счета")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Запрос успешно выполнен",
                    content = @Content(schema = @Schema(implementation = TransactionResponse[].class))),
            @ApiResponse(responseCode = "400",
                    description = "В случае, если пользователь не передал один обязательных из параметров)",
                    content = @Content(schema = @Schema(implementation = ExceptionDto.class))),
            @ApiResponse(responseCode = "500",
                    description = "Внутренняя ошибка сервера",
                    content = @Content(schema = @Schema(implementation = ExceptionDto.class)))
    })
    @GetMapping("/transactions")
    @Transactional
    public List<TransactionResponse> getTransactions(@Parameter(description = "Параметр size и page." +
                                                                              " Отображают получаемое кол-во" +
                                                                              " транзакций в ответе и страницу соответственно." +
                                                                              " Должны быть больше нуля. Если кол-во указано" +
                                                                              " больше 20, то кол-во транзакций автоматически выставляется" +
                                                                              " в 20 в ответе")
                                                     @PageableDefault(size = 20) Pageable pageable,
                                                     @Parameter(description = "Номер аккаунта", required = true)
                                                     @RequestParam(value = "accountFrom") String accountFrom,
                                                     @Parameter(description = "Флаг, был ли привышен лимит в момент выполнения транзакции или нет", required = true)
                                                     @RequestParam(value = "limit_exceed", required = false) Boolean limitExceed){
        if (pageable.getPageSize() > maxPageSize) {
            pageable = PageRequest.of(pageable.getPageNumber(), maxPageSize);
        }
        TransactionParams transactionParams = new TransactionParams();
        transactionParams.setLimitExceeded(limitExceed);
        transactionParams.setAccountFrom(accountFrom);
        return transactionServiceImpl.getTransactions(transactionParams, pageable).stream()
                .map(TransactionResponse::new)
                .collect(Collectors.toList());
    }

}
