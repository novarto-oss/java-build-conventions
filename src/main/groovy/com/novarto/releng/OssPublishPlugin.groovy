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

        def publishUser = System.getenv('PUBLISH_USER')
        def publishKey = System.getenv('PUBLISH_KEY')

        if (publishUser==null || publishKey==null)
        {
            println("will not add artifactory / bintray publish task since PUBLISH_USER or PUBLISH_KEY env var is not set")
            return
        }

        def isSnapshot = target.version.toString().endsWith("SNAPSHOT")

        target.pluginManager.apply('com.jfrog.bintray')
        target.pluginManager.apply('com.jfrog.artifactory')


        if (isSnapshot) {
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
