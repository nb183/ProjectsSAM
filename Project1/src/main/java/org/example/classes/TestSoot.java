package org.example.classes;

import java.util.ArrayList;
import java.util.List;

public class TestSoot {

    // Instance fields => heap storage
    private final String name;
    private final int age;
    private double balance;

    // Static field => this is shared among all instances
    private static String bankName = "WSU Bank";

    // Array field => heap storage
    private final int[] transactions;

    // List => heap allocation for dynamic objects
    private final List<String> transactionHistory;

    // Constructor for initialization
    public TestSoot(String name, int age, double initialBalance) {
        this.name = name;
        this.age = age;
        this.balance = initialBalance;
        this.transactions = new int[5]; // Heap allocation
        this.transactionHistory = new ArrayList<>(); // Heap allocation
    }

    // Methods showing Heap operations
    public void deposit(int amount) {
        this.balance += amount;  // Heap store => InstanceFieldRef
        System.out.println(name + " deposited: $" + amount);

        transactions[0] = amount; // Heap store => ArrayRef
        transactionHistory.add("Deposited: $" + amount); // Heap store => dynamic heap allocation
    }

    public void withdraw(int amount) {
        if (amount <= this.balance) {
            this.balance -= amount; // Heap store => InstanceFieldRef
            System.out.println(name + " withdrew: $" + amount);

            transactions[1] = amount; // Heap store => ArrayRef
            transactionHistory.add("Withdrew: $" + amount); // Heap store => dynamic heap allocation
        } else {
            System.out.println(name + " has insufficient funds for");
        }
    }

    public void displayAccountDetails() {
        System.out.println("Account Holder: " + name); // Heap read => InstanceFieldRef
        System.out.println("Age: " + age); // Heap read => InstanceFieldRef
        System.out.println("Balance: $" + balance); // Heap read => InstanceFieldRef
        System.out.println("Bank Name: " + bankName); // Heap read => StaticFieldRef
        System.out.println("Transaction History: " + transactionHistory); // Heap read => ArrayRef & List
    }

    public static void changeBankName(String newBankName) {
        bankName = newBankName; // Heap store => StaticFieldRef
    }

    public static void main(String[] args) {
        // Heap object creation
        TestSoot user1 = new TestSoot("User1",  17, 5000);
        TestSoot user2 = new TestSoot("User2", 23, 3000);

        // Performing operations => heap interactions
        user1.deposit(3000);
        user1.withdraw(1500);
        user1.displayAccountDetails(); // Heap reads

        user2.deposit(6000);
        user2.withdraw(12000);
        user2.displayAccountDetails(); // Heap reads

        // Changing static field => this affects all instances
        TestSoot.changeBankName("Wichita Bank");
        user1.displayAccountDetails(); // Heap read => new static field value
        user2.displayAccountDetails(); // Heap read => new static field value
    }
}

