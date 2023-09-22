package kz.solva.tz.expense.tracker.api.integration.fixer.api;

public interface RequestSender<T> {
    T get(Request request);
}
