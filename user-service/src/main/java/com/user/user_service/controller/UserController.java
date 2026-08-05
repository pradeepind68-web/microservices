package com.user.user_service.controller;

import com.user.user_service.dto.Payload;
import com.user.user_service.dto.UserDTO;
import com.user.user_service.entity.UserDetails;
import com.user.user_service.repo.UserRepo;
import com.user.user_service.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/user-api")
public class UserController {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private UserService userService;

    @GetMapping(value = "/user/{id}")
    public ResponseEntity<Payload> getUser(@PathVariable int id){

        Optional<UserDetails> userDetails=userRepo.findById(id);
        return userDetails.map(details -> ResponseEntity.ok(new Payload(userDetails,null)))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new Payload(null,"No Such User Found")));
    }

    @GetMapping(value = "/findByUsername/{username}")
    public ResponseEntity<Boolean> findByUsername(@PathVariable String username){

        Boolean exists=userRepo.existsByUsernameIgnoreCase(username);
        return ResponseEntity.ok(exists);
    }

    @GetMapping(value = "/users")
    public ResponseEntity<Payload> getUsers(){

        List<UserDetails> userDetails=userRepo.findAll();
        if (!userDetails.isEmpty())
            return ResponseEntity.ok(new Payload(userDetails,null));
        else
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Payload(null,"There is no User"));
    }

    @PostMapping(value = "/user")
    public ResponseEntity<Payload> saveUser(@RequestBody UserDTO user){

        return ResponseEntity.status(HttpStatus.CREATED).body(userService.saveUser(user));
    }

    @PostMapping(value = "/users")
    public ResponseEntity<Payload> saveUsers(@RequestBody List<UserDTO> users){

        return ResponseEntity.status(HttpStatus.CREATED).body(userService.saveAllUser(users));
    }

    @PutMapping(value = "/user/{id}")
    public ResponseEntity<Payload> updateUser(@RequestBody UserDTO userDTO,@PathVariable int id){

        return ResponseEntity.ok(new Payload(userService.updateUser(userDTO,id),null));
    }

    @DeleteMapping(value = "/user/{id}")
    public ResponseEntity<Payload> deleteUser(@PathVariable int id){
        userRepo.deleteById(id);
        return ResponseEntity.ok(new Payload("User Deleted",null));
    }
}
