package com.apis.postgrestesting.service;
import com.apis.postgrestesting.entity.UsersEntity;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import com.apis.postgrestesting.repository.UsersRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.xml.bind.DatatypeConverter;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.security.Key;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.JwtBuilder;
import javax.crypto.spec.SecretKeySpec;
@Service

public class UserService {
    @Autowired
    private UsersRepository usersRepository;
    public UsersEntity registerUser(Map<String, Object> userDetails){
        BCryptPasswordEncoder passwordEncoder=new BCryptPasswordEncoder();
        String hashedPassword=passwordEncoder.encode(userDetails.get("password").toString());
        UsersEntity newUserRequestEntity = new UsersEntity();
        newUserRequestEntity.setEmail(userDetails.get("email").toString());
        newUserRequestEntity.setPassword(hashedPassword);
        newUserRequestEntity.setProfession(userDetails.get("profession").toString());
        newUserRequestEntity.setMobileNumber(userDetails.get("mobileNumber").toString());
        newUserRequestEntity.setUsername(userDetails.get("username").toString());

        String dobString = userDetails.get("dob").toString();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate dob = LocalDate.parse(dobString, formatter);
        newUserRequestEntity.setDob(dob);
        UsersEntity newUserResponseEntity = usersRepository.save(newUserRequestEntity);
        return newUserResponseEntity;

    }

//    public UsersEntity getUserById(Long userId) {
//        return usersRepository.findById(userId)
//                .orElseThrow(() -> new RuntimeException("User with ID " + userId + " not found"));
//    }

    public Map<String, Object> getUserById(Long userId) {
        UsersEntity user = usersRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User with ID " + userId + " not found"));

        Map<String, Object> userMap = new HashMap<>();
        userMap.put("user_id", user.getUserId());
        userMap.put("email", user.getEmail());
        userMap.put("password", user.getPassword());
        userMap.put("profession", user.getProfession());
        userMap.put("mobile_number", user.getMobileNumber());
        userMap.put("dob", user.getDob());

        // userMap.put("password", user.getPassword()); // Optional, avoid exposing it in real apps
        userMap.put("message", "User retrieved successfully");

        return userMap;
    }

    public Map<String, Object> updateUser(Long userId, Map<String, Object> updateDetails) {
        UsersEntity user = usersRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Update fields
        user.setEmail(updateDetails.get("email").toString());
        user.setUsername(updateDetails.get("username").toString());
        user.setProfession(updateDetails.get("profession").toString());
        user.setMobileNumber(updateDetails.get("mobileNumber").toString());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String dobStr = updateDetails.get("dob").toString();
        LocalDate dob = LocalDate.parse(dobStr, formatter);
        user.setDob(dob);


        // 🔐 Encrypt password before saving
//        String plainPassword = updateDetails.get("password").toString();
//        String encryptedPassword = new BCryptPasswordEncoder().encode(plainPassword);
//        user.setPassword(encryptedPassword);

        // Save updated user
        usersRepository.save(user);

        // Prepare response
        Map<String, Object> response = new HashMap<>();
        response.put("message", "User updated successfully");
        response.put("userId", user.getUserId());
        response.put("email", user.getEmail());

        return response;
    }


    public void deleteUser(Long userId) {
        if (!usersRepository.existsById(userId)) {
            throw new RuntimeException("User not found");
        }
        usersRepository.deleteById(userId);
    }

    public Map<String, Object> getAllUsers(String token) {
        Map<String, Object> response = new HashMap<>();
        try {
            Map<String, Object> claims = decodeJwt(token);
            Long userIdFromToken = Long.parseLong(claims.get("userId").toString());

            // ✅ Check token is latest for this user
            String latestToken = latestTokens.get(userIdFromToken);
            if (latestToken == null || !latestToken.equals(token)) {
                response.put("error", "Access denied: Please login again.");
                return response;
            }

            // ✅ Valid user & valid token – return all users
            List<UsersEntity> users = usersRepository.findAll();
            response.put("total_users", users.size());
            response.put("users", users);
            response.put("message", "All users retrieved successfully");
            return response;

        } catch (Exception e) {
            response.put("error", "Access denied: Invalid or expired token.");
            return response;
        }
    }



    // In-memory map to track latest valid token per user
    private static final Map<Long, String> latestTokens = new HashMap<>();

    public Map<String, Object> loginUser(Map<String, Object> loginDetails) {
        String email = loginDetails.get("email").toString();
        String password = loginDetails.get("password").toString();

        // Find user by email
        UsersEntity user = usersRepository.findByEmail(email);
        if (user == null) {
            throw new RuntimeException("User not found with email: " + email);
        }

        // Check if password matches
        String hashedPasswordFromDB = user.getPassword();
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        boolean passwordMatches = passwordEncoder.matches(password, hashedPasswordFromDB);
        if (!passwordMatches) {
            throw new RuntimeException("Incorrect password");
        }

        // Generate new JWT token
        String token = createJwt(user.getEmail(), user.getUserId(), 3600000); // 1 hour

        // Save token as latest for this user
        latestTokens.put(user.getUserId(), token);

        // Prepare response
        Map<String, Object> response = new HashMap<>();
        response.put("user_id", user.getUserId());
        response.put("email", user.getEmail());
        response.put("token", token);
        response.put("message", "Login successful");

        return response;
    }









//
//    public String createJwt(String email, Long userId, long ttlMillis) {
//        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
//        long nowMillis = System.currentTimeMillis();
//        Date now = new Date(nowMillis);
//
//        byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary("This is a secret key This is a secret key This is a secret key This is a secret key");
//        Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());
//
//        Map<String, Object> claims = new HashMap<>();
//        claims.put("email", email);
//        claims.put("userId", userId.toString());
//
//        JwtBuilder builder = Jwts.builder()
//                .setId(email)
//                .setIssuedAt(now)
//                .setClaims(claims)
//                .signWith(signatureAlgorithm, signingKey);
//
//        if (ttlMillis > 0) {
//            long expMillis = nowMillis + ttlMillis;
//            Date exp = new Date(expMillis);
//            builder.setExpiration(exp);
//        }
//
//        return builder.compact();
//    }
//
//    public Map<String, Object> decodeJwt(String jwt) {
//        Map<String, Object> claims = Jwts.parser()
//                .setSigningKey(DatatypeConverter.parseBase64Binary("This is a secret key This is a secret key This is a secret key This is a secret key"))
//                .parseClaimsJws(jwt)
//                .getBody();
//
//        return claims;
//    }


    private final String secretKey = "This is a secret key This is a secret key This is a secret key This is a secret key";

    public String createJwt(String email, Long userId, long ttlMillis) {
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);

        Key signingKey = new SecretKeySpec(secretKey.getBytes(), signatureAlgorithm.getJcaName());

        Map<String, Object> claims = new HashMap<>();
        claims.put("email", email);
        claims.put("userId", userId.toString());

        JwtBuilder builder = Jwts.builder()
                .setId(email)
                .setIssuedAt(now)
                .setClaims(claims)
                .signWith(signingKey, signatureAlgorithm);

        if (ttlMillis > 0) {
            long expMillis = nowMillis + ttlMillis;
            Date exp = new Date(expMillis);
            builder.setExpiration(exp);
        }

        return builder.compact();
    }

    public Map<String, Object> decodeJwt(String jwt) {
        Key signingKey = new SecretKeySpec(secretKey.getBytes(), SignatureAlgorithm.HS256.getJcaName());

        return Jwts.parserBuilder()
                .setSigningKey(signingKey)
                .build()
                .parseClaimsJws(jwt)
                .getBody();
    }

}
