package com.javamentor.qa.platform.webapp.controllers.rest;

import com.javamentor.qa.platform.models.entity.question.answer.Answer;
import com.javamentor.qa.platform.models.dto.AnswerDto;
import com.javamentor.qa.platform.models.entity.user.User;
import com.javamentor.qa.platform.service.abstracts.model.AnswerService;
import com.javamentor.qa.platform.service.abstracts.dto.AnswerDtoService;
import com.javamentor.qa.platform.service.abstracts.model.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/admin")
@Api(value = "Security")
public class AdminResourceController {
    private final UserService userService;
    private final AnswerService answerService;
    private final AnswerDtoService answerDtoService;

    @ApiOperation(value = "Права доступа и авторизации пользователя", tags = {"isEnabled"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Успешное выполнение"),
            @ApiResponse(code = 400, message = "Ошибка выполнения")})
    @PostMapping("/delete/{email}")
    public ResponseEntity<?> deleteUser(@PathVariable("email") String email) {
        Optional<User> optionalUser = userService.getByEmail(email);
        if (optionalUser.isPresent()) {
            userService.deleteByEmail(email);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().body("User with email " + email + " not found");
    }

    @ApiOperation(value = "Удаление ответа по ID")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Успешное выполнение операции"),
            @ApiResponse(code = 404, message = "Ответ с указанным ID не найден")})
    @DeleteMapping("/answer/{id}/delete")
    public ResponseEntity<String> deleteAnswerById(@PathVariable("id") Long id){
        Optional<Answer> optionalAnswer = answerService.getById(id);
        if(optionalAnswer.isEmpty()) {
            return new ResponseEntity<>("Answer is not exist", HttpStatus.NOT_FOUND);
        }
        Answer answer = optionalAnswer.get();
        answer.setIsDeleted(true);
        answer.setIsDeletedByModerator(true);
        answerService.update(answer);
        return new ResponseEntity<>("Answer is successfully deleted", HttpStatus.OK);
    }

    @ApiOperation(value = "Получение списка удаленных ответов у пользователя",
            tags = {"Получение списка"}, response = AnswerDto.class, responseContainer = "list")
    @GetMapping("/answer/delete")
    public ResponseEntity<List<AnswerDto>> deletedAnswers(@RequestParam Long userId) {
        return ResponseEntity.ok(answerDtoService.getDeletedAnswersByUserId(userId));
    }
}