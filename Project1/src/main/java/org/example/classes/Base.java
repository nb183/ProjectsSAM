package org.example.classes;

import java.util.ArrayList;
import java.util.List;

public class Base {

    // Instance fields (heap storage)
    private String name;
    private int age;
    private double balance;

    // Static field (shared among all instances)
    private static String bankName = "WSU Bank";

    // Array field (heap storage)
    private int[] transactions;

    // List (heap allocation for dynamic objects)
    private List<String> history;

    // Constructor
    public Base(String name, int age, double initialBalance) {
        this.name = name;
        this.age = age;
        this.balance = initialBalance;
        this.transactions = new int[5]; // Heap allocation
        this.history = new ArrayList<>(); // Heap allocation
    }

    // Methods showing Heap operations
    public void deposit(int amount) {
        this.balance += amount;  // Heap store (InstanceFieldRef)
        System.out.println(name + " deposited: $" + amount);

        transactions[0] = amount; // Heap store (ArrayRef)
        history.add("Deposited: $" + amount); // Heap store (dynamic heap allocation)
    }

    public void withdraw(int amount) {
        if (amount <= this.balance) {
            this.balance -= amount; // Heap store (InstanceFieldRef)
            System.out.println(name + " withdrew: $" + amount);

            transactions[1] = amount; // Heap store (ArrayRef)
            history.add("Withdrew: $" + amount); // Heap store (dynamic heap allocation)
        } else {
            System.out.println("Insufficient funds for " + name);
        }
    }

    public void printAccountDetails() {
        System.out.println("Account Holder: " + name); // Heap read (InstanceFieldRef)
        System.out.println("Age: " + age); // Heap read (InstanceFieldRef)
        System.out.println("Balance: $" + balance); // Heap read (InstanceFieldRef)
        System.out.println("Bank Name: " + bankName); // Heap read (StaticFieldRef)
        System.out.println("Transaction History: " + history); // Heap read (ArrayRef & List)
    }

    public static void changeBankName(String newBankName) {
        bankName = newBankName; // Heap store (StaticFieldRef)
    }

    public static void main(String[] args) {
        // Heap object creation
        Base user1 = new Base("Alice", 30, 5000);
        Base user2 = new Base("Bob", 25, 3000);

        // Performing operations (heap interactions)
        user1.deposit(3000);
        user1.withdraw(1500);
        user1.printAccountDetails(); // Heap read operations

        user2.deposit(6000);
        user2.withdraw(12000);
        user2.printAccountDetails(); // Heap read operations

        // Changing static field (affects all instances)
        Base.changeBankName("Wichita Bank");
        user1.printAccountDetails(); // Heap read (new static field value)
        user2.printAccountDetails(); // Heap read (new static field value)
    }
}

