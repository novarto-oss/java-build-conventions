# java-build-conventions
A set of gradle build conventions to eliminate build boilerplate and enforce coding standards

# How to use
// TODO change gradle snippet once we have a release version
```groovy
buildscript {
    repositories {
        mavenCentral()
        jcenter()
        maven {
            url 'https://oss.jfrog.org/artifactory/libs-snapshot/'
        }
    }
    dependencies {

        classpath group: 'com.novarto', name: 'java-conventions',
                version: '0.9.1-SNAPSHOT'
    }
}
```

Then you need to supply project version **prior** to applying plugins:
```groovy
group 'foo'
version '0.42-SNAPSHOT'
```

Then you apply plugins:
```groovy
//general build conventions
apply plugin: 'com.novarto.javaconventions'
//publish to bintray or jfrog oss snapshots:
apply plugin: 'com.novarto.osspublish'
```

# What it's for
These are gradle plugins which are mostly intended to share build logic / reduce boilerplate in the various projects under https://github.com/novarto-oss. At this point they probably are not flexible enough to be used as general purpose build plugins. That being said, you could give them a shot and let us know of any problems via an issue.

# What it does

## `'com.novarto.javaconventions'`
* applies the `java` plugin
* applies and configures the `maven-publish` plugin. Adds sources and javadoc jars to the default `mavenJava` publication
* configures java for 1.8 source and target compat
* turns on javac strict mode such that all warnings are reported, and any warning fails the build
* adds parameter information to the java bytecode so that you can deserialize immutable classes without the `@JsonProperty` madness et al.
* Makes gradle fail in case there are different version of the same lib on your build path
* Sets up source jar build
* Applies `checkstyle` with [this](https://github.com/novarto-oss/java-build-conventions/blob/master/src/main/resources/checkstyle_config.xml) template
* Applies `jacoco`
* Tweaks `test` and `javadoc` with some sane defaults
* Applies findbugs. Turned off by default since it is painfully slow and might make you quit your job. Canonical use is to turn it on [via a commandline parameter](https://github.com/novarto-oss/java-build-conventions/blob/master/javaconventions-example/build.gradle#L20) 

## `'com.novarto.osspublish'`
**Use this only if your project is an opensource project, this publishes artifacts to public oss repositories. Licence
is by default hardcoded to APL 2**

* Configures publish to bintray, or jfrog oss snapshots, depending on whether your project version is release or a `-SNAPSHOT` one
* Configures credentials with whatever you export in the `PUBLISH_USER` and `PUBLISH_KEY` env vars

