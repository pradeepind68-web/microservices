package com.ms.auth.service.auth_service.controller;

import com.ms.auth.service.auth_service.dto.Payload;
import com.ms.auth.service.auth_service.dto.UserDTO;
import com.ms.auth.service.auth_service.proxy.UserClient;
import com.ms.auth.service.auth_service.service.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("auth-api")
public class AuthController {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserClient userClient;

    @Autowired
    private RestTemplate restTemplate;

    @PostMapping(value = "/register")
    public ResponseEntity<Payload> registerUser(@RequestBody UserDTO userDTO){

        String url="http://user-service:8765/user-api/user";
        return restTemplate.postForEntity(url,userDTO,Payload.class);
    }

    @PostMapping(value = "/login")
    public ResponseEntity<Payload> login(@RequestBody UserDTO userDTO){
        ResponseEntity<Boolean> exist=userClient.findByUsername(userDTO.username());
        if(exist.getBody()){
            return ResponseEntity.ok(new Payload(jwtService.generateToken(userDTO.username()),null));
        }else{
            return ResponseEntity.notFound().build();
        }

    }

}
