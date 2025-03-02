package org.example;

import soot.*;
import soot.jimple.*;
import soot.jimple.internal.JAssignStmt;
import soot.options.Options;
import soot.util.Chain;

import java.util.Collections;

public class HeapAnalysis {
    public static void main(String[] args) {
        // Initialize Soot
        G.reset();

        Options.v().set_prepend_classpath(true);
        Options.v().set_allow_phantom_refs(true);
        Options.v().set_output_format(Options.output_format_jimple);
        Options.v().set_verbose(false);

        // False handles the intra procedural analysis rather than the whole program analysis
        Options.v().set_whole_program(false);

        // Set default target classes directory if not provided as argument
        String classesDirectory = args.length > 0 ? args[0] : "target/classes/org/example/classes";
        Options.v().set_process_dir(Collections.singletonList(classesDirectory));

        Scene.v().loadNecessaryClasses();

        // We can, as per our requirements, force-load specific test classes
        Scene.v().addBasicClass("org.example.classes.SootAccount", SootClass.BODIES);

        // Load all application classes
        for (SootClass sc : Scene.v().getApplicationClasses()) {
            if (!sc.isConcrete()) continue;

            System.out.println("Analyze class: " + sc.getName());

            for (SootMethod method : sc.getMethods()) {
                if (!method.isConcrete()) continue;

                System.out.println("\nAnalyze method: " + method.getSignature());
                System.out.println("-".repeat(100));

                // Retrieve Jimple method body
                Body body = method.retrieveActiveBody();

                // Parse body  into call Chain units
                Chain<Unit> units = body.getUnits();

                // Analyze statements for heap load/store
                for (Unit unit : units) {
                    // We are only concerned regarding JAssignStmts among all Stmt classes
                    if (unit instanceof JAssignStmt stmt){
                        // To identify statements that access the heap, we need to understand
                        // what the left-hand-side (LHS) and RHS operands are for each statement
                        // call methods getLeftOp() and getRightOp() on each statement object

                        Value lhs = stmt.getLeftOp(); // Returns objects of type value
                        Value rhs = stmt.getRightOp();

                        // Perform check for heap write and read
                        if (lhs instanceof InstanceFieldRef || lhs instanceof StaticFieldRef || lhs instanceof ArrayRef ){
                            System.out.println(stmt + " ===> " + "HEAP_WRITE");
                        }

                        if (rhs instanceof InstanceFieldRef || rhs instanceof StaticFieldRef || rhs instanceof ArrayRef ){
                            System.out.println(stmt + " ===> " + "HEAP_READ");
                        }
                    }
                }
            }
            System.out.println();
        }
    }
}