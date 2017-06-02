# java-build-conventions
A set of gradle build conventions to eliminate gradle boilerplate and enforce coding standards

# How to use
// TODO add gradle snippet when we have bintray snapshots

Currently you have to build it from source and refer the plugin from your local() repo, as done [here](https://github.com/novarto-oss/java-build-conventions/blob/master/javaconventions-example/build.gradle).

# What it's for
This is a gradle plugin which is mostly intended to share build logic / reduce boilerplate in the various projects under https://github.com/novarto-oss. At this point it probably is not flexible enough to be used as a general purpose build plugin. That being said, you could give it a shot and let us know of any problems via an issue.

# What it does
* applies the `java` plugin
* applies the `maven-publish` plugin
* bintray support - work in progress
* configures java for 1.8 source and target compat
* turns on javac strict mode such that all warnings are reported, and any warning fails the build
* adds parameter information to the java bytecode so that you can deserialize immutable classes without the `@JsonProperty` madness et al.
* Makes gradle fail in case there are different version of the same lib on your build path
* Sets up source jar build
* Applies `checkstyle` with [this](https://github.com/novarto-oss/java-build-conventions/blob/master/src/main/resources/checkstyle_config.xml) template
* Applies `jacoco`
* Tweaks `test` and `javadoc` with some sane defaults
* Applies findbugs. Turned off by default since it is painfully slow and might make you quit your job. Canonical use is to turn it on [via a commandline parameter](https://github.com/novarto-oss/java-build-conventions/blob/master/javaconventions-example/build.gradle#L20) 


