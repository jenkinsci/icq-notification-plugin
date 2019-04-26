package jenkinsci.plugins.icqbot

import hudson.ProxyConfiguration
import io.ktor.client.HttpClient
import io.ktor.client.engine.apache.Apache
import io.ktor.client.features.json.JacksonSerializer
import io.ktor.client.features.json.JsonFeature
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.newSingleThreadContext
import org.apache.http.HttpHost
import org.apache.http.auth.AuthScope
import org.apache.http.auth.UsernamePasswordCredentials
import org.apache.http.impl.client.BasicCredentialsProvider
import org.jenkinsci.plugins.tokenmacro.MacroEvaluationException
import su.nlq.icq.bot.Bot
import su.nlq.icq.bot.PenPal
import java.io.IOException
import java.io.PrintStream
import java.util.concurrent.atomic.AtomicReference

object ICQBot {
  @Suppress("EXPERIMENTAL_API_USAGE")
  private val asyncScope = CoroutineScope(newSingleThreadContext("icq-notifier"))
  private val httpClient = httpClient(ProxyConfiguration.load())
  private val token = AtomicReference<String>()

  @Throws(IOException::class, InterruptedException::class)
  fun send(message: Message, recipients: List<ICQRecipient>, log: PrintStream) {
    token.get()?.let { Bot(it, httpClient) }?.also { bot ->
      try {
        val content = message.content
        log.println("Sending \"$content\" message to $recipients")
        recipients.map { bot.conversation(PenPal(it.id)) }.forEach {
          asyncScope.launch {
            it.message(content).onFailure {
              log.println("Failed to send message: $it")
            }
          }
        }
      } catch (e: MacroEvaluationException) {
        log.println("Failed to expand message: \"$message\": ${e.message}")
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
