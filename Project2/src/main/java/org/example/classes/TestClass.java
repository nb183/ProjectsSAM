package org.example.classes;

public class TestClass {

    public void testMethod() {
        // Variable Definition Statements
        int a;                  // Statement 1
        int b;                  // Statement 2

        // Variable Assignment Statements
        a = 10;                 // Statement 3
        b = 20;                 // Statement 4

        // Variable Definition + Assignment Statements
        int c = 0;                // Statement 5
        int d = 5;                // Statement 6
        int e = 0;                // Statement 7

        // Calculation statements
        a = a + b;                // Statement 8
        b = b - 5;                // Statement 9

        // Branching statements
        if (a > 15) {             // Statement 10
            c = a + b;            // Statement 11  -> if-branch assignment
            d = c * 2;            // Statement 12 ->  if-branch assignment
        } else {
            c = a - b;            // Statement 13 -> else-branch assignment
            d = c / 2;            // Statement 14 -> else-branch assignment
        }

        // Loop statements
        int i = 0;                // Statement 15 -> initialization
        while (i < 5) {           // Statement 16 -> condition
            e = e + i;            // Statement 17 -> assignment
            i = i + 1;            // Statement 18 -> increment
        }

        // Additional calculation + assignments
        int result = a + b + c;   // Statement 19
        result = result * d;      // Statement 20

        // Final branching Statement
        if (result > 100) {       // Statement 21 -> condition
            result = 100;         // Statement 22 -> assignment
        }

        // Print the result
        System.out.println(result); // Statement 23: Calling a method
    }
}