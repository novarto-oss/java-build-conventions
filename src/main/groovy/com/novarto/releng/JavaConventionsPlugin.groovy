package com.novarto.releng

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.quality.FindBugs
import org.gradle.api.publish.maven.MavenPublication
import org.gradle.api.tasks.bundling.Jar
import org.gradle.api.tasks.testing.Test

class JavaConventionsPlugin implements Plugin<Project> {

    @Override
    void apply(Project target) {

        target.extensions.create('javaconventions', Config)

        target.pluginManager.apply("java")

        target.pluginManager.apply("maven-publish")

        //set up jar publication
        target.publishing {
            publications {
                mavenJava(MavenPublication) {
                    from target.components.java
                }
            }
        }


        // common repos boilerplate
        target.repositories {
            mavenCentral()
            jcenter()
        }


        //bintray support
        //TODO maybe make optional via config
        target.pluginManager.apply('com.jfrog.bintray')

        // java 8, strict mode
        [target.compileJava, target.compileTestJava].each() {
            it.sourceCompatibility = 1.8
            it.targetCompatibility = 1.8
            it.options.compilerArgs += ["-Xlint:all", "-parameters", "-Werror"]
            it.options.encoding = "UTF-8"
        }

        // dependency resolution strict mode
        target.configurations.all {
            resolutionStrategy {
                failOnVersionConflict()
            }
        }

        //generate sources jar
        target.task('sourcesJar', type: Jar) {
            classifier = 'sources'
            from target.sourceSets.main.allSource
        }
        target.artifacts {
            archives target.sourcesJar
        }

        //predefined checkstyle config coming from plugin
        def checkstyleConfig = target.resources.text.fromString(
                getClass().getResourceAsStream("/checkstyle_config.xml").withCloseable {x -> x.text})

        //checkstyle
        target.pluginManager.apply('checkstyle')

        target.checkstyle {
            config = checkstyleConfig
            toolVersion = "7.0"
            ignoreFailures = false
        }

        //code coverage
        target.pluginManager.apply( 'jacoco')
        target.check.dependsOn target.jacocoTestReport

        //javadoc - make sure jdk links work
        target.javadoc.options {
            encoding = 'UTF-8'
            links 'https://docs.oracle.com/javase/8/docs/api/'
        }

        //show full traces during tests
        target.tasks.withType(Test) {
            testLogging {
                exceptionFormat = 'full'
                showExceptions true
                showCauses true
                showStackTraces true
                showStandardStreams true
            }
        }



        //checks only enabled during continuous integration build, but not during local build
        target.afterEvaluate {
            if(target.javaconventions.ci)
            {

                //findbugs
                target.pluginManager.apply('findbugs')
                target.findbugs {
                    toolVersion = "3.0.1"
                    ignoreFailures = false
                }

                target.tasks.withType(FindBugs) {
                    reports {
                        html.enabled = true
                        xml.enabled = false
                    }
                }



            }
        }




    }


}

class Config {
    def boolean ci = false
}
