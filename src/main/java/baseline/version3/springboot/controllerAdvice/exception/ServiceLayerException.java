package baseline.version3.springboot.controllerAdvice.exception;

import baseline.version3.springboot.controllerAdvice.exception.abs.AbstractCustomException;
import baseline.version3.springboot.controllerAdvice.subType.ServiceException;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ServiceLayerException extends AbstractCustomException {

    private final HttpStatus statusCode;
    private final ServiceException serviceException;

    public ServiceLayerException(ServiceException serviceException) {
        super(serviceException.getErrorMessage());
        this.statusCode = serviceException.getStatusCode();
        this.serviceException = serviceException;
    }

    @Override
    public HttpStatus getStatusCode() {
        return this.statusCode;
    }
}
