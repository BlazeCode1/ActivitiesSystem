package org.example.activitiessystem.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotEmpty(message = "User username cannot be Empty")
    @Column(columnDefinition = "varchar(100) not null unique")
    private String username;

    @Email(message = "Invalid Email")
    @NotEmpty(message = "Email Cannot Be Empty")
    @Column(columnDefinition = "varchar(100) not null unique")
    private String email;

    @NotEmpty(message = "User role Cannot Be Empty")
    @Pattern(regexp = "^(admin|user)$" ,message ="User Can Only Be Admin/User")
    @Column(columnDefinition = "varchar(10) not null")
    private String role;

    @NotEmpty(message = "User Password Cannot Be Empty")
    @Pattern(regexp = "^(?=.*?[A-Z])(?=.*[a-z])(?=.*[!@#&()–{}:;',?/*~$^+=<>]).{8,}$")
    @Column(columnDefinition = "varchar(255) not null")
    private String password;
    @NotEmpty(message = "User district cannot Be Empty")
    @Column(columnDefinition = "varchar(100) not null")
    private String district;
    @Column(columnDefinition = "int default 0")
    private Integer points;
    @Column(columnDefinition = "text")
    private String completedChallenges;

    @Column(columnDefinition = "boolean not null default false")
    private Boolean isSubscriber = false;
    @CreationTimestamp
    private Instant created_at;

    @UpdateTimestamp
    private Instant updated_at;



}
