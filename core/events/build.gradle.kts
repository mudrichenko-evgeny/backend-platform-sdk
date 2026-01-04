plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.kapt)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.java.library)
}

dependencies {
    implementation(project(":core.common"))

    implementation(libs.kotlinx.serialization.json)
    implementation(libs.kotlinx.coroutines.core)

    implementation(libs.apache.kafka)

    kapt(libs.dagger.compiler)
    implementation(libs.dagger)

    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}