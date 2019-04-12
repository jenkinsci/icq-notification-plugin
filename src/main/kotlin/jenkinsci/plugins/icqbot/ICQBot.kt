package jenkinsci.plugins.icqbot

import hudson.FilePath
import hudson.ProxyConfiguration
import hudson.model.Run
import hudson.model.TaskListener
import io.ktor.client.HttpClient
import io.ktor.client.engine.apache.Apache
import io.ktor.client.features.json.JacksonSerializer
import io.ktor.client.features.json.JsonFeature
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.newSingleThreadContext
import org.apache.http.HttpHost
import org.apache.http.auth.AuthScope
import org.apache.http.auth.UsernamePasswordCredentials
import org.apache.http.impl.client.BasicCredentialsProvider
import org.jenkinsci.plugins.tokenmacro.MacroEvaluationException
import org.jenkinsci.plugins.tokenmacro.TokenMacro
import su.nlq.icq.bot.Bot
import su.nlq.icq.bot.PenPal
import java.io.IOException
import java.util.concurrent.atomic.AtomicReference
import java.util.logging.Level
import java.util.logging.Logger

@ObsoleteCoroutinesApi
object ICQBot {
  private val log = Logger.getLogger("icq-notifier")

  private val asyncScope = CoroutineScope(newSingleThreadContext("icq-notifier"))
  private val httpClient = httpClient(ProxyConfiguration.load())
  private val token = AtomicReference<String>()

  @Throws(IOException::class, InterruptedException::class)
  fun send(template: String,
           recipients: List<ICQRecipient>,
           run: Run<*, *>,
           path: FilePath,
           listener: TaskListener) {
    token.get()?.let { Bot(it, httpClient) }?.also { bot ->
      try {
        val message = TokenMacro.expandAll(run, path, listener, template)
        log.info("Sending \"$message\" message to $recipients")
        recipients.map { PenPal(it.id) }.forEach {
          asyncScope.launch {
            bot.conversation(it).message(message).onFailure {
              log.warning("Failed to send message to $it")
            }
          }
        }
      } catch (e: MacroEvaluationException) {
        log.log(Level.SEVERE, "Failed to expand message: \"$template\"", e)
      }
    }
  }

  fun token(value: String) {
    token.set(value)
  }

  private fun httpClient(proxy: ProxyConfiguration?) = HttpClient(Apache) {
    install(JsonFeature) {
      serializer = JacksonSerializer()
    }
    engine {
      proxy?.also {
        followRedirects = true
        customizeClient {
          setProxy(HttpHost(it.name, it.port))
          val credentialsProvider = BasicCredentialsProvider()
          credentialsProvider.setCredentials(
              AuthScope(it.name, it.port),
              UsernamePasswordCredentials(it.userName, it.password)
          )
          setDefaultCredentialsProvider(credentialsProvider)
        }
      }
    }
  }
}
