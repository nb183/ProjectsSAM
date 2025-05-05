package org.example;

import soot.Body;
import soot.BodyTransformer;
import soot.SootMethod;
import soot.Unit;
import soot.toolkits.graph.BriefUnitGraph;
import soot.toolkits.graph.UnitGraph;
import soot.toolkits.scalar.FlowSet;
import java.util.Map;

// This will be a Forward Reaching Definition Transformer
public class Transformer extends BodyTransformer {
    @Override
    protected void internalTransform(Body body,
                                     String phaseName,
                                     Map<String, String> options) {
        SootMethod m = body.getMethod();

        // match TestClass.testMethod
        if (!m.getDeclaringClass().getShortName().equals("TestClass")
                || !m.getName().equals("testMethod")) {
            return;
        }
        System.out.println("Analyzing method: " + body.getMethod().getSignature());

        UnitGraph cfg = new BriefUnitGraph(body);
        ReachingDefinitions rd = new ReachingDefinitions(cfg);

        int columnWidth = 60;

        // Initializing an iterator to loop over the statement units

        for (Unit s : cfg) {
            String stmt = "Statement: " + s;
            System.out.print(stmt);
            for (int i = stmt.length(); i < columnWidth; i++) System.out.print('.');

            // Retrieving the flow before a jimple statement, i.e. exit set
            System.out.print("\tEntry-> {");
            printLocalNames(rd.getFlowBefore(s));
            System.out.print("}");

            // Retrieving the flow after a jimple statement, i.e. exit set
            System.out.print("\tExit-> {");
            printLocalNames(rd.getFlowAfter(s));
            System.out.println("}");
        }
    }

    // Helper: print comma‑separated Local names contained in a FlowSet<Unit>
    private static void printLocalNames(FlowSet<Unit> defs) {
        java.util.LinkedHashSet<String> vars = new java.util.LinkedHashSet<>();
        for (Unit u : defs) {
            if (u instanceof soot.jimple.DefinitionStmt d) {
                soot.Value lhs = d.getLeftOp();
                if (lhs instanceof soot.Local l) {
                    String v = l.getName()
                            .replaceFirst("\\d+$","");   // drop digits
                    if (v.startsWith("$") || v.equals("this")) continue;
                    vars.add(v);
                }
            }
        }
        int i=0, n=vars.size();
        for (String v : vars) {
            System.out.print(v);
            if (++i < n) System.out.print(", ");
        }
    }
}

