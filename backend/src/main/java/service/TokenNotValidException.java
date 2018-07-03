package service;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Created by mihkel on 20.06.2018.
 */
@ResponseStatus(HttpStatus.FORBIDDEN)
public class TokenNotValidException extends RuntimeException {

    public TokenNotValidException(String exception) {
        super(exception);
    }

}