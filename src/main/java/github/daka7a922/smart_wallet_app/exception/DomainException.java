package github.daka7a922.smart_wallet_app.exception;

public class DomainException extends RuntimeException{

    public DomainException(String message) {
        super(message);
    }

    public DomainException(String message, Throwable cause) {
        super(message, cause);
    }
}
