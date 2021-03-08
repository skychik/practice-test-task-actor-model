plugins {
    application
}

repositories {
    mavenCentral()
    mavenLocal()
    jcenter()
}

dependencies {
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.4.2")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.4.2")
}

tasks {
    "test"(Test::class) {
        useJUnitPlatform()
    }
}


application {
    mainClass.set("ru.ifmo.mse.App")
}