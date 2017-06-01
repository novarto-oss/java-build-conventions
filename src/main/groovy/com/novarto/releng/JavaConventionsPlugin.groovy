package com.novarto.releng

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.bundling.Jar

class JavaConventionsPlugin implements Plugin<Project> {

    @Override
    void apply(Project target) {

        target.extensions.create('javaconventions', Config)

        target.pluginManager.apply("java")
        target.pluginManager.apply("maven")


        // common repos boilerplate
        target.repositories {
            mavenCentral()
            jcenter()
        }

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

        def checkstyleConfig = target.resources.text.fromString(
                getClass().getResourceAsStream("/checkstyle_config.xml").withCloseable {x -> x.text})

        //checkstyle
        target.pluginManager.apply('checkstyle')

        target.checkstyle {
            config = checkstyleConfig
            toolVersion = "7.0"
            ignoreFailures = false
            if (target.hasProperty("checkstyle.ignoreFailures")) {
                ignoreFailures = rootProject.properties["checkstyle.ignoreFailures"].toBoolean()
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
            }
        }




    }


}

class Config {
        def boolean ci = false
}
