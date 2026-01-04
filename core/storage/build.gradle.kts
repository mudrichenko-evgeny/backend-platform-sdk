plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.kapt)
    alias(libs.plugins.java.library)
}

dependencies {
    implementation(project(":core.common"))

    // libs
    implementation(libs.kotlinx.coroutines.core)

    kapt(libs.dagger.compiler)
    implementation(libs.dagger)

    implementation(libs.aws.s3)
    implementation(libs.aws.apache.client)

    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}