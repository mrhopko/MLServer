ThisBuild / version := "0.01"

ThisBuild / scalaVersion := "2.13.10"

lazy val root = (project in file("."))
  .settings(
    name := "MLServer"
  )

libraryDependencies ++= Seq(
  "dev.zio" %% "zio" % "2.0.2",
  "dev.zio" %% "zio-streams" % "2.0.2",
  "dev.zio" %% "zio-json" % "0.3.0-RC8",
  "dev.zio" %% "zio-http" % "0.0.3",
  "org.json4s" %% "json4s-jackson" % "3.6.12",
)