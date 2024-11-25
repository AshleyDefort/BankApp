package bank.controllers;

import bank.controllers.utils.Response;
import bank.controllers.utils.Status;
import bank.models.User;
import bank.models.storage.Storage;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class UserController {

    // Crear usuario
    public static Response createUser(String id, String firstname, String lastname, String age) {
        try {
            // Validación del ID
            int userId;
            try {
                userId = Integer.parseInt(id);
                if (userId < 0 || String.valueOf(userId).length() > 9) {
                    return new Response("User ID must be a positive integer with up to 9 digits", Status.BAD_REQUEST);
                }
            } catch (NumberFormatException ex) {
                return new Response("User ID must be numeric", Status.BAD_REQUEST);
            }

            // Validación del nombre
            if (firstname == null || firstname.trim().isEmpty()) {
                return new Response("Firstname must not be empty", Status.BAD_REQUEST);
            }

            // Validación del apellido
            if (lastname == null || lastname.trim().isEmpty()) {
                return new Response("Lastname must not be empty", Status.BAD_REQUEST);
            }

            // Validación de la edad
            int userAge;
            try {
                userAge = Integer.parseInt(age);
                if (userAge < 18) {
                    return new Response("Age must be 18 or older", Status.BAD_REQUEST);
                }
            } catch (NumberFormatException ex) {
                return new Response("Age must be numeric", Status.BAD_REQUEST);
            }

            // Verificar unicidad del ID en el almacenamiento
            Storage storage = Storage.getInstance();
            Optional<User> existingUser = storage.getUsers().stream()
                    .filter(user -> user.getId() == userId)
                    .findFirst();

            if (existingUser.isPresent()) {
                return new Response("A user with this ID already exists", Status.CONFLICT);
            }

            // Crear y guardar el usuario
            User newUser = new User(userId, firstname, lastname, userAge);
            storage.addUser(newUser);

            return new Response("User created successfully", newUser ,Status.CREATED);
        } catch (Exception ex) {
            return new Response("Unexpected error: " + ex.getMessage(), Status.INTERNAL_SERVER_ERROR);
        }
    }

    // Obtener usuario por ID
    public static Response getUser(String id) {
        try {
            int userId;
            try {
                userId = Integer.parseInt(id);
                if (userId < 0) {
                    return new Response("User ID must be positive", Status.BAD_REQUEST);
                }
            } catch (NumberFormatException ex) {
                return new Response("User ID must be numeric", Status.BAD_REQUEST);
            }

            Storage storage = Storage.getInstance();
            User user = storage.getUser(userId);

            if (user == null) {
                return new Response("User not found", Status.NOT_FOUND);
            }

            return new Response("User found", user ,Status.OK);
        } catch (Exception ex) {
            return new Response("Unexpected error: " + ex.getMessage(), Status.INTERNAL_SERVER_ERROR);
        }
    }

    // Obtener todos los usuarios ordenados por ID
    public static Response getAllUsers() {
        try {
            Storage storage = Storage.getInstance();
            List<User> sortedUsers = storage.getUsers();
            sortedUsers.sort(Comparator.comparingInt(User::getId));

            return new Response("Users retrieved successfully", sortedUsers ,Status.OK);
        } catch (Exception ex) {
            return new Response("Unexpected error: " + ex.getMessage(), Status.INTERNAL_SERVER_ERROR);
        }
    }
}
