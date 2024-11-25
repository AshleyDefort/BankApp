package bank.models;

public interface Transaction {
  void execute();
  long getTimestamp();
  String getAccount();
  double getAmount();
  String getTypeString();
  String getDestinationAccount();
}
