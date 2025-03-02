package org.example;

import soot.*;
import soot.jimple.*;
import soot.jimple.internal.JAssignStmt;
import soot.options.Options;
import soot.util.Chain;

import java.util.Collections;
import java.util.Map;

public class HeapAnalysis extends BodyTransformer {

    public static void main(String[] args) {
        // Initialize Soot
        G.reset();

        Options.v().set_prepend_classpath(true);
        Options.v().set_allow_phantom_refs(true);
        Options.v().set_output_format(Options.output_format_jimple);
        Options.v().set_verbose(false);

        // False handles the intra-procedural analysis rather than the whole-program analysis
        Options.v().set_whole_program(false);

        // Set default target classes directory if not provided as argument
        String classesDirectory = args.length > 0 ? args[0] : "target/classes/org/example/classes";
        Options.v().set_process_dir(Collections.singletonList(classesDirectory));

        Scene.v().loadNecessaryClasses();

        // Add a transformation to the Jimple transformation pack (jtp)
        PackManager.v().getPack("jtp").add(new Transform("jtp.heapAnalysis", new HeapAnalysis()));

        // Run Soot with PackManager
        PackManager.v().runPacks();
    }

    @Override
    protected void internalTransform(Body body, String phaseName, Map<String, String> options) {
        if (body == null) {
            return;
        }

        SootMethod method = body.getMethod();
        System.out.println("\nAnalyze method: " + method.getSignature());
        System.out.println("-".repeat(100));

        // Retrieve Jimple method body
        Chain<Unit> units = body.getUnits();

        // Analyze statements for heap load/store
        for (Unit unit : units) {
            if (unit instanceof JAssignStmt) {
                analyzeHeapAccess((JAssignStmt) unit);
            }
        }
    }

    // Method to analyze heap access
    private void analyzeHeapAccess(JAssignStmt stmt) {
        Value lhs = stmt.getLeftOp(); // Returns objects of type Value
        Value rhs = stmt.getRightOp();


        String lhsType = getHeapAccessType(lhs);
        String rhsType = getHeapAccessType(rhs);

        if (lhsType != null) {
            System.out.println("Statement " + stmt + " ===> HEAP_WRITE (" + lhsType + ")");
        }
        if (rhsType != null) {
            System.out.println("Statement " + stmt + " ===> HEAP_READ (" + rhsType + ")");
        }
    }

    // Helper method for heap access type checks
    private String getHeapAccessType(Value value) {
        if (value instanceof InstanceFieldRef) {
            return "Instance Field Ref";
        } else if (value instanceof StaticFieldRef) {
            return "Static Field Ref";
        } else if (value instanceof ArrayRef) {
            return "Array Ref";
        }
        return null;
    }
}
