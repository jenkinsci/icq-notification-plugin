# ICQ plugin for Jenkins                                                                                                                                                         
[![Plugin Version](https://img.shields.io/jenkins/plugin/v/icq-notification.svg)](https://plugins.jenkins.io/icq-notification)
[![Build Status](https://ci.jenkins.io/job/plugins/job/icq-notification-plugin/job/master/badge/icon)](https://ci.jenkins.io/job/plugins/job/icq-notification-plugin/job/master/)

The plugin allows to send Jenkins notifications to ICQ
                   
## Install from Plugin Manager
* Open *<your_jenkins>/pluginManager/available*
* Find and install **ICQ Notification**

## Build and install
* Run `gradle jpi` to build the plugin. The resulting *icq-notification.hpi* file will created in *./build/libs/*
* [Manually install](https://jenkins.io/doc/book/managing/plugins/#advanced-installation) the plugin

## Usage
* Create your own bot by sending the `/newbot` command to [MegaBot](https://icq.com/people/70001) and follow the instructions
* Specify your bot token in Jenkins system configuration
* Add a build step and/or a post build action
* Fill the message and recipients
