package net.seichi915.seichi915botforadmin.configuration

import org.yaml.snakeyaml.Yaml

import java.io.{File, FileOutputStream, FileReader, IOException}
import java.util
import scala.collection.mutable
import scala.jdk.CollectionConverters._
import scala.reflect.ClassTag

object Configuration {
  private val configFile =
    new File(System.getProperty("user.dir"), "config.yml")
  private var configObject = mutable.Map[String, Object]()

  def saveDefaultConfig: Boolean =
    if (!configFile.exists())
      try {
        val inputStream = ClassLoader.getSystemResourceAsStream("config.yml")
        val outputStream = new FileOutputStream(configFile)
        val bytes = new Array[Byte](1024)
        var read = 0
        while ({
          read = inputStream.read(bytes)
          read
        } != -1) outputStream.write(bytes, 0, read)
        inputStream.close()
        outputStream.close()
        true
      } catch {
        case e: Exception =>
          e.printStackTrace()
          false
      } else true

  private def load: Boolean =
    try {
      configObject =
        (new Yaml().load(new FileReader(configFile)): util.LinkedHashMap[
          String,
          Object]).asScala
      true
    } catch {
      case e: Exception =>
        e.printStackTrace()
        false
    }

  // TODO: 強引すぎる
  def get[T: ClassTag](path: String*): Option[T] = {
    if (!load) throw new IOException("Failed to load config.yml.")
    if (path.isEmpty) return None
    try {
      if (path.length == 1)
        configObject.getOrElse(path.head, None) match {
          case t: T => Some(t)
          case None => None
        } else {
        var previousValue = configObject.getOrElse(path.head, None)
        try {
          path
            .drop(1)
            .dropRight(1)
            .foreach(
              p =>
                previousValue = previousValue
                  .asInstanceOf[util.LinkedHashMap[String, Object]]
                  .asScala
                  .getOrElse(p, None))
          previousValue
            .asInstanceOf[util.LinkedHashMap[String, Object]]
            .asScala
            .getOrElse(path.last, None) match {
            case obj: Object => Some(obj.asInstanceOf[T])
            case None        => None
          }
        } catch {
          case _: ClassCastException     => None
          case _: NoSuchElementException => None
        }
      }
    } catch {
      case _: NullPointerException => None
    }
  }

  def getString(path: String*): Option[String] = get[String](path: _*)

  def getBoolean(path: String*): Option[Boolean] = get[Boolean](path: _*)

  def getInt(path: String*): Option[Int] = get[Int](path: _*)

  def getDouble(path: String*): Option[Double] = get[Double](path: _*)

  def getLong(path: String*): Option[Long] = get[Long](path: _*)

  def getList[T](path: String*): Option[List[T]] =
    try {
      val obj = get[Object](path: _*)
      Some(obj.asInstanceOf[util.ArrayList[T]].asScala.toList)
    } catch {
      case _: ClassCastException => None
    }

  def getToken: String = getString("Token").getOrElse("")
}
