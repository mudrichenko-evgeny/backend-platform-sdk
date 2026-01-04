plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.kapt)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.java.library)
}

dependencies {
    // libs
    implementation(libs.kotlinx.serialization.json)

    implementation(libs.ktor.server.core)
    implementation(libs.ktor.server.content.negotiation)
    implementation(libs.ktor.server.status.pages)
    implementation(libs.ktor.server.netty)
    implementation(libs.ktor.serialization.json)
    implementation(libs.ktor.server.cors)
    implementation(libs.ktor.server.default.headers.jvm)
    implementation(libs.ktor.server.rate.limit)
    implementation(libs.ktor.client.cio)
    implementation(libs.ktor.client.logging)
    implementation(libs.ktor.client.content.negotiation)

    kapt(libs.dagger.compiler)
    implementation(libs.dagger)

    implementation(libs.logback)
    implementation(libs.logstash)

    implementation(libs.dotenv)

    implementation(libs.smiley4.ktor.openapi)
    implementation(libs.smiley4.ktor.swagger.ui)
    implementation(libs.smiley4.schema.kenerator.core)
    implementation(libs.smiley4.schema.kenerator.serialization)
    implementation(libs.smiley4.schema.kenerator.swagger)

    // test
    testRuntimeOnly(libs.kotlin.test.junit5)
    testImplementation(libs.junit.jupiter.api)
    testRuntimeOnly(libs.junit.jupiter.engine)
    testImplementation(libs.mockk)
    testImplementation(libs.ktor.server.test.host)
}

tasks.test {
    useJUnitPlatform()
}