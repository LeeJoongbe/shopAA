package com.example.shop.entity;

import com.example.shop.constant.Role;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
public class Members {

    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    @Column(name = "members_num")
    private Long num;

    private String name;

    //이멜을 유일하다
    @Column(unique = true)
    private String email;

    private String password;

    private String address;

    @Enumerated(EnumType.STRING)
    private Role role;









}
