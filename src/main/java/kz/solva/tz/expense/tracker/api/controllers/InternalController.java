package kz.solva.tz.expense.tracker.api.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import kz.solva.tz.expense.tracker.api.dto.TransactionRequest;
import kz.solva.tz.expense.tracker.api.dto.TransactionResponse;
import kz.solva.tz.expense.tracker.api.exception.*;
import kz.solva.tz.expense.tracker.api.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/internal")
@Validated
@Tag(name = "API для приема транзакций")
public class InternalController {
    @Qualifier("transactionServiceImpl")
    private final TransactionService<TransactionRequest> transactionServiceImpl;

    @Autowired
    public InternalController(TransactionService<TransactionRequest> transactionServiceImpl) {
        this.transactionServiceImpl = transactionServiceImpl;
    }

    @Operation(summary = "Эндпоинт для проведения транзакции")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Запрос успешно выполнен",
                    content = @Content(schema = @Schema(implementation = TransactionResponse.class))),
            @ApiResponse(responseCode = "400",
                    description = "В случае, если пользователь не передал один обязательных из параметров в теле запроса)",
                    content = @Content(schema = @Schema(implementation = ExceptionDto.class))),
            @ApiResponse(responseCode = "404",
                    description = "Если аккаунт не найден",
                    content = @Content(schema = @Schema(implementation = ExceptionDto.class)))
    })
    @PostMapping("/complete-transaction")
    public TransactionResponse completeTransaction(@RequestBody @Valid TransactionRequest request) throws AccountNotFoundException,
                                                                                                   TwelvedataApiException,
                                                                                                   PurchaseLimitExceededException,
                                                                                                   PaymentException {
        return new TransactionResponse(transactionServiceImpl.completeTransaction(request));
    }
}
