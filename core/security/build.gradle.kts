plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.kapt)
    alias(libs.plugins.java.library)
}

dependencies {
    implementation(project(":core.common"))
    implementation(project(":core.database"))

    kapt(libs.dagger.compiler)
    implementation(libs.dagger)

    implementation(libs.password4j)
}

tasks.test {
    useJUnitPlatform()
}