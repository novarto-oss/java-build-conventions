package com.novarto.releng

import org.gradle.api.GradleException
import org.gradle.api.Plugin
import org.gradle.api.Project

class OssPublishPlugin  implements Plugin<Project> {

    @Override
    void apply(Project target) {

        if(target.version == Project.DEFAULT_VERSION)
        {
            throw new GradleException("unspecified project version. Please define version before applying plugin")
        }

        target.pluginManager.apply('com.jfrog.bintray')
        target.pluginManager.apply('com.jfrog.artifactory')

        def publishUser = System.getenv('PUBLISH_USER')
        def publishKey = System.getenv('PUBLISH_KEY')
        def publishEnvIsSet = publishUser!=null && publishKey!=null

        def isSnapshot = target.version.toString().endsWith("SNAPSHOT")

        if(!publishEnvIsSet)
        {
            println("PUBLISH_USER or PUBLISH_KEY ENV is not set. Will not configure snapshot or release build")
            return
        }

        if (isSnapshot) {
            println("Snapshot version ${target.version} is set for ${target.name}. Configuring publish to oss-snapshot-local")
            //artifactory support
            target.artifactory {
                publish {
                    contextUrl = 'https://oss.jfrog.org/artifactory'
                    repository {
                        repoKey = 'oss-snapshot-local'
                        username = publishUser
                        password = publishKey
                    }

                    defaults {
                        publications('mavenJava')
                    }
                }
            }
        }


        else {
            //bintray support

            println("Release version ${target.version} is set for ${target.name}. Configuring publish to bintray")
            target.bintray {

                user = publishUser
                key = publishKey

                pkg {
                    repo = 'novarto-oss-snapshots'
                    userOrg = 'novarto-oss'

                    licenses = ['Apache-2.0']

                    version {
                        name = target.version
                    }

                }

                publications = ['mavenJava']

            }
        }

    }
}
