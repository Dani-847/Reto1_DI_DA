package org.drk.user;

import lombok.Data;

/**
 * Clase de modelo que representa a un usuario del sistema.
 */

@Data
public class User {

    private int id;
    private String email;
    private String password;

}
