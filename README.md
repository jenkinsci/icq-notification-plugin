# ICQ and MyTeam plugin for Jenkins

[![Plugin Version](https://img.shields.io/jenkins/plugin/v/icq-notification.svg)](https://plugins.jenkins.io/icq-notification)
[![Build Status](https://ci.jenkins.io/job/plugins/job/icq-notification-plugin/job/master/badge/icon)](https://ci.jenkins.io/job/plugins/job/icq-notification-plugin/job/master/)
[![Сodebeat](https://codebeat.co/badges/36f4b8ec-53a3-4926-bd8e-fa4abe9a291e)](https://codebeat.co/projects/github-com-jenkinsci-icq-notification-plugin-master)

The plugin allows to send Jenkins notifications to ICQ and MyTeam
                   
## Install from Plugin Manager

* Open *<your_jenkins>/pluginManager/available*
* Find and install **ICQ and MyTeam Notification Plugin**

## Build and install
* Run `gradle jpi` to build the plugin. The resulting *icq-notification.hpi* file will created in *./build/libs/*
* [Manually install](https://jenkins.io/doc/book/managing/plugins/#advanced-installation) the plugin

## Usage

* Create your own bot by sending the `/newbot` command to [@megabot](https://icq.com/people/70001) for ICQ (
  or [@metabot](https://myteam.mail.ru/profile/70001) for MyTeam) and follow the instructions
* Specify your bot token in Jenkins system configuration
* Optional: Specify custom MyTeam Bot API URL (leave the field blank for ICQ)
* Add a build step and/or a post build action
* Fill the message and recipients
