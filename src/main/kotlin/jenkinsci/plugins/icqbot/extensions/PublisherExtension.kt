package jenkinsci.plugins.icqbot.extensions

import hudson.Extension
import hudson.model.Result
import javaposse.jobdsl.dsl.Context
import javaposse.jobdsl.plugin.ContextExtensionPoint
import javaposse.jobdsl.plugin.DslExtensionMethod
import jenkinsci.plugins.icqbot.ICQRecipient
import jenkinsci.plugins.icqbot.actions.SendMessagePostBuildAction
import java.util.*

@Extension(optional = true)
class PublisherExtension : ContextExtensionPoint() {

  @DslExtensionMethod(context = javaposse.jobdsl.dsl.helpers.publisher.PublisherContext::class)
  fun message(runnable: Runnable): SendMessagePostBuildAction {
    val context = PublisherContext()
    ContextExtensionPoint.executeInContext(runnable, context)
    return SendMessagePostBuildAction(context.message, context.recipients, context.active)
  }

  private class PublisherContext : Context {
    val recipients = ArrayList<ICQRecipient>()
    val active = HashSet<Result>()
    var message = ""

    fun message(value: String) {
      message = value
    }

    fun recipient(id: String) {
      recipients.add(ICQRecipient(id))
    }

    fun succeeded() {
      active.add(Result.SUCCESS)
    }

    fun failed() {
      active.add(Result.FAILURE)
    }

    fun aborted() {
      active.add(Result.ABORTED)
    }

    fun unstable() {
      active.add(Result.UNSTABLE)
    }
  }
}
