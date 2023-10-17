package br.com.pedrobrundes.todolist.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum UserMessages implements MessageBase {

    USER_ALREADY_EXIST (1,"User already exists.")

   ;
    private Integer code;
    private String message;
}
