package jenkinsci.plugins.icqbot

import hudson.FilePath
import hudson.model.FreeStyleBuild
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.jvnet.hudson.test.JenkinsRule
import java.io.File

class MessageContentTest {
  @Rule
  @JvmField
  var jenkins = JenkinsRule()

  @Test
  internal fun emptyFilePath() {
    assertMessageContent { "" }
  }

  @Test
  internal fun noFileFound() {
    assertMessageContent { "./no/such/file" }
  }

  @Test
  internal fun fileMessageTemplate() {
    assertMessageContent {
      workspace?.let {
        val tempFile = File("${it.createTextTempFile("message-template", "", messageTemplate)}")
        return@let File(it.remote).toURI().relativize(tempFile.toURI()).path
      } ?: ""
    }
  }

  private fun assertMessageContent(filepath: FreeStyleBuild.() -> String) {
    val build = jenkins.buildAndAssertSuccess(jenkins.createFreeStyleProject())
    val listener = jenkins.createTaskListener()

    build.displayName = "Build"
    build.number = 42

    val message = Message(messageTemplate, filepath(build), build, FilePath(File(".")), listener).content
    Assert.assertEquals(message, "${build.displayName} number is ${build.number}")
  }

  companion object {
    private const val messageTemplate = "\${BUILD_DISPLAY_NAME} number is \${BUILD_NUMBER}"
  }
}