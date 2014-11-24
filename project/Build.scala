import sbt.Keys._
import sbt._
import play.Play.autoImport._
import PlayKeys._

object ApplicationBuild extends Build {

  val appName = "CMUSensorNetwork"
  val appVersion = "1.0-SNAPSHOT"


  val appDependencies = Seq(
    // Add your project dependencies here,
    javaCore, javaJdbc, javaEbean
  )

  val main = Project(appName, file(".")).enablePlugins(play.PlayScala).settings(
    version := appVersion,
    libraryDependencies ++= appDependencies,
    scalaVersion := "2.10.4"
  )

}
