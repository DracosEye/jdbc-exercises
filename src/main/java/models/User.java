package models;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
@Setter
public class User {
    private long id;
    private String username;
    private String email;
    private String password;

}