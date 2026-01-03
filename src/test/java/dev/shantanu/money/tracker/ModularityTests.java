
package dev.shantanu.money.tracker;

import org.junit.jupiter.api.Test;
import org.springframework.modulith.core.ApplicationModules;
import org.springframework.modulith.docs.Documenter;
import org.springframework.modulith.docs.Documenter.CanvasOptions;
import org.springframework.modulith.docs.Documenter.DiagramOptions;
import org.springframework.modulith.docs.Documenter.DiagramOptions.DiagramStyle;


class ModularityTests {

    ApplicationModules modules = ApplicationModules.of(MoneyTrackerApplication.class);

    @Test
    void verifyModularity() {

        // --> Module model
        IO.println(modules.toString());

        // --> Trigger verification
        modules.verify();
    }

    @Test
    void renderDocumentation() {

        var canvasOptions = CanvasOptions.defaults()
                // --> Optionally enable linking of JavaDoc
                // .withApiBase("https://foobar.something")
                ;

        var docOptions = DiagramOptions.defaults()
                .withStyle(DiagramStyle.UML);

        new Documenter(modules) //
                .writeDocumentation(docOptions, canvasOptions);
    }

    @Test
    void writeDocumentationAsPlantUml() {

        new Documenter(modules)
                .writeModulesAsPlantUml()
                .writeIndividualModulesAsPlantUml();
    }

    @Test
    void writeDocumentationSnippets() {

        new Documenter(modules)
                .writeAggregatingDocument();
    }
}