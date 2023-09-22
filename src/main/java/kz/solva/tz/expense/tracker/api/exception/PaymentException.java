package kz.solva.tz.expense.tracker.api.exception;

public class PaymentException extends Throwable {
    public PaymentException(String message) {
        super(message);
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }
}
