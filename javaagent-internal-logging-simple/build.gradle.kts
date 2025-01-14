import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
  id("otel.java-conventions")
  id("otel.publish-conventions")

  id("com.github.johnrengelman.shadow")
}

group = "io.opentelemetry.javaagent"

val agentSlf4jVersion = "2.0.0"

dependencies {
  compileOnly(project(":javaagent-bootstrap"))
  compileOnly(project(":javaagent-tooling"))

  implementation("org.slf4j:slf4j-api:$agentSlf4jVersion")
  implementation("org.slf4j:slf4j-simple:$agentSlf4jVersion")

  annotationProcessor("com.google.auto.service:auto-service")
  compileOnly("com.google.auto.service:auto-service-annotations")
  testCompileOnly("com.google.auto.service:auto-service-annotations")
}

tasks {
  val shadowJar by existing(ShadowJar::class) {
    // required for META-INF/services files relocation
    mergeServiceFiles()

    // Prevents configuration naming conflict with other SLF4J instances
    relocate("org.slf4j", "io.opentelemetry.javaagent.slf4j")
  }

  assemble {
    dependsOn(shadowJar)
  }
}
