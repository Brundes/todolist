package br.com.pedrobrundes.todolist.errors;

import java.time.LocalDateTime;

public record ErrorModel (Integer code, String message, LocalDateTime timestamp) {

    public ErrorModel(Integer code,String message){
        this(code, message, LocalDateTime.now());
    }
}