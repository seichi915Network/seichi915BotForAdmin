package net.seichi915.seichi915botforadmin

import com.jagrosh.jdautilities.command.{Command, CommandClientBuilder}
import net.dv8tion.jda.api.{AccountType, JDA, JDABuilder, OnlineStatus}
import net.dv8tion.jda.api.hooks.ListenerAdapter
import net.seichi915.seichi915botforadmin.command._
import net.seichi915.seichi915botforadmin.configuration.Configuration

import java.util.logging.{FileHandler, Level, Logger, SimpleFormatter}
import javax.security.auth.login.LoginException

object Seichi915BotForAdmin {
  var jda: JDA = _

  private val logger = Logger.getLogger("Seichi915BotForAdmin")

  def getLogger: Logger = logger

  def main(args: Array[String]): Unit = {
    System.setProperty(
      "java.util.logging.SimpleFormatter.format",
      "[%1$tY-%1$tm-%1$td %1$tH:%1$tM:%1$tS.%1$tL] %4$s %2$s %5$s%6$s%n")
    val fileHandler = new FileHandler("bot.log", true)
    fileHandler.setFormatter(new SimpleFormatter)
    getLogger.addHandler(fileHandler)
    getLogger.setLevel(Level.ALL)
    if (!Configuration.saveDefaultConfig) {
      getLogger.severe("デフォルトのconfig.ymlファイルをコピーできませんでした。Botを停止します。")
      sys.exit(1)
      return
    }
    val commands = Seq[Command](
      new PingCommand
    )
    val listeners = Seq[ListenerAdapter](
      )
    val commandClient = new CommandClientBuilder()
      .setOwnerId("566817854616240128")
      .setPrefix("$")
      .setStatus(OnlineStatus.ONLINE)
      .addCommands(commands: _*)
      .setActivity(null)
      .useHelpBuilder(false)
      .build()
    try {
      jda = new JDABuilder(AccountType.BOT)
        .setToken(Configuration.getToken)
        .addEventListeners(commandClient)
        .addEventListeners(listeners: _*)
        .build()
      getLogger.info("ログインに成功しました。")
    } catch {
      case e: LoginException =>
        getLogger.log(Level.SEVERE, "ログインに失敗しました。Botを停止します。", e)
        sys.exit(1)
    }
  }
}
