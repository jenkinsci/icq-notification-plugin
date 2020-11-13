import com.gargoylesoftware.htmlunit.WebAssert
import hudson.model.FreeStyleProject
import jenkinsci.plugins.icqbot.ICQRecipient
import jenkinsci.plugins.icqbot.actions.SendMessagePostBuildAction
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.jvnet.hudson.test.JenkinsRule

class ConfigurationTest {
  @Rule
  @JvmField
  var jenkins = JenkinsRule()

  @Test
  fun shouldExistSingleConfigurationElement() {
    //when
    val page = jenkins.createWebClient().goTo("configure")
    val elements = page.getElementsByName("jenkinsci-plugins-icqbot-ICQBotGlobalConfiguration")

    //then
    assertEquals("Single plugin configuration element should exist", elements.size, 1)
  }

  @Test
  @Throws(Exception::class)
  fun shouldHaveConfigurationFields() {
    //when
    val page = jenkins.createWebClient().goTo("configure")

    //then
    WebAssert.assertInputPresent(page, "_.token")
    WebAssert.assertInputPresent(page, "_.api")
  }

  @Test
  fun shouldStoreConfiguration() {
    //given
    val project = jenkins.instance.createProject(FreeStyleProject::class.java, "test_project")
    val expected = SendMessagePostBuildAction("test message", listOf(ICQRecipient("42")), emptySet())
    project.publishersList.add(expected)

    //when
    jenkins.submit(jenkins.createWebClient().getPage(project, "configure").getFormByName("config"))

    //then
    val actual = project.publishersList.get(SendMessagePostBuildAction::class.java)
    jenkins.assertEqualBeans(expected, actual, arrayOf("message", "recipients").joinToString(","))
  }
}
