package br.com.pedrobrundes.todolist.user;

import at.favre.lib.crypto.bcrypt.BCrypt;
import br.com.pedrobrundes.todolist.enumeration.UserMessages;
import br.com.pedrobrundes.todolist.errors.BadRequestException;
import br.com.pedrobrundes.todolist.errors.ErrorModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Modificador
 * public
 * private
 * protected
 */
@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private IUserRepository userRepository;

    @PostMapping("/")
    public ResponseEntity create(@RequestBody UserModel userModel) {

        validateUserExistance(userModel);
        var passwordHashred = BCrypt.withDefaults().hashToString(12, userModel.getPassword().toCharArray());

        userModel.setPassword(passwordHashred);

        var userCreated = this.userRepository.save(userModel);
        return ResponseEntity.status(HttpStatus.CREATED).body(userCreated);
    }

    private void validateUserExistance(UserModel userModel) {
        if (existsUser(userModel)){
//            throw new BadRequestException(new ErrorModel(UserMessages.USER_ALREADY_EXIST.getCode(), UserMessages.USER_ALREADY_EXIST.getMessage()));
            throw BadRequestException.of(UserMessages.USER_ALREADY_EXIST);
        }
    }

    private boolean existsUser(UserModel userModel) {
        return this.userRepository.existsByUsername(userModel.getUsername());
    }
}
