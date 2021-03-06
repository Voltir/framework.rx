
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

import com.typesafe.sbt.pgp.PgpKeys._
import sbt.Keys._
import sbt.internal.util.ConsoleLogger
import sbt.librarymanagement.PublishConfiguration
import sbt.sbtpgp.Compat.publishSignedConfigurationTask
import sbt.{AutoPlugin, Package, ThisBuild}

object PublishingPlugin extends AutoPlugin {
  protected val logger: ConsoleLogger = ConsoleLogger()

  override def trigger = allRequirements

  override lazy val globalSettings = Seq(
    pomIncludeRepository := (_ => false)
    , packageOptions += {
      val attributes = Map(
        "X-Built-By" -> System.getProperty("user.name")
        , "X-Build-JDK" -> System.getProperty("java.version")
        , "X-Version" -> (version in ThisBuild).value
        , "X-Build-Timestamp" -> DateTimeFormatter.ISO_OFFSET_DATE_TIME.format(ZonedDateTime.now())
      )
      attributes.foreach {
        case (k, v) =>
          logger.info(s"Manifest value: $k = $v")
      }
      Package.ManifestAttributes(attributes.toSeq: _*)
    }
  )

  override lazy val projectSettings = Seq(
    publishConfiguration := withOverwrite(publishConfiguration.value, isSnapshot.value)
    , publishSignedConfiguration := withOverwrite(publishSignedConfigurationTask.value, isSnapshot.value)
    , publishLocalConfiguration ~= withOverwriteEnabled
    , publishLocalSignedConfiguration ~= withOverwriteEnabled
  )

  private def withOverwriteEnabled(config: PublishConfiguration) = {
    config.withOverwrite(true)
  }

  private def withOverwrite(config: PublishConfiguration, isSnapshot: Boolean) = {
    // in case overwrite is already enabled (snapshots, smth else) we should not disable it
    config.withOverwrite(config.overwrite || isSnapshot)
  }
}