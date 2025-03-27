package org.example;

import soot.Local;
import soot.Unit;
import soot.Value;
import soot.jimple.DefinitionStmt;
import soot.toolkits.graph.UnitGraph;
import soot.toolkits.scalar.ArraySparseSet;
import soot.toolkits.scalar.BackwardFlowAnalysis;
import soot.toolkits.scalar.FlowSet;

class ReachingDefinitions extends BackwardFlowAnalysis<Unit, FlowSet<Local>> {

    public ReachingDefinitions(UnitGraph graph) {
        super(graph);
        doAnalysis();
    }

    @Override
    protected FlowSet<Local> newInitialFlow() {
        return new ArraySparseSet<>();
    }

    @Override
    protected FlowSet<Local> entryInitialFlow() {
        return new ArraySparseSet<>();
    }


    @Override
    protected void merge(FlowSet<Local> in1, FlowSet<Local> in2, FlowSet<Local> out) {
        in1.union(in2, out);
    }

    @Override
    protected void copy(FlowSet<Local> source, FlowSet<Local> dest) {
        source.copy(dest);
    }

    @Override
    protected void flowThrough(FlowSet<Local> in, Unit unit, FlowSet<Local> out) {
        // Implementing the actual flow logic
        out.clear();
        out.union(in);

        if (unit instanceof DefinitionStmt defStmt) {
            Value leftOp = defStmt.getLeftOp();

            if (leftOp instanceof Local local) {

                // Removing previous definitions of the same local variable
                FlowSet<Local> temp = new ArraySparseSet<>();
                for (Local l : out) {
                    if (!l.equals(local)) {
                        temp.add(l);
                    }
                }
                out.clear();

                // Adding the new variables to the out set
                out.union(temp);
                out.add(local);
            }
        }
    }
}