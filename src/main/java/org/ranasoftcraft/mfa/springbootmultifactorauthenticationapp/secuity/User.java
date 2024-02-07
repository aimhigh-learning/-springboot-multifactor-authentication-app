package org.ranasoftcraft.mfa.springbootmultifactorauthenticationapp.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * @author sandeep.rana
 */
@Entity(name = "users")
@Table @Data
public class User implements Serializable {


    @Id
    @Column(unique = true, nullable = false)
    private String username;

    private String password;

    private boolean enable2FA;

    private String secrete;

}
