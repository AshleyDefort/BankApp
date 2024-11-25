package bank.models;


public class TransferTransaction implements Transaction {
    private final Account sourceAccount;
    private final Account destinationAccount;
    private final double amount;
    private final long timestamp;  // Crear un timestamp

    public TransferTransaction(Account sourceAccount, Account destinationAccount, double amount) {
        this.sourceAccount = sourceAccount;
        this.destinationAccount = destinationAccount;
        this.amount = amount;
        this.timestamp = System.currentTimeMillis(); 
    }

    @Override
    public void execute() {
        if (sourceAccount.withdraw(amount)) {
            destinationAccount.deposit(amount);
        } else {
            throw new IllegalArgumentException("Insufficient funds in source account.");
        }
    }

    @Override
    public long getTimestamp() {
        return timestamp;  // Retornar el timestamp
    }

    @Override
    public String getAccount() {
        return sourceAccount.getId();
    }

    @Override
    public double getAmount() {
        return amount;
    }

    @Override
    public String getTypeString() {
        return "TRANSFER";
    }

    @Override
    public String getDestinationAccount() {
        return destinationAccount.getId();
    }


}
