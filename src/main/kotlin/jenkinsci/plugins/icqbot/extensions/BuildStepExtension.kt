package jenkinsci.plugins.icqbot.extensions

import hudson.Extension
import javaposse.jobdsl.dsl.Context
import javaposse.jobdsl.dsl.helpers.step.StepContext
import javaposse.jobdsl.plugin.ContextExtensionPoint
import javaposse.jobdsl.plugin.DslExtensionMethod
import jenkinsci.plugins.icqbot.ICQRecipient
import jenkinsci.plugins.icqbot.actions.SendMessageBuildStep
import java.util.*

@Extension(optional = true)
class BuildStepExtension : ContextExtensionPoint() {

  @DslExtensionMethod(context = StepContext::class)
  fun message(runnable: Runnable): Any {
    val context = BuilderContext()
    ContextExtensionPoint.executeInContext(runnable, context)
    return SendMessageBuildStep(context.message, "", context.recipients)
  }

  private class BuilderContext : Context {
    val recipients = ArrayList<ICQRecipient>()
    var message = ""

    fun message(value: String) {
      message = value
    }

    fun recipient(id: String) {
      recipients.add(ICQRecipient(id))
    }
  }
}
