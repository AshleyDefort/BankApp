package bank.models;

public class DepositTransaction implements Transaction {
    private final Account account;
    private final double amount;
    private final long timestamp;

    public DepositTransaction(Account account, double amount) {
        this.account = account;
        this.amount = amount;
        this.timestamp = System.currentTimeMillis();
    }

    @Override
    public void execute() {
        account.deposit(amount);
    }

    @Override
    public long getTimestamp() {
        return timestamp;
    }

    @Override
    public String getAccount() {
        return account.getId();
    }
    
    @Override
    public double getAmount() {
        return amount;
    }

    @Override
    public String getTypeString() {
        return "DEPOSIT";
    }

    @Override
    public String getDestinationAccount() {
        return null;
    }

}
