package bank.controllers;

import bank.models.Account;
import bank.models.Transaction;
import bank.models.TransferTransaction;
import bank.models.DepositTransaction;
import bank.controllers.utils.Response;
import bank.controllers.utils.Status;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class TransactionController {

    private static List<Account> accounts = new ArrayList<>();  // Almacena las cuentas
    private static List<Transaction> transactions = new ArrayList<>();  // Almacena las transacciones

    // Crear una transacción
    public static Response createTransaction(String type, String sourceAccountId, String destinationAccountId, String amount) {
        try {
            double amountValue = parseAmount(amount);
            if (amountValue <= 0) {
                return new Response("Amount must be positive", Status.BAD_REQUEST);
            }

            // Obtener cuentas
            Account sourceAccount = getAccountById(sourceAccountId);
            if (sourceAccount == null) {
                return new Response("Source account not found", Status.NOT_FOUND);
            }

            Account destinationAccount = null;
            if (type.equals("TRANSFER")) {
                destinationAccount = getAccountById(destinationAccountId);
                if (destinationAccount == null) {
                    return new Response("Destination account not found", Status.NOT_FOUND);
                }
            }

            // Crear transacción según tipo
            Transaction transaction;
            if (type.equals("TRANSFER")) {
                transaction = new TransferTransaction(sourceAccount, destinationAccount, amountValue);
            } else if (type.equals("DEPOSIT")) {
                transaction = new DepositTransaction(sourceAccount, amountValue);
            } else {
                return new Response("Invalid transaction type", Status.BAD_REQUEST);
            }

            // Ejecutar la transacción
            transaction.execute();

            // Registrar la transacción
            transactions.add(transaction);
            // Ordenar transacciones por antigüedad (de más reciente a más antigua)
            transactions.sort(Comparator.comparingLong(Transaction::getTimestamp).reversed());

            return new Response("Transaction executed successfully", Status.CREATED);
        } catch (IllegalArgumentException ex) {
            return new Response("Transaction failed: " + ex.getMessage(), Status.BAD_REQUEST);
        } catch (Exception ex) {
            return new Response("Unexpected error", Status.INTERNAL_SERVER_ERROR);
        }
    }

    // Método para parsear el monto
    private static double parseAmount(String amount) {
        try {
            return Double.parseDouble(amount);
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException("Amount must be numeric");
        }
    }

    // Obtener cuenta por ID
    private static Account getAccountById(String accountId) {
        for (Account account : accounts) {
            if (account.getId().equals(accountId)) {
                return account;
            }
        }
        return null;
    }

    // Obtener usuarios de manera ordenada por ID
    public static List<Account> getAccounts() {
        Collections.sort(accounts, Comparator.comparing(Account::getId));
        return accounts;
    }

    // Obtener transacciones ordenadas por antigüedad
    public static List<Transaction> getTransactions() {
        return transactions;
    }
}
