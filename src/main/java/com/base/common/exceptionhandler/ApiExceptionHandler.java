package com.base.common.exceptionhandler;

import com.base.common.exception.NotFoundException;
import com.base.common.model.ApiErrorMsg;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceResolvable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

@ControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {


    @Autowired
    MessageSource messageSource;

    private final Map<Class<? extends Exception>, String> messageMappings =
            Collections.unmodifiableMap(new LinkedHashMap(){
                {
                    put(HttpMessageNotReadableException.class, "잘못된 요청입니다.");
                    put(MethodArgumentNotValidException.class, "요청값이 잘못되었습니다.");
                }
            });



    /**
     * 기본 http 에러 발생 시
     * @param ex
     * @param body
     * @param headers
     * @param status
     * @param request
     * @return
     */
    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers, HttpStatus status, WebRequest request) {
        ApiErrorMsg apiErrorMsg = createApiError(ex);
        return super.handleExceptionInternal(ex, apiErrorMsg, headers, status, request);
    }

    /**
     * Custom Error 발생 시
     * @param e
     * @param request
     * @return
     */
    @ExceptionHandler
    public ResponseEntity<Object> handleNotFoundException(NotFoundException e, WebRequest request){
        return handleExceptionInternal(e, null, null, HttpStatus.NOT_FOUND, request);
    }

    /**
     * System Error 발생 시
     * @param e
     * @param request
     * @return
     */
    @ExceptionHandler
    public ResponseEntity<Object> handleSystemException(Exception e, WebRequest request){
        ApiErrorMsg errorMsg = createApiError(e, "시스템 에러가 발생하였습니다.");
        return handleExceptionInternal(e, errorMsg, null, HttpStatus.NOT_FOUND, request);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {

        ApiErrorMsg apiErrorMsg = createApiError(ex, ex.getMessage());

        ex.getBindingResult().getGlobalErrors().stream()
                .forEach(e -> apiErrorMsg.addDetail(e.getObjectName(),getMessage(e, request)));
        ex.getBindingResult().getFieldErrors().stream()
                .forEach(e -> apiErrorMsg.addDetail(e.getField(),getMessage(e, request)));

        return super.handleExceptionInternal(ex, apiErrorMsg, headers, status, request);
    }

    private String resolveMessage(Exception ex, String message){
        return messageMappings.entrySet().stream()
                .filter(entry -> entry.getKey().isAssignableFrom(ex.getClass()))
                .findFirst().map(Map.Entry::getValue).orElse(message);
    }

    private ApiErrorMsg createApiError(Exception e){

        ApiErrorMsg apiErrorMsg = new ApiErrorMsg();
        apiErrorMsg.setErrorMessage(resolveMessage(e, e.getMessage()));
        return apiErrorMsg;
    }

    private ApiErrorMsg createApiError(Exception e, String message){
        ApiErrorMsg apiErrorMsg = new ApiErrorMsg();
        apiErrorMsg.setErrorMessage(resolveMessage(e, message));
        return apiErrorMsg;
    }

    private String getMessage(MessageSourceResolvable resolvable, WebRequest request){
        return messageSource.getMessage(resolvable,request.getLocale());
    }


}
