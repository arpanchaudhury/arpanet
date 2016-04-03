scalaVersion in Global := "2.10.4"

logLevel := Level.Info

scalacOptions in compile in Compile ++= Seq(
  "-Xlint",
  "-Ywarn-dead-code"
)

scalacOptions ++= Seq(
  "-unchecked",
  "-deprecation",
  "-feature",
  "-target:jvm-1.8",
  "-encoding", "UTF-8"
)

javacOptions in compile in Compile ++= Seq(
  "-source", "1.8", 
  "-target", "1.8"
)

javaOptions ++= Seq(
  "-Duser.language=en-US"
)

lazy val libraries = Seq(
  ws,
  cache,
  "org.reactivemongo" %% "reactivemongo" % "0.11.9",
  "org.scala-lang.modules" %% "scala-async" % "0.9.3",
  "com.sksamuel.scrimage" %% "scrimage-core" % "2.1.0"
)

val api = project.in(file("."))
            .enablePlugins(PlayScala)
            .settings(
              name := "api",
              version := "1.0.0",
              scalaVersion := "2.11.7",
              libraryDependencies ++= libraries
            )
