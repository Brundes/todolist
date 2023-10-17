package br.com.pedrobrundes.todolist.enumeration;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum TaskMessages implements MessageBase{

    TASK_NOT_FOUND(2, "Tarefa não encontrada."),
    USER_DOES_NOT_HAVE_PERMISSION(3, "Usuário não tem permissão para alterar essa tarefa")

;
    private Integer code;
    private String message;
}
