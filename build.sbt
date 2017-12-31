enablePlugins(ScalaJSPlugin)

name := "frameworkrx"

organization := "io.github.voltir" 

organizationName := "Nick Childers"

crossScalaVersions := Seq("2.11.12", "2.12.4")

version := "0.1.3-SNAPSHOT"

resolvers ++= Seq(
  Resolver.sonatypeRepo("snapshots"),
  Resolver.sonatypeRepo("releases")
)

libraryDependencies += "org.scala-js" %%% "scalajs-dom" % "0.9.4"
libraryDependencies += "io.github.voltir" %%% "scalarx" % "0.3.3-SNAPSHOT"
libraryDependencies += "com.lihaoyi" %%% "scalatags" % "0.6.7"

//Publish Info
licenses += ("MIT License", url("http://www.opensource.org/licenses/mit-license.php"))

homepage := Some(url("http://stabletechs.com/"))

scmInfo := Some(ScmInfo(
  url("https://github.com/Voltir/framework.rx"),
  "scm:git:git@github.com/Voltir/framework.rx.git",
  Some("scm:git:git@github.com/Voltir/framework.rx.git")))

releaseCrossBuild := true

publishMavenStyle := true

publishArtifact in Test := false

publishTo := {
  val nexus = "https://oss.sonatype.org/"
  if (isSnapshot.value)
    Some("snapshots" at nexus + "content/repositories/snapshots")
  else
    Some("releases" at nexus + "service/local/staging/deploy/maven2")
}

sonatypeProfileName := "io.github.voltir"

scalacOptions :=
  "-encoding" :: "UTF-8" ::
  "-unchecked" ::
  "-deprecation" ::
  "-explaintypes" ::
  "-feature" ::
  "-language:_" ::
  "-Xcheckinit" ::
  "-Xfuture" ::
  "-Xlint" ::
  "-Ypartial-unification" ::
  "-Yno-adapted-args" ::
  "-Ywarn-infer-any" ::
  "-Ywarn-nullary-override" ::
  "-Ywarn-nullary-unit" ::
  Nil

pomExtra :=
  <developers>
    <developer>
      <id>Voltaire</id>
      <name>Nick Childers</name>
      <url>https://github.com/voltir/</url>
    </developer>
  </developers>

pomIncludeRepository := { _ => false }
