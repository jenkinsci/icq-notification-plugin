package jenkinsci.plugins.icqbot.actions

import hudson.Extension
import hudson.FilePath
import hudson.Launcher
import hudson.model.AbstractProject
import hudson.model.Run
import hudson.model.TaskListener
import hudson.tasks.BuildStepDescriptor
import hudson.tasks.Builder
import jenkins.tasks.SimpleBuildStep
import jenkinsci.plugins.icqbot.ICQBot
import jenkinsci.plugins.icqbot.ICQRecipient
import jenkinsci.plugins.icqbot.Message
import org.kohsuke.stapler.DataBoundConstructor
import java.io.IOException

@Suppress("MemberVisibilityCanBePrivate")
class SendMessageBuildStep @DataBoundConstructor
constructor(
    val message: String,
    val filepath: String,
    val recipients: List<ICQRecipient>
) : Builder(), SimpleBuildStep {

  @Throws(InterruptedException::class, IOException::class)
  override fun perform(run: Run<*, *>,
                       path: FilePath,
                       launcher: Launcher,
                       listener: TaskListener) {
    ICQBot.send(Message(message, filepath, run, path, listener), recipients, listener.logger)
  }

  @Extension
  class SendMessageBuildStepDescriptor : BuildStepDescriptor<Builder>() {

    override fun isApplicable(jobType: Class<out AbstractProject<*, *>>): Boolean {
      return true
    }

    override fun getDisplayName(): String {
      return "Send message to ICQ"
    }
  }
}
