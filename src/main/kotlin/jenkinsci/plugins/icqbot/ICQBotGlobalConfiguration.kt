package jenkinsci.plugins.icqbot

import hudson.Extension
import jenkins.model.GlobalConfiguration
import kotlinx.coroutines.ObsoleteCoroutinesApi
import net.sf.json.JSONObject
import org.kohsuke.stapler.DataBoundConstructor
import org.kohsuke.stapler.StaplerRequest

@ObsoleteCoroutinesApi
@Suppress("MemberVisibilityCanBePrivate")
@Extension
class ICQBotGlobalConfiguration @DataBoundConstructor constructor() : GlobalConfiguration() {
  var token: String = ""
    private set

  var api: String = ""
    private set

  init {
    load()
    ICQBot.token(token)
    ICQBot.api(api)
  }

  @Throws(hudson.model.Descriptor.FormException::class)
  override fun configure(req: StaplerRequest, formData: JSONObject): Boolean {
    token = formData.getString("token")
    ICQBot.token(token)

    api = formData.getString("api")
    ICQBot.api(api)

    save()
    return super.configure(req, formData)
  }

  override fun getDisplayName() = "ICQ and MyTeam Bot"
}
