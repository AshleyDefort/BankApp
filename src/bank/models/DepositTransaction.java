package bank.models;

public class DepositTransaction implements Transaction {
    private final Account account;
    private final double amount;

    public DepositTransaction(Account account, double amount) {
        this.account = account;
        this.amount = amount;
    }

    @Override
    public void execute() {
        account.deposit(amount);
    }
}
