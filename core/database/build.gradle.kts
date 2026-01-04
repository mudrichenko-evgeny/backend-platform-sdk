plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.kapt)
    alias(libs.plugins.java.library)
}

dependencies {
    implementation(project(":core.common"))
    implementation(project(":core.observability"))

    // libs
    implementation(libs.exposed.core)
    implementation(libs.exposed.dao)
    implementation(libs.exposed.jdbc)
    implementation(libs.exposed.java.time)

    kapt(libs.dagger.compiler)
    implementation(libs.dagger)

    implementation(libs.logback)
    implementation(libs.postgresql)
    implementation(libs.hikari)
    implementation(libs.flyway.core)
    implementation(libs.flyway.postgresql)
    implementation(libs.lettuce.core)

    implementation(libs.micrometer.registry.prometheus)

    // test
    testRuntimeOnly(libs.kotlin.test.junit5)
    testImplementation(libs.junit.jupiter.api)
    testRuntimeOnly(libs.junit.jupiter.engine)
    testImplementation(libs.mockk)
    testImplementation(libs.h2database)
}

tasks.test {
    useJUnitPlatform()
}