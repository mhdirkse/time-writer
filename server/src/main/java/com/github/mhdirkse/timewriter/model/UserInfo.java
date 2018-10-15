package com.github.mhdirkse.timewriter.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Entity
@Table(name = "users")
@Getter
@Setter
public class UserInfo implements Serializable {
    private static final long serialVersionUID = -1335379990646419090L;

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column
    private String password;
}
