plugins {
    java
    application
    id("war")
    id("org.springframework.boot") version "3.3.5"
    id("io.spring.dependency-management") version "1.1.6"
    id("com.autonomousapps.dependency-analysis") version "2.4.2"
}

group = "ru.mirea"
version = "0.0.1-SNAPSHOT"

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

repositories {
    mavenCentral()
}

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

dependencies {
    // Spring Boot
    implementation(platform("org.springframework.boot:spring-boot-dependencies:2.2.4.RELEASE"))
    implementation("com.fasterxml.jackson.core:jackson-core")
    implementation("com.fasterxml.jackson.core:jackson-databind")
    implementation("com.squareup.retrofit2:retrofit:2.11.0")
    implementation("com.squareup.retrofit2:converter-jackson:2.11.0")
    implementation("org.postgresql:postgresql")
    implementation("com.google.guava:guava:20.0")
    implementation("com.squareup.okhttp3:okhttp:4.12.0")
    implementation("org.springframework:spring-beans:6.1.14")
    implementation("org.springframework.boot:spring-boot-autoconfigure:3.3.5")
    implementation("org.springframework:spring-context:6.1.14")
    implementation("org.springframework.boot:spring-boot:3.3.5")
    implementation("jakarta.servlet:jakarta.servlet-api:5.0.0")
    implementation("org.hibernate.orm:hibernate-core:6.3.0.Final")
    implementation("com.vladmihalcea:hibernate-types-60:2.21.1")
    implementation("org.slf4j:slf4j-api:2.0.9")
    implementation("org.slf4j:slf4j-simple:2.0.9")
    implementation("com.google.code.gson:gson:2.7")

    compileOnly("org.jetbrains:annotations:24.1.0")
    compileOnly ("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")

    testCompileOnly("org.projectlombok:lombok")
    testAnnotationProcessor("org.projectlombok:lombok")

    testImplementation("org.junit.jupiter:junit-jupiter-api:5.10.5")
    testImplementation("junit:junit")
}

tasks {
    test {
        useJUnitPlatform()
    }
}

application {
    mainClass.set("ru.mirea.pkmn.PkmnApplication")
}

tasks.named<War>("war") {
    archiveFileName.set("pkmn.war")
}