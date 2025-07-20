package com.apis.postgrestesting.entity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDate;

@Table(name="userssss")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
public class UsersEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long userId;

    @Column(name="username", nullable = false)
    private String username;

    @Column(name="password", nullable = false)
    private String password;

    @Column(name = "email", nullable = false,unique = true)
    private String email;

    @Column(name="profession", nullable = false)
    private String profession;

    @Column(name="dob", nullable = false)
    private LocalDate dob;

    @Column(name = "mobile_number", nullable = false, unique = true)
    private String mobileNumber;

}