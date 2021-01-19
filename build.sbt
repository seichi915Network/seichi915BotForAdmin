ThisBuild / scalaVersion := "2.13.4"
ThisBuild / version := "1.0.0"
ThisBuild / description := "seichi915Adminの集いにいるDiscordBot"

resolvers ++= Seq(
  "hub.spigotmc.org" at "https://hub.spigotmc.org/nexus/content/repositories/snapshots/",
  "oss.sonatype.org" at "https://oss.sonatype.org/content/repositories/snapshots",
  "maven.elmakers.com" at "https://maven.elmakers.com/repository/",
  "jcenter.bintray.com" at "https://jcenter.bintray.com",
  "maven.apache.org" at "https://maven.apache.org"
)

libraryDependencies ++= Seq(
  "net.dv8tion" % "JDA" % "4.1.1_101",
  "com.jagrosh" % "jda-utilities" % "3.0.1",
  "org.yaml" % "snakeyaml" % "1.21"
)

assemblyMergeStrategy in assembly := {
  case PathList("javax", "servlet", _ @ _*) => MergeStrategy.first
  case PathList(ps @ _*) if ps.last endsWith ".properties" => MergeStrategy.first
  case PathList(ps @ _*) if ps.last endsWith ".xml" => MergeStrategy.first
  case PathList(ps @ _*) if ps.last endsWith ".types" => MergeStrategy.first
  case PathList(ps @ _*) if ps.last endsWith ".class" => MergeStrategy.first
  case PathList(ps @ _*) if ps.last endsWith "plugin.yml" => MergeStrategy.first
  case PathList(ps @ _*) if ps.last endsWith "config.yml" => MergeStrategy.first
  case PathList(ps @ _*) if ps.last endsWith "Syntax.java" => MergeStrategy.first
  case "application.conf" => MergeStrategy.concat
  case "unwanted.txt" => MergeStrategy.discard
  case x =>
    val oldStrategy = (assemblyMergeStrategy in assembly).value
    oldStrategy(x)
}

lazy val root = (project in file("."))
  .settings(
    name := "seichi915BotForAdmin",
    assemblyOutputPath in assembly := baseDirectory.value / "target" / "build" / s"seichi915BotForAdmin-${version.value}.jar"
  )