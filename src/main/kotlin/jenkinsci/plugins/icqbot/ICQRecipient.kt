package jenkinsci.plugins.icqbot

import hudson.Extension
import hudson.model.AbstractDescribableImpl
import hudson.model.Descriptor
import org.kohsuke.stapler.DataBoundConstructor

@Suppress("MemberVisibilityCanBePrivate")
class ICQRecipient @DataBoundConstructor
constructor(val id: String) : AbstractDescribableImpl<ICQRecipient>() {

  override fun toString(): String {
    return "[$id]"
  }

  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (javaClass != other?.javaClass) return false
    other as ICQRecipient
    if (id != other.id) return false
    return true
  }

  override fun hashCode(): Int {
    return id.hashCode()
  }

  @Extension
  class ICQRecipientDescriptor : Descriptor<ICQRecipient>() {

    override fun getDisplayName(): String {
      return "ICQ user, channel or chat"
    }
  }
}
