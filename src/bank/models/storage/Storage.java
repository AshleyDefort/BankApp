package bank.models.storage;

import java.util.ArrayList;
import bank.models.Account;
import bank.models.User;
import bank.models.Transaction;

public class Storage {

    private static Storage instance;

    private ArrayList<User> users;
    private ArrayList<Account> accounts;
    private ArrayList<Transaction> transactions;

    private Storage() {
        this.users = new ArrayList<>();
        this.accounts = new ArrayList<>();
        this.transactions = new ArrayList<>();
    }

    public static Storage getInstance() {
        if (instance == null) {
            instance = new Storage();
        }
        return instance;
    }

    public boolean addUser(User user) {
        for (User u : this.users) {
            if (u.getId()==user.getId()) {
                return false; 
            }
        }
        this.users.add(user);
        return true;
    }

    public User getUser(String id) {
        for (User user : this.users) {
            if (user.getId() == Integer.parseInt(id)) {
                return user;
            }
        }
        return null; 
    }

    public boolean delUser(String id) {
        return this.users.removeIf(user -> user.getId() == Integer.parseInt(id));
    }


    public boolean addAccount(Account account) {
        for (Account a : this.accounts) {
            if (a.getId().equals(account.getId())) {
                return false; // Cuenta con ID duplicado
            }
        }
        this.accounts.add(account);
        return true;
    }

    public Account getAccount(String id) {
        for (Account account : this.accounts) {
            if (account.getId().equals(id)) {
                return account;
            }
        }
        return null; 
    }

    public boolean delAccount(String id) {
        return this.accounts.removeIf(account -> account.getId().equals(id));
    }


    public void addTransaction(Transaction transaction) {
        this.transactions.add(transaction);
    }

    public ArrayList<Transaction> getTransactions() {
        return new ArrayList<>(this.transactions);
    }


    public ArrayList<User> getUsers() {
        return new ArrayList<>(this.users); 
    }

    public ArrayList<Account> getAccounts() {
        return new ArrayList<>(this.accounts); 
    }
}
