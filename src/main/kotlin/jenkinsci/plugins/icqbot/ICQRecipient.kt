package jenkinsci.plugins.icqbot

import hudson.Extension
import hudson.model.AbstractDescribableImpl
import hudson.model.Descriptor
import org.kohsuke.stapler.DataBoundConstructor

class ICQRecipient @DataBoundConstructor constructor(val id: String) : AbstractDescribableImpl<ICQRecipient>() {

  override fun toString() = "[$id]"

  override fun equals(other: Any?) =
    when {
      this === other -> true
      javaClass != other?.javaClass -> false
      id != (other as ICQRecipient).id -> false
      else -> true
    }

  override fun hashCode() = id.hashCode()

  @Extension
  class ICQRecipientDescriptor : Descriptor<ICQRecipient>() {

    override fun getDisplayName() = "ICQ or MyTeam user, channel or chat"
  }
}
