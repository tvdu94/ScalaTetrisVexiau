ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "3.3.1"
import indigoplugin._
addCommandAlias("buildGame", ";compile;fastLinkJS;indigoBuild")
addCommandAlias("runGame", ";compile;fastLinkJS;indigoRun")
addCommandAlias("buildGameFull", ";compile;fullLinkJS;indigoBuildFull")
addCommandAlias("runGameFull", ";compile;fullLinkJS;indigoRunFull")
lazy val mygame =
  (project in file("."))
    .enablePlugins(ScalaJSPlugin, SbtIndigo) // Enable the Scala.js and Indigo plugins
    .settings( // Standard SBT settings
      name := "mygame",
      version := "0.0.1",
      scalaVersion := "3.3.1",
      organization := "org.mygame"
    )
    .settings( // Indigo specific settings
      indigoOptions :=
        IndigoOptions.defaults
          .withTitle("Tetris VEXIAU")
          .withWindowSize(1024, 720),
      libraryDependencies ++= Seq(
        "io.indigoengine" %%% "indigo" % "0.15.2",
        "io.indigoengine" %%% "indigo-extras" % "0.15.2",
        "io.indigoengine" %%% "indigo-json-circe" % "0.15.2",
      )
    )