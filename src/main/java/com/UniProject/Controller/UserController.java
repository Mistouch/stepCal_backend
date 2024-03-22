package com.UniProject.Controller;

import com.UniProject.DTO.LoginDetails;
import com.UniProject.DTO.TaskDto;
import com.UniProject.DTO.UserDto;
import com.UniProject.Entities.CompletedTask;
import com.UniProject.Entities.User;
import com.UniProject.DTO.VerCode;
import com.UniProject.Services.CompletedTaskService;
import com.UniProject.Services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jdk.jfr.Frequency;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {
    private int code;
    private String userEmail;

    @Autowired
    UserService userService;

    @Autowired
    CompletedTaskService taskService;

    @GetMapping("/message")
    public ResponseEntity<String>showMessage(){
        return ResponseEntity.status(HttpStatus.OK).body("This is for User");
    }

    @GetMapping("/profile")
    public UserDto showProfile(HttpServletRequest request){
        String email= (String) request.getAttribute("email");
        return userService.getUser(email);
    }

    @PostMapping("/add-task")
    public ResponseEntity<String>saveTask(@RequestBody CompletedTask task,HttpServletRequest request){
        //System.out.println(task);
        try {
            task.setEmail((String) request.getAttribute("email"));
            task.setDate(LocalDate.now().toString());
            taskService.saveTask(task);
        }catch (Exception e){
            System.out.println("in");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed");
        }
        System.out.println("ok");
        return ResponseEntity.status(HttpStatus.OK).body("Saved");
    }
    @GetMapping("/task-today")
    public CompletedTask showCompletedTask(HttpServletRequest request){
        return taskService.getTask((String)request.getAttribute("email"),LocalDate.now().toString());
    }
    @GetMapping("/task-history")
    public List<CompletedTask>showTaskHistory(HttpServletRequest request){
        return taskService.taskHistory((String) request.getAttribute("email"));
    }

    @GetMapping("/task")
    public TaskDto sendTask(HttpServletRequest request){
        String email=(String) request.getAttribute("email");
        return userService.forTask(email);
    }
    @PutMapping("/update-point")
    public ResponseEntity<String> updatePoint(HttpServletRequest request){
        String email= (String) request.getAttribute("email");
        try{
            userService.givePoint(email);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("failed");
        }
        return ResponseEntity.status(HttpStatus.OK).body("saved");
    }
    /*
    @GetMapping("/verify/{email}")
    public ResponseEntity<String> sendEmail(@PathVariable String email){
        userEmail=email;
        code=userService.getVerCode(email);
        System.out.println(code);
        return ResponseEntity.status(HttpStatus.OK).body("Sent");
    }

    @PostMapping("/check")
    public ResponseEntity<String> show(@RequestBody VerCode verCode){
        int userCode = verCode.getCode();
        if(userCode==code){
            userService.verifyUser(userEmail);
            return ResponseEntity.status(HttpStatus.OK).body("Verified");
        }
        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("Wrong verification code");
    }
    */

    @GetMapping("/leaderboard")
    public ResponseEntity<List<UserDto>> getLeaderboard() {

        List<UserDto> leaderboard = userService.getLeaderboard();
        return ResponseEntity.ok(leaderboard);
    }

}
