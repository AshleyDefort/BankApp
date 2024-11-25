package bank.models;

public interface Transaction {
  void execute();
  long getTimestamp();
}
