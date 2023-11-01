package br.ufrj.coppetec.servicechat.core.apis.handlers;

import br.ufrj.coppetec.servicechat.domain.exceptions.ExceptionError;
import br.ufrj.coppetec.servicechat.domain.exceptions.InfraStructureException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class ExceptionHandlerImpl {

    @ResponseBody
    @ExceptionHandler(InfraStructureException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionError dataIntegrityViolationException(InfraStructureException e) {
        return new ExceptionError(e.getMessage());
    }
}