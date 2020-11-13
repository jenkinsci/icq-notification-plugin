package jenkinsci.plugins.icqbot

import hudson.FilePath
import hudson.model.Run
import hudson.model.TaskListener
import org.jenkinsci.plugins.tokenmacro.TokenMacro
import java.io.File

class Message(
  private val template: String,
  private val filepath: String?,
  private val run: Run<*, *>,
  private val path: FilePath,
  private val listener: TaskListener
) {

  val content: String
    get() = expand(
      when {
        !filepath.isNullOrBlank() -> {
          val file = File(expand("\${WORKSPACE}/$filepath"))
          when {
            file.exists() -> file.readText()
            else -> template
          }
        }
        else -> template
      }
    )

  override fun toString() = "Message('$template', '$filepath')"

  private fun expand(value: String) = TokenMacro.expandAll(run, path, listener, value)
}
