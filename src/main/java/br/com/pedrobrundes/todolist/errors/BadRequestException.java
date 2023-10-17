package br.com.pedrobrundes.todolist.errors;

import br.com.pedrobrundes.todolist.enumeration.MessageBase;
import lombok.Getter;

public class BadRequestException extends RuntimeException{

    @Getter
    private ErrorModel errorModel;

    private BadRequestException(ErrorModel errorModel) {
        super(errorModel.message());
        this.errorModel = errorModel;
    }

    public static BadRequestException of(MessageBase message) {
        return new BadRequestException(new ErrorModel(message.getCode(), message.getMessage()));
    }
}
