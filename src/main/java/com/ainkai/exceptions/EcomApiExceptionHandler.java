/*
 * Copyright (c) 2025. Mitakshar.
 * All rights reserved.
 *
 * This is an e-commerce project built for Learning Purpose and may not be reproduced, distributed, or used without explicit permission from Mitakshar.
 *
 *
 */

package com.ainkai.exceptions;

import com.ainkai.api.utils.*;
import com.ainkai.model.dtos.EcomApiServiceBaseApiResponse;
import com.ainkai.user.domain.Constants;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import java.time.Instant;
import java.util.*;

@RestControllerAdvice
@RequiredArgsConstructor
public class EcomApiExceptionHandler {

    private static final String ERROR_SUFFIX = "ERROR";

    @ExceptionHandler(UserException.class)
    public ResponseEntity<EcomApiServiceBaseApiResponse> serviceExceptionHandler(UserException ex,
                                                                                 WebRequest request) {
        HttpStatusCode statusCode = switch (ex.getMessageKey()) {
            case Constants.DATA_NOT_FOUND_KEY -> HttpStatus.NOT_FOUND;
            case Constants.REQUEST_ERROR_KEY -> HttpStatus.BAD_REQUEST;
            default -> HttpStatus.UNPROCESSABLE_ENTITY;
        };

        EcomApiServiceBaseApiResponse apiResponse = (EcomApiServiceBaseApiResponse) new EcomApiServiceBaseApiResponse()
                .metadata(new Metadata().timestamp(Instant.now()))
                .status(new Status().statusCode(statusCode.value()).statusMessage(ex.getMessage())
                        .statusMessageKey(ex.getMessageKey()));

        return new ResponseEntity<>(apiResponse, statusCode);
    }

    @ExceptionHandler(HttpClientErrorException.BadRequest.class)
    public ResponseEntity<EcomApiServiceBaseApiResponse> badRequestExceptionHandler(
            HttpClientErrorException.BadRequest ex, WebRequest request) {
        EcomApiServiceBaseApiResponse apiResponse = (EcomApiServiceBaseApiResponse) new EcomApiServiceBaseApiResponse()
                .metadata(new Metadata().timestamp(Instant.now()))
                .status(new Status().statusCode(HttpStatus.BAD_REQUEST.value())
                        .statusMessage(Constants.REQUEST_ERROR_MSG)
                        .statusMessageKey(Constants.REQUEST_ERROR_KEY));
        return new ResponseEntity<>(apiResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<EcomApiServiceBaseApiResponse> methodArgsExceptionHandler(
            MethodArgumentNotValidException ex, WebRequest request) {

        List<ApiMessage> errorMsgs = new ArrayList<>();
        for (ObjectError error : ex.getBindingResult().getAllErrors()) {
            String defaultMessage =
                    null != error.getDefaultMessage() ? ((FieldError) error).getDefaultMessage()
                            : "field error";
            errorMsgs.add(new ApiMessage().messageType(MessageType.ERROR)
                    .messageKey(((FieldError) error).getField().toUpperCase() + "_" + ERROR_SUFFIX)
                    .fieldName(((FieldError) error).getField()).errorType(ErrorType.REQUEST_ERROR)
                    .value(defaultMessage));
        }
        EcomApiServiceBaseApiResponse apiResponse = (EcomApiServiceBaseApiResponse) new EcomApiServiceBaseApiResponse()
                .messages(errorMsgs).metadata(new Metadata().timestamp(Instant.now()))
                .status(new Status().statusCode(HttpStatus.BAD_REQUEST.value())
                        .statusMessage(Constants.REQUEST_ERROR_MSG)
                        .statusMessageKey(Constants.REQUEST_ERROR_KEY));
        return new ResponseEntity<>(apiResponse, HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(ProductException.class)
    public ResponseEntity<EcomApiServiceBaseApiResponse> ProductServiceExceptionHandler(ProductException ex,
                                                                                 WebRequest request) {
        HttpStatusCode statusCode = switch (ex.getMessageKey()) {
            case Constants.DATA_NOT_FOUND_KEY -> HttpStatus.NOT_FOUND;
            case Constants.REQUEST_ERROR_KEY -> HttpStatus.BAD_REQUEST;
            default -> HttpStatus.UNPROCESSABLE_ENTITY;
        };

        EcomApiServiceBaseApiResponse apiResponse = (EcomApiServiceBaseApiResponse) new EcomApiServiceBaseApiResponse()
                .metadata(new Metadata().timestamp(Instant.now()))
                .status(new Status().statusCode(statusCode.value()).statusMessage(ex.getMessage())
                        .statusMessageKey(ex.getMessageKey()));

        return new ResponseEntity<>(apiResponse, statusCode);
    }
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<EcomApiServiceBaseApiResponse> missingServletRequestParameterExceptionHandler(
            MissingServletRequestParameterException ex, WebRequest request) {
        List<ApiMessage> errorMsgs = new ArrayList<>();
        errorMsgs.add(new ApiMessage().messageType(MessageType.ERROR)
                .messageKey(ex.getParameterName().toUpperCase() + "_" + ERROR_SUFFIX)
                .fieldName(ex.getParameterName()).errorType(ErrorType.REQUEST_ERROR)
                .value("Request Parameter Missing or Blank"));
        EcomApiServiceBaseApiResponse apiResponse = (EcomApiServiceBaseApiResponse) new EcomApiServiceBaseApiResponse()
                .messages(errorMsgs).metadata(new Metadata().timestamp(Instant.now()))
                .status(new Status().statusCode(HttpStatus.BAD_REQUEST.value())
                        .statusMessage(Constants.REQUEST_ERROR_MSG)
                        .statusMessageKey(Constants.REQUEST_ERROR_KEY));
        return new ResponseEntity<>(apiResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MissingPathVariableException.class)
    public ResponseEntity<EcomApiServiceBaseApiResponse> missingPathVariableExceptionHandler(
            MissingPathVariableException ex, WebRequest request) {
        List<ApiMessage> errorMsgs = new ArrayList<>();
        errorMsgs.add(new ApiMessage().messageType(MessageType.ERROR)
                .messageKey(ex.getVariableName().toUpperCase() + "_" + ERROR_SUFFIX)
                .fieldName(ex.getVariableName()).errorType(ErrorType.REQUEST_ERROR).value(ex.getMessage()));
        EcomApiServiceBaseApiResponse apiResponse = (EcomApiServiceBaseApiResponse) new EcomApiServiceBaseApiResponse()
                .messages(errorMsgs).metadata(new Metadata().timestamp(Instant.now()))
                .status(new Status().statusCode(HttpStatus.BAD_REQUEST.value())
                        .statusMessage(Constants.REQUEST_ERROR_MSG)
                        .statusMessageKey(Constants.REQUEST_ERROR_KEY));
        return new ResponseEntity<>(apiResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<EcomApiServiceBaseApiResponse> handleMaxSizeException(
            MaxUploadSizeExceededException exc, WebRequest request) {
        EcomApiServiceBaseApiResponse apiResponse = (EcomApiServiceBaseApiResponse) new EcomApiServiceBaseApiResponse().messages(null)
                .metadata(new Metadata().timestamp(Instant.now()))
                .status(new Status().statusCode(HttpStatus.BAD_REQUEST.value())
                        .statusMessage(Constants.REQUEST_ERROR_MSG)
                        .statusMessageKey(Constants.REQUEST_ERROR_KEY));
        return new ResponseEntity<>(apiResponse, HttpStatus.BAD_REQUEST);
    }


}
