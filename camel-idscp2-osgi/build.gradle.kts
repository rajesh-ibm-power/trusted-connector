import org.gradle.plugins.ide.idea.model.IdeaModel

@Suppress("UNCHECKED_CAST")
val libraryVersions = rootProject.ext.get("libraryVersions") as Map<String, String>

description = "Camel IDSCP2 Component"
version = libraryVersions["idscp2"] ?: error("IDSCP2 version not specified")

plugins {
    id("com.github.gmazzo.buildconfig") version "2.0.2"
}

apply(plugin = "idea")

buildConfig {
    sourceSets.getByName("main") {
        packageName("de.fhg.aisec.ids.informationmodelmanager")
        buildConfigField("String", "INFOMODEL_VERSION",
                "\"${libraryVersions["infomodel"] ?: error("Infomodel version not available")}\"")
    }
}

configure<IdeaModel> {
    module {
        // mark as generated sources for IDEA
        generatedSourceDirs.add(File("${buildDir}/generated/source/buildConfig/main/main"))
    }
}

dependencies {
    providedByBundle(project(":ids-api")) { isTransitive = false }
    providedByBundle(project(":idscp2-app-layer")) { isTransitive = false }

    implementation(project(":camel-idscp2")) { isTransitive = false }

    implementation("de.fraunhofer.iais.eis.ids.infomodel", "java", libraryVersions["infomodel"])
    implementation("de.fraunhofer.iais.eis.ids", "infomodel-serializer", libraryVersions["infomodel"])

    osgiCore("org.osgi", "osgi.cmpn", libraryVersions["osgiCompendium"])

    providedByFeature("org.apache.camel", "camel-core", libraryVersions["camel"])

    providedByFeature("com.google.protobuf", "protobuf-java", libraryVersions["protobuf"])

    providedByBundle("com.google.guava", "guava", libraryVersions["guava"]) {
        isTransitive = false  // Avoid pulling in of checker framework and other annotation stuff
    }

    testImplementation("junit", "junit", libraryVersions["junit4"])
    testImplementation("org.apache.camel", "camel-test", libraryVersions["camel"])
    testImplementation("org.mockito", "mockito-core", libraryVersions["mockito"])
}
