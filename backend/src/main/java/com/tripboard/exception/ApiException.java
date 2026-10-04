package com.tripboard.exception;
import org.springframework.http.HttpStatus;
import lombok.Getter;
@Getter
public class ApiException extends RuntimeException {
 private final HttpStatus status;
 public ApiException(HttpStatus status,String message){super(message);this.status=status;}
 public static ApiException bad(String message){return new ApiException(HttpStatus.BAD_REQUEST,message);}
 public static ApiException forbidden(){return new ApiException(HttpStatus.FORBIDDEN,"You do not have permission for this action.");}
 public static ApiException missing(){return new ApiException(HttpStatus.NOT_FOUND,"This item no longer exists.");}
}
