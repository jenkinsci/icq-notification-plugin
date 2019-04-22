package jenkinsci.plugins.icqbot

import hudson.FilePath
import hudson.model.Run
import hudson.model.TaskListener
import org.jenkinsci.plugins.tokenmacro.TokenMacro
import java.io.File

class Message(
    private val template: String,
    private val filepath: String,
    private val run: Run<*, *>,
    private val path: FilePath,
    private val listener: TaskListener
) {
  val content: String
    get() = expand(if (filepath.isBlank()) {
      template
    } else {
      File(expand("\${WORKSPACE}/$filepath")).readText()
    })

  override fun toString() = "Message('$template', '$filepath')"

  private fun expand(value: String) = TokenMacro.expandAll(run, path, listener, value)
}
