package com.user.user_service.service;

import com.user.user_service.dto.Payload;
import com.user.user_service.dto.UserDTO;
import com.user.user_service.entity.UserDetails;
import com.user.user_service.repo.UserRepo;
import org.jspecify.annotations.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserRepo userRepo;

    public Payload saveUser(UserDTO userDTO){
        boolean isExist= isExist(userDTO.name(),userDTO.email());
        if(!isExist) {
            UserDetails userDetails = getUserDetails(userDTO);
            userRepo.save(userDetails);
            return new Payload("User Saved Successfully ",null);
        }else
            return new Payload(null,"User with same name and email already exists");

    }

    public Payload saveAllUser(List<UserDTO> userDTOs){
        List<String> result=userDTOs.stream().map(userDto-> {
            boolean exist= isExist(userDto.name(), userDto.email());
            if(exist)
                return "User with same name :"+userDto.name()+" and email: "+userDto.email()+" already exists";
            else
                return null;
        }).toList();
        if(result.stream().allMatch(Objects::isNull)) {
            List<UserDetails> userDetails = userDTOs.stream().map(UserService::getUserDetails).collect(Collectors.toList());
            userRepo.saveAll(userDetails);
            return new Payload("Users Saved Successfully ",null);
        }else
            return new Payload(result.stream().filter(Objects::nonNull).toList(),null);

    }

    public String updateUser(UserDTO userDTO,int id){
        UserDetails user=userRepo.findById(id).orElseThrow(()->new RuntimeException("User not found"));
        UserDetails userDetails=updateUserDetails(user,userDTO);
        userRepo.save(userDetails);
        return "User updated Successfully";
    }

    private static UserDetails updateUserDetails(UserDetails userDetails,UserDTO userDTO){
        return UserDetails.builder().userId(userDetails.getUserId()).name(userDTO.name()
                    !=null?userDTO.name():userDetails.getName()).email(userDTO.email()
                    !=null?userDTO.email():userDetails.getEmail()).username(userDetails.getUsername()).build();
    }

    private static @NonNull UserDetails getUserDetails(UserDTO userDTO) {
        return UserDetails.builder().userId(userDTO.userId())
                .name(userDTO.name()).email(userDTO.email()).username(userDTO.username())
                .password(userDTO.password()).build();
    }

    public boolean isExist(String name, String email){
        return userRepo.existsByNameIgnoreCaseAndEmailIgnoreCase(name,email);
    }
}
