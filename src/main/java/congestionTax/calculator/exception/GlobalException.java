package congestionTax.calculator.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class GlobalException extends Exception{

    private String message;
    private HttpStatus httpStatus;

    public GlobalException(String message) {
        this.message = message;
    }

    public GlobalException(String message, HttpStatus httpStatus) {
        this.message = message;
        this.httpStatus = httpStatus;
    }
}
