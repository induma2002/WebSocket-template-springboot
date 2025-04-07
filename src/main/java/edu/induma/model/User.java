package edu.induma.model;

import lombok.Data;
import lombok.ToString;

@ToString
@Data
public class User {
    private String name;
    private String email;
    private String password;
}
