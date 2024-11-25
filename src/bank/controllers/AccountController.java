package bank.controllers;

import bank.models.Account;
import bank.models.User;
import bank.models.storage.Storage;
import bank.controllers.utils.Response;
import bank.controllers.utils.Status;

import java.util.regex.Pattern;

public class AccountController {

    private static final String ACCOUNT_ID_PATTERN = "\\d{3}-\\d{6}-\\d{2}";

    public static Response createAccount(String userId, String initialBalance) {
        try {
            int userIdInt;
            double initialBalanceDouble;

            // Validar que el ID del usuario sea un número entero válido
            try {
                userIdInt = Integer.parseInt(userId);
                if (userIdInt < 0 || userId.length() > 9) {
                    return new Response("User ID must be a positive integer with a maximum of 9 digits", Status.BAD_REQUEST);
                }
            } catch (NumberFormatException ex) {
                return new Response("User ID must be numeric", Status.BAD_REQUEST);
            }

            // Validar saldo inicial
            try {
                initialBalanceDouble = Double.parseDouble(initialBalance);
                if (initialBalanceDouble < 0) {
                    return new Response("Initial balance must be non-negative", Status.BAD_REQUEST);
                }
            } catch (NumberFormatException ex) {
                return new Response("Initial balance must be numeric", Status.BAD_REQUEST);
            }

            Storage storage = Storage.getInstance();

            // Validar que el usuario exista
            User user = storage.getUser(userIdInt);
            if (user == null) {
                return new Response("User not found", Status.NOT_FOUND);
            }

            // Generar ID único para la cuenta
            String accountId = generateAccountId(storage);

            // Crear y almacenar la cuenta
            Account newAccount = new Account(accountId, user, initialBalanceDouble);
            if (!storage.addAccount(newAccount)) {
                return new Response("Account could not be created", Status.INTERNAL_SERVER_ERROR);
            }

            return new Response("Account created successfully", newAccount ,Status.CREATED);
        } catch (Exception ex) {
            return new Response("Unexpected error", Status.INTERNAL_SERVER_ERROR);
        }
    }

    public static Response getAccount(String accountId) {
        try {
            // Validar el formato del ID de la cuenta
            if (!Pattern.matches(ACCOUNT_ID_PATTERN, accountId)) {
                return new Response("Invalid account ID format", Status.BAD_REQUEST);
            }

            Storage storage = Storage.getInstance();

            // Buscar la cuenta
            Account account = storage.getAccount(accountId);
            if (account == null) {
                return new Response("Account not found", Status.NOT_FOUND);
            }

            return new Response("Account found", account, Status.OK);
        } catch (Exception ex) {
            return new Response("Unexpected error", Status.INTERNAL_SERVER_ERROR);
        }
    }

    public static Response deposit(String accountId, String amount) {
        try {
            double amountDouble;

            // Validar el formato del ID de la cuenta
            if (!Pattern.matches(ACCOUNT_ID_PATTERN, accountId)) {
                return new Response("Invalid account ID format", Status.BAD_REQUEST);
            }

            // Validar la cantidad
            try {
                amountDouble = Double.parseDouble(amount);
                if (amountDouble <= 0) {
                    return new Response("Deposit amount must be positive", Status.BAD_REQUEST);
                }
            } catch (NumberFormatException ex) {
                return new Response("Amount must be numeric", Status.BAD_REQUEST);
            }

            Storage storage = Storage.getInstance();

            // Buscar la cuenta
            Account account = storage.getAccount(accountId);
            if (account == null) {
                return new Response("Account not found", Status.NOT_FOUND);
            }

            // Realizar el depósito
            account.deposit(amountDouble);
            return new Response("Deposit successful", account ,Status.OK);
        } catch (Exception ex) {
            return new Response("Unexpected error", Status.INTERNAL_SERVER_ERROR);
        }
    }

    public static Response withdraw(String accountId, String amount) {
        try {
            double amountDouble;

            // Validar el formato del ID de la cuenta
            if (!Pattern.matches(ACCOUNT_ID_PATTERN, accountId)) {
                return new Response("Invalid account ID format", Status.BAD_REQUEST);
            }

            // Validar la cantidad
            try {
                amountDouble = Double.parseDouble(amount);
                if (amountDouble <= 0) {
                    return new Response("Withdraw amount must be positive", Status.BAD_REQUEST);
                }
            } catch (NumberFormatException ex) {
                return new Response("Amount must be numeric", Status.BAD_REQUEST);
            }

            Storage storage = Storage.getInstance();

            // Buscar la cuenta
            Account account = storage.getAccount(accountId);
            if (account == null) {
                return new Response("Account not found", Status.NOT_FOUND);
            }

            // Intentar realizar el retiro
            if (!account.withdraw(amountDouble)) {
                return new Response("Insufficient funds", Status.BAD_REQUEST);
            }

            return new Response("Withdrawal successful", account ,Status.OK);
        } catch (Exception ex) {
            return new Response("Unexpected error", Status.INTERNAL_SERVER_ERROR);
        }
    }

    private static String generateAccountId(Storage storage) {
        String id;
        do {
            id = String.format("%03d-%06d-%02d",
                (int) (Math.random() * 1000),
                (int) (Math.random() * 1000000),
                (int) (Math.random() * 100));
        } while (storage.getAccount(id) != null);
        return id;
    }
}
