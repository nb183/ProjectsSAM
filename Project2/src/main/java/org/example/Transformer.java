package org.example;

import soot.*;
import soot.toolkits.graph.BriefUnitGraph;
import soot.toolkits.graph.UnitGraph;
import soot.toolkits.scalar.FlowSet;
import java.util.Map;

public class Transformer extends BodyTransformer {
    @Override
    protected void internalTransform(Body body, String phaseName, Map<String, String> options) {
        SootMethod method = body.getMethod();

        if (method.getDeclaringClass().getName().equals("TestClass") &&
                method.getName().equals("testMethod")) {
            System.out.println("Analyzing method: " + body.getMethod().getSignature());

            UnitGraph cfg = new BriefUnitGraph(body);
            ReachingDefinitions analysis = new ReachingDefinitions(cfg);

            // Initializing an iterator to loop over the statement units

            for (Unit s : cfg) {
                System.out.print("Statement: " + s);

                int d = 50 - s.toString().length();
                while (d > 0) {
                    System.out.print(".");
                    d--;
                }
                // Retrieving the flow before a jimple statement, i.e. entry set
                FlowSet<Local> set = analysis.getFlowBefore(s);

                System.out.print("\t\t\tEntry-> {");
                int count = 0;
                int size = set.size();
                for (Local local : set) {
                    System.out.print(local.getName());
                    if (count == size - 1){
                        continue;
                    }
                    System.out.print(", ");
                    count += 1;
                }
                System.out.print("}");
                // Retrieving the flow before a jimple statement, i.e. exit set
                set = analysis.getFlowAfter(s);

                System.out.print("\tExit-> {");
                count = 0;
                size = set.size();
                for (Local local : set) {

                    System.out.print(local.getName());
                    if (count == size - 1){
                        continue;
                    }
                    System.out.print(", ");
                    count += 1;
                }
                System.out.println("}");
            }
        }
    }
}
