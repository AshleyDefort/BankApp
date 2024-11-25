package bank.models;
  

public class TransferTransaction implements Transaction {
    private final Account sourceAccount;
    private final Account destinationAccount;
    private final double amount;

    public TransferTransaction(Account sourceAccount, Account destinationAccount, double amount) {
        this.sourceAccount = sourceAccount;
        this.destinationAccount = destinationAccount;
        this.amount = amount;
    }

    @Override
    public void execute() {
        if (sourceAccount.withdraw(amount)) {
            destinationAccount.deposit(amount);
        } else {
            throw new IllegalArgumentException("Insufficient funds in source account.");
        }
    }
}
