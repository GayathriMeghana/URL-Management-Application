package com.apis.postgrestesting.controller;
import com.apis.postgrestesting.entity.UsersEntity;
import com.apis.postgrestesting.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
@RestController
@RequestMapping(value="/api/user")
public class UsersController {
    @Autowired
    private UserService userService;
    @RequestMapping(value="/register", method = RequestMethod.POST)
    public Map<String, Object> registerUser(@RequestBody Map<String, Object> requestJson) {
        UsersEntity usersEntity = userService.registerUser(requestJson);
        Map<String, Object> responseJson = new HashMap<>();
        responseJson.put("user_id", usersEntity.getUserId());
        responseJson.put("email", usersEntity.getEmail());
        responseJson.put("dob", usersEntity.getDob());
        responseJson.put("profession", usersEntity.getProfession());
        responseJson.put("mobile_number", usersEntity.getMobileNumber());
        responseJson.put("username", usersEntity.getUsername());
        return responseJson;
    }

//
//    @GetMapping("/retrieve-user")
//    public Map<String, Object> getUserById(@RequestParam("user-id") Long userId) {
//        return userService.getUserById(userId);
//    }


    @GetMapping("/retrieve-user")
    public Map<String, Object> getUserById(HttpServletRequest request) {
        // Get JWT token from request header
        String jwtToken = request.getHeader("token");

        // Decode the JWT token to get claims
        Map<String, Object> claims = userService.decodeJwt(jwtToken);

        // Extract userId from claims
        String userIdStr = claims.get("userId").toString();
        Long userId = Long.valueOf(userIdStr);

        // Return user details by ID
        return userService.getUserById(userId);
    }



    @PutMapping("/update")
    public Map<String, Object> updateUser(HttpServletRequest request,
                                          @RequestBody Map<String, Object> updateDetails) {
        String jwtToken=request.getHeader("token");
        Map<String, Object> claims=userService.decodeJwt(jwtToken);
        Long userId=Long.valueOf(claims.get("userId").toString());
        return userService.updateUser(userId, updateDetails);
    }


    @DeleteMapping("/delete")
    public Map<String, String> deleteUser(HttpServletRequest request) {
        String jwtToken = request.getHeader("token");
        Map<String, Object> claims = userService.decodeJwt(jwtToken);
        Long userId = Long.valueOf(claims.get("userId").toString());

        userService.deleteUser(userId);

        Map<String, String> response = new HashMap<>();
        response.put("message", "User deleted successfully");
        return response;
    }



//    @RequestMapping(value = "/retrieve-all-users", method = RequestMethod.GET)
//    public Map<String, Object> getAllUsers(HttpServletRequest request) {
//        String jwtToken = request.getHeader("token");
//        Map<String, Object> claims = userService.decodeJwt(jwtToken);
//
//        // Optional: check role here
//        return userService.getAllUsers();
//    }


    @RequestMapping(value = "/retrieve-all-users", method = RequestMethod.GET)
    public Map<String, Object> getAllUsers(HttpServletRequest request) {
        String jwtToken = request.getHeader("token");
        return userService.getAllUsers(jwtToken);
    }



    @PostMapping("/login")
    public Map<String, Object> loginUser(@RequestBody Map<String, Object> loginJson) {
        try {
            return userService.loginUser(loginJson);
        } catch (RuntimeException e) {
            Map<String, Object> error = new HashMap<>();
            error.put("message", e.getMessage());
            return error;
        }
    }
}

