package kz.solva.tz.expense.tracker.api.exception;

public class PurchaseLimitExceededException extends Exception{
    public PurchaseLimitExceededException(String message) {
        super(message);
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }
}
