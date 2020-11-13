package jenkinsci.plugins.icqbot

import com.google.gson.Gson
import hudson.ProxyConfiguration
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.newSingleThreadContext
import okhttp3.Credentials
import okhttp3.OkHttpClient
import org.jenkinsci.plugins.tokenmacro.MacroEvaluationException
import org.slf4j.LoggerFactory
import ru.mail.im.botapi.BotApiClient
import ru.mail.im.botapi.fetcher.OnEventFetchListener
import ru.mail.im.botapi.util.ListenerList
import java.io.IOException
import java.net.InetSocketAddress
import java.net.Proxy
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicReference

private const val ICQ_API_URL = "https://api.icq.net"

object ICQBot {
  @Suppress("EXPERIMENTAL_API_USAGE")
  private val scope = CoroutineScope(newSingleThreadContext("icq-notifier"))
  private val proxy = ProxyConfiguration.load()
  private val token = AtomicReference<String>()
  private val api = AtomicReference<String>()

  private val logger = LoggerFactory.getLogger("icq-bot")

  @Throws(IOException::class, InterruptedException::class)
  fun send(message: Message, recipients: List<ICQRecipient>) {
    token.get()?.let { client(it) }?.also { client ->
      try {
        val content = message.content
        logger.info("Sending \"$content\" message to $recipients")
        recipients.forEach {
          scope.launch { client.messages().sendText(it.id, content, null, null, null, null) }
        }
      } catch (e: MacroEvaluationException) {
        logger.error("Failed to expand message: \"$message\"", e)
      }
    }
  }

  fun token(value: String) = token.set(value)

  fun api(url: String) = api.set(if (url.isNotBlank()) url else ICQ_API_URL)

  private fun client(token: String) = BotApiClient(
    Gson(),
    OkHttpClient.Builder().also {
      it.followRedirects(true)
      it.readTimeout(60, TimeUnit.SECONDS)
      proxy?.apply {
        it
          .proxy(Proxy(Proxy.Type.HTTP, InetSocketAddress(name, port)))
          .authenticator { _, response ->
            response.request().newBuilder()
              .header("Proxy-Authorization", Credentials.basic(userName, password))
              .build()
          }
      }
    }.build(),
    ListenerList(OnEventFetchListener::class.java),
    api.get(),
    token,
    0,
    60
  )
}
