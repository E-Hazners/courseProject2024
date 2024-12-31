public class Customer extends Person {
    private double balance;

    public Customer(String id, String name, double balance) {
        super(id, name);
        this.balance = balance;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public void deposit(double amount) {
        this.balance += amount;
    }

    public boolean withdraw(double amount) {
        if (amount > balance) {
            return false;
        }
        this.balance -= amount;
        return true;
    }
}
