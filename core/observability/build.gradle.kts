plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.kapt)
    alias(libs.plugins.java.library)
}

dependencies {
    implementation(project(":core.common"))

    // libs
    kapt(libs.dagger.compiler)
    implementation(libs.dagger)

    implementation(libs.hikari)

    implementation(platform(libs.opentelemetry.bom))
    implementation(libs.opentelemetry.api)
    implementation(libs.opentelemetry.sdk)
    implementation(libs.opentelemetry.exporter.otlp)
    implementation(libs.opentelemetry.sdk.metrics)
    implementation(libs.opentelemetry.sdk.extension.autoconfigure)
    implementation(libs.opentelemetry.extension.kotlin)
    implementation(libs.opentelemetry.semconv)

    implementation(libs.ktor.server.metrics.micrometer)
    implementation(libs.micrometer.registry.prometheus)

    implementation(libs.smiley4.ktor.openapi)
    // test
}

tasks.test {
    useJUnitPlatform()
}