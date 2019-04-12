package jenkinsci.plugins.icqbot.actions

import hudson.Extension
import hudson.FilePath
import hudson.Launcher
import hudson.model.AbstractProject
import hudson.model.Result
import hudson.model.Run
import hudson.model.TaskListener
import hudson.tasks.BuildStepDescriptor
import hudson.tasks.BuildStepMonitor
import hudson.tasks.Notifier
import hudson.tasks.Publisher
import jenkins.tasks.SimpleBuildStep
import jenkinsci.plugins.icqbot.ICQBot
import jenkinsci.plugins.icqbot.ICQRecipient
import kotlinx.coroutines.ObsoleteCoroutinesApi
import org.jenkinsci.Symbol
import org.kohsuke.stapler.DataBoundConstructor
import java.io.IOException
import java.util.*

@ObsoleteCoroutinesApi
@Suppress("MemberVisibilityCanBePrivate")
class SendMessagePostBuildAction : Notifier, SimpleBuildStep {
  val message: String
  val recipients: List<ICQRecipient>
  private val active = HashSet<Result>()

  val isSucceeded: Boolean get() = active(Result.SUCCESS)
  val isUnstable: Boolean get() = active(Result.UNSTABLE)
  val isFailed: Boolean get() = active(Result.FAILURE)
  val isAborted: Boolean get() = active(Result.ABORTED)

  private fun active(result: Result) = active.contains(result)

  @DataBoundConstructor
  constructor(message: String,
              recipients: List<ICQRecipient>,
              succeeded: Boolean,
              unstable: Boolean,
              failed: Boolean,
              aborted: Boolean) {
    this.message = message
    this.recipients = recipients
    activate(succeeded, Result.SUCCESS)
    activate(unstable, Result.UNSTABLE)
    activate(failed, Result.FAILURE)
    activate(aborted, Result.ABORTED)
  }

  constructor(message: String,
              recipients: List<ICQRecipient>,
              active: Set<Result>) {
    this.message = message
    this.recipients = recipients
    this.active.addAll(active)
  }

  override fun getRequiredMonitorService(): BuildStepMonitor {
    return BuildStepMonitor.NONE
  }

  @Throws(InterruptedException::class, IOException::class)
  override fun perform(run: Run<*, *>,
                       path: FilePath,
                       launcher: Launcher,
                       listener: TaskListener) {
    if (run.getResult()?.let { active(it) } == true) {
      ICQBot.send(message, recipients, run, path, listener)
    }
  }

  private fun activate(enabled: Boolean, result: Result) {
    if (enabled) {
      active.add(result)
    }
  }

  @Extension
  @Symbol("icqMessage")
  class SendMessagePostBuildActionDescriptor : BuildStepDescriptor<Publisher>() {

    override fun isApplicable(jobType: Class<out AbstractProject<*, *>>): Boolean {
      return true
    }

    override fun getDisplayName(): String {
      return "Send message to ICQ"
    }
  }
}
