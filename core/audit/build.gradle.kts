plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.kapt)
    alias(libs.plugins.java.library)
}

dependencies {
    implementation(project(":core.common"))
    implementation(project(":core.database"))

    implementation(libs.ktor.server.core)

    kapt(libs.dagger.compiler)
    implementation(libs.dagger)

    implementation(libs.exposed.core)
    implementation(libs.exposed.json)
}

tasks.test {
    useJUnitPlatform()
}