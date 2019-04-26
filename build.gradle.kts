import org.jenkinsci.gradle.plugins.jpi.JpiDeveloper
import org.jenkinsci.gradle.plugins.jpi.JpiLicense
import org.jetbrains.kotlin.gradle.internal.KaptGenerateStubsTask
import org.jetbrains.kotlin.gradle.internal.KaptTask
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
  kotlin("jvm") version "1.3.11"
  kotlin("kapt") version ("1.3.11")
  id("org.jenkins-ci.jpi") version "0.31.0"
}

group = "org.jenkins-ci.plugins"
version = "1.2.0"
description = "The plugin allows to send Jenkins notifications to ICQ"

jenkinsPlugin {
  coreVersion = "2.68"

  shortName = "icq-notification"
  displayName = "ICQ Notification Plugin"

  url = "https://wiki.jenkins.io/display/JENKINS/ICQ+Notification+Plugin"
  gitHubUrl = "https://github.com/jenkinsci/icq-notification-plugin"

  compatibleSinceVersion = "${project.version}"

  developers = this.Developers().apply {
    developer(delegateClosureOf<JpiDeveloper> {
      setProperty("id", "nolequen")
      setProperty("name", "Anton Potsyus")
      setProperty("email", "nolequen@gmail.com")
      setProperty("url", "https://nlq.su")
      setProperty("timezone", "UTC+3")
    })
  }

  licenses = this.Licenses().apply {
    license(delegateClosureOf<JpiLicense> {
      setProperty("url", "https://jenkins.io/license/")
    })
  }
}

repositories {
  maven("http://repo.jenkins-ci.org/public/")
  maven("https://kotlin.bintray.com/kotlinx")
}

dependencies {
  compile(kotlin("stdlib-jdk8"))
  compile("org.jenkins-ci.main", "jenkins-core", "2.68")
  compile("su.nlq", "icq-bot-core", "1.1.1")
  compile("org.jetbrains.kotlinx", "kotlinx-coroutines-core", "1.1.1")
  compile("io.ktor", "ktor-client-apache", "1.1.1")

  jenkinsPlugins("org.jenkins-ci.plugins", "token-macro", "2.7")

  optionalJenkinsPlugins("org.jenkins-ci.plugins", "job-dsl", "1.72")

  compileOnly("javax.servlet", "servlet-api", "2.5")

  kapt("net.java.sezpoz", "sezpoz", "1.13")

  testCompile("org.jenkins-ci.main", "jenkins-test-harness", "2.49")
}

kapt {
  correctErrorTypes = true
}

tasks.withType(KaptTask::class.java).all {
  outputs.upToDateWhen { false }
}

tasks.withType(KaptGenerateStubsTask::class.java).all {
  outputs.upToDateWhen { false }
}

tasks.withType<KotlinCompile> {
  kotlinOptions.jvmTarget = "1.8"
}
