package br.com.pedrobrundes.todolist.task;

import br.com.pedrobrundes.todolist.enumeration.TaskMessages;
import br.com.pedrobrundes.todolist.enumeration.TestMessage;
import br.com.pedrobrundes.todolist.errors.BadRequestException;
import br.com.pedrobrundes.todolist.utils.Utils;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    @Autowired
    private ITaskRepository taskRepository;

    @PostMapping("/")
    public ResponseEntity create(@RequestBody TaskModel taskModel, HttpServletRequest request) {
        var idUser = request.getAttribute("idUser");
        taskModel.setIdUser((UUID) idUser);

        validatePastDate(taskModel);

        if (taskModel.getStartAt().isAfter(taskModel.getEndAt())){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("A data de término tem que ser depois da data de início.");
        }

        var task = this.taskRepository.save(taskModel);
        return ResponseEntity.status(HttpStatus.OK).body(task);
    }

//    TODO 1 - Mudar o retorno do método para "void"
//    TODO 2 - Alterar de "return" para "throw BadRequestException.of(TaskMessages.CRIAR UM ENUM)
//    TODO 3 - Criar um ENUM para lançar a mensagem de erro
    private static ResponseEntity<String> validatePastDate(TaskModel taskModel) {
        var currentDate = LocalDateTime.now();
        if (currentDate.isAfter(taskModel.getStartAt()) || currentDate.isAfter(taskModel.getEndAt())){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("A data de início / data de término deve ser maior que a data atual");
        }
        return null;
    }

    @GetMapping("/")
    public List<TaskModel> list(HttpServletRequest request){
        var idUser = request.getAttribute("idUser");
        var tasks = this.taskRepository.findByIdUser((UUID) idUser);
        return tasks;
    }

    @PutMapping("/{id}")
    public ResponseEntity update(@RequestBody TaskModel taskModel, HttpServletRequest request, @PathVariable UUID id) {
        var task = this.taskRepository.findById(id).orElse(null);

        validateTaskExistance(task);

        validateUserPermission(request, task);

        Utils.copyNonNullProperties(taskModel, task);
        var taskUpdated = this.taskRepository.save(task);
        return ResponseEntity.ok().body(taskUpdated);
    }

    private static void validateUserPermission(HttpServletRequest request, TaskModel task) {
        var idUser = request.getAttribute("idUser");
        if (!task.getIdUser().equals(idUser)) {
            throw BadRequestException.of(TaskMessages.USER_DOES_NOT_HAVE_PERMISSION);
        }
    }

    private static void validateTaskExistance(TaskModel taskModel) {
        if (taskModel == null) {
            throw BadRequestException.of(TaskMessages.TASK_NOT_FOUND);
        }
    }
}
