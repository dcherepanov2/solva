package kz.solva.tz.expense.tracker.api.service;

import kz.solva.tz.expense.tracker.api.data.*;
import kz.solva.tz.expense.tracker.api.dto.TransactionParams;
import kz.solva.tz.expense.tracker.api.exception.AccountNotFoundException;
import kz.solva.tz.expense.tracker.api.exception.PaymentException;
import kz.solva.tz.expense.tracker.api.exception.PurchaseLimitExceededException;
import kz.solva.tz.expense.tracker.api.exception.TwelvedataApiException;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface TransactionService <T> {
    Transaction completeTransaction(T request) throws AccountNotFoundException,
                                                                              TwelvedataApiException,
                                                                              PurchaseLimitExceededException,
                                                                              PaymentException;
    Transaction createNewTransaction(T request, Account from, Account to, ExpenseCategory category, ExpenseLimit limit, LimitExceeded limitExceeded) throws TwelvedataApiException;

    List<Transaction> getTransactions(TransactionParams transactionParams, Pageable pageable);
}
