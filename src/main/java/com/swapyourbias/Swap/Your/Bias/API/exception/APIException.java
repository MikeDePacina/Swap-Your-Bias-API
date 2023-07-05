package com.swapyourbias.Swap.Your.Bias.API.exception;

import org.springframework.http.HttpStatus;

public class APIException extends RuntimeException{
    private HttpStatus status;
    private String message;

    public APIException(HttpStatus status, String message){
        this.status = status;
        this.message = message;
    }

    public HttpStatus getStatus(){
        return this.status;
    }

    public String getMessage(){
        return this.message;
    }

}
