import java.util.*;

enum TransactionType { WITHDRAW, DEPOSIT, TRANSFER_IN, TRANSFER_OUT }

class Transaction {
    TransactionType type;
    double amount;
    Date date = new Date();
    String details;

    Transaction(TransactionType type, double amount, String details) {
        this.type = type; this.amount = amount; this.details = details;
    }

    public String toString() {
        return String.format("%s of $%.2f on %s %s", type, amount, date,
                (details != null ? "(" + details + ")" : ""));
    }
}

class User {
    String userId, pin;
    double balance;
    List<Transaction> transactions = new ArrayList<>();

    User(String userId, String pin, double balance) {
        this.userId = userId; this.pin = pin; this.balance = balance;
    }

    boolean checkPin(String inputPin) { return pin.equals(inputPin); }
    void addTransaction(Transaction t) { transactions.add(t); }
}

class Bank {
    Map<String, User> users = Map.of(
        "user1", new User("user1", "1234", 1000.0),
        "user2", new User("user2", "4321", 1500.0)
    );

    User login(String id, String pin) {
        User u = users.get(id);
        return (u != null && u.checkPin(pin)) ? u : null;
    }

    User getUser(String id) { return users.get(id); }
}

public class ATM {
    static Scanner sc = new Scanner(System.in);
    static Bank bank = new Bank();

    public static void main(String[] args) {
        System.out.println("Welcome to the Java ATM");

        User user;
        do {
            System.out.print("User ID: ");
            String id = sc.nextLine();
            System.out.print("PIN: ");
            String pin = sc.nextLine();
            user = bank.login(id, pin);
            if (user == null) System.out.println("Invalid credentials. Try again.");
        } while (user == null);

        System.out.println("Login successful! Welcome " + user.userId);

        while (true) {
            System.out.print("""
                \n1. Transaction History
                2. Withdraw
                3. Deposit
                4. Transfer
                5. Quit
                Choice: """);
            switch (sc.nextLine()) {
                case "1" -> showHistory(user);
                case "2" -> processTransaction(user, TransactionType.WITHDRAW);
                case "3" -> processTransaction(user, TransactionType.DEPOSIT);
                case "4" -> transfer(user);
                case "5" -> {
                    System.out.println("Thank you for using the ATM. Goodbye!");
                    return;
                }
                default -> System.out.println("Invalid option.");
            }
        }
    }

    static void showHistory(User u) {
        if (u.transactions.isEmpty()) System.out.println("No transactions.");
        else u.transactions.forEach(System.out::println);
    }

    static void processTransaction(User u, TransactionType type) {
        System.out.print("Enter amount: ");
        double amount = parseAmount();
        if (amount <= 0) return;

        if (type == TransactionType.WITHDRAW && amount > u.balance) {
            System.out.println("Insufficient balance.");
            return;
        }

        u.balance += (type == TransactionType.DEPOSIT ? amount : -amount);
        u.addTransaction(new Transaction(type, amount, null));
        System.out.printf("%s $%.2f. New balance: $%.2f%n",
                type == TransactionType.DEPOSIT ? "Deposited" : "Withdrawn", amount, u.balance);
    }

    static void transfer(User sender) {
        System.out.print("Recipient ID: ");
        String recipientId = sc.nextLine();
        User recipient = bank.getUser(recipientId);
        if (recipient == null) {
            System.out.println("Recipient not found.");
            return;
        }

        System.out.print("Amount to transfer: ");
        double amount = parseAmount();
        if (amount <= 0 || amount > sender.balance) {
            System.out.println("Invalid or insufficient funds.");
            return;
        }

        sender.balance -= amount;
        recipient.balance += amount;

        sender.addTransaction(new Transaction(TransactionType.TRANSFER_OUT, amount, "To: " + recipientId));
        recipient.addTransaction(new Transaction(TransactionType.TRANSFER_IN, amount, "From: " + sender.userId));

        System.out.printf("Transferred $%.2f to %s. New balance: $%.2f%n", amount, recipientId, sender.balance);
    }

    static double parseAmount() {
        try {
            double amt = Double.parseDouble(sc.nextLine());
            if (amt <= 0) throw new Exception();
            return amt;
        } catch (Exception e) {
            System.out.println("Invalid amount.");
            return -1;
        }
    }
}
