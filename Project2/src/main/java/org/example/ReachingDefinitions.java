package org.example;

import soot.Unit;
import soot.Value;
import soot.jimple.DefinitionStmt;
import soot.Local;
import soot.toolkits.graph.UnitGraph;
import soot.toolkits.scalar.ArraySparseSet;
import soot.toolkits.scalar.FlowSet;
import soot.toolkits.scalar.ForwardFlowAnalysis;

public class ReachingDefinitions
        extends ForwardFlowAnalysis<Unit, FlowSet<Unit>> {

    public ReachingDefinitions(UnitGraph graph) {
        super(graph);
        doAnalysis();
    }

    @Override
    protected FlowSet<Unit> newInitialFlow() {
        return new ArraySparseSet<>();
    }

    @Override
    protected FlowSet<Unit> entryInitialFlow() {
        return new ArraySparseSet<>();
    }

    @Override
    protected void merge(FlowSet<Unit> in1, FlowSet<Unit> in2,
                         FlowSet<Unit> out) {
        in1.union(in2, out);
    }

    @Override
    protected void copy(FlowSet<Unit> src, FlowSet<Unit> dest) {
        src.copy(dest);
    }

    @Override
    protected void flowThrough(FlowSet<Unit> in, Unit unit,
                               FlowSet<Unit> out) {
        // Implementing the actual flow logic
        in.copy(out);

        if (unit instanceof DefinitionStmt defStmt) {
            Value leftOp = defStmt.getLeftOp();
            if (leftOp instanceof Local) {

                // Removing previous definitions of the same local variable
                FlowSet<Unit> toKill = new ArraySparseSet<>();
                for (Unit u : in) {
                    DefinitionStmt d = (DefinitionStmt) u;
                    if (d.getLeftOp().equivTo(leftOp)) {
                        toKill.add(u);
                    }
                }
                out.difference(toKill, out);

                // Adding the new variables to the out set
                out.add(unit);
            }
        }
    }
}
