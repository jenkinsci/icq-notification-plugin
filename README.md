# ICQ plugin for Jenkins
The plugin allows to send Jenkins notifications to ICQ
                   
## Install from Plugin Manager
* Open *<your_jenkins>/pluginManager/available*
* Find and install **ICQ Notifier**

## Build and install
* Run `gradle jpi` to build the plugin. The resulting *icq-notifications.hpi* file will created in *./build/libs/*
* [Manually install](https://jenkins.io/doc/book/managing/plugins/#advanced-installation) the plugin

## Usage
* Create your own bot by sending the `/newbot` command to [MegaBot](https://icq.com/people/70001) and follow the instructions
* Specify your bot token in Jenkins system configuration
* Add a build step and/or a post build action
* Fill the message and recipients
