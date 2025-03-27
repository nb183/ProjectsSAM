package org.example;

import soot.*;
import soot.options.Options;
import java.util.*;

public class ReachingDefinitionAnalysis{

    public static void main(String[] args) {
        // Initializing Soot
        G.reset();
        Options.v().set_prepend_classpath(true);
        Options.v().set_allow_phantom_refs(true);
        Options.v().set_output_format(Options.output_format_jimple);

        // False handles the intra - procedural analysis rather than the whole program analysis
        Options.v().set_whole_program(false);

       // Setting default target classes directory if not provided as argument
        String classesDirectory = args . length > 0 ? args [0] : "target/classes/org/example/classes" ;
        Options.v().set_process_dir(Collections.singletonList(classesDirectory));

        Scene.v().loadNecessaryClasses();

        // Adding a transformation to the Jimple transformation pack (jtp)
        PackManager.v().getPack("jtp").add(new Transform("jtp.reachingDefs", new Transformer()));

        // Running Soot with PackManager
        PackManager.v().runPacks();
    }
}
