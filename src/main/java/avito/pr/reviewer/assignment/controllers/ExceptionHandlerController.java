package avito.pr.reviewer.assignment.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpServerErrorException.InternalServerError;

import avito.pr.reviewer.assignment.dto.responses.ErrorResponseDto;
import avito.pr.reviewer.assignment.exceptions.NotFoundResourceError;
import avito.pr.reviewer.assignment.exceptions.CodeError;

@RestControllerAdvice
public class ExceptionHandlerController {
    @ExceptionHandler(NotFoundResourceError.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public ErrorResponseDto handleException(NotFoundResourceError e) {
        return ErrorResponseDto.builder()
            .message(e.getMessageString())
            .code(e.getCodeError())
            .build();
    }

    @ExceptionHandler(InternalServerError.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponseDto handleException(InternalServerError e) {
        return ErrorResponseDto.builder()
            .message("internal server error")
            .code(CodeError.INTERNAL_SERVER_ERROR)
            .build();
    }
}
