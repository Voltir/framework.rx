enablePlugins(ScalaJSPlugin)

name := "frameworkrx"

organization := "com.stabletechs"

scalaVersion := "2.11.8"

version := "0.1.2"

libraryDependencies += "org.scala-js" %%% "scalajs-dom" % "0.9.0"
libraryDependencies += "com.lihaoyi" %%% "scalarx" % "0.3.1"
libraryDependencies += "com.lihaoyi" %%% "scalatags" % "0.5.5"

//Publish Info
licenses += ("MIT License", url("http://www.opensource.org/licenses/mit-license.php"))

homepage := Some(url("http://stabletechs.com/"))

scmInfo := Some(ScmInfo(
  url("https://github.com/Voltir/framework.rx"),
  "scm:git:git@github.com/Voltir/framework.rx.git",
  Some("scm:git:git@github.com/Voltir/framework.rx.git")))

publishMavenStyle := true
publishArtifact in Test := false

publishTo := {
  val nexus = "https://oss.sonatype.org/"
  if (isSnapshot.value)
    Some("snapshots" at nexus + "content/repositories/snapshots")
  else
    Some("releases" at nexus + "service/local/staging/deploy/maven2")
}

sonatypeProfileName := "com.stabletechs"

pomExtra :=
  <developers>
    <developer>
      <id>Voltaire</id>
      <name>Nick Childers</name>
      <url>https://github.com/voltir/</url>
    </developer>
  </developers>

pomIncludeRepository := { _ => false }