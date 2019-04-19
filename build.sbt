name := "StatelessSnake"

version := "0.1"

scalaVersion := "2.12.3"

lazy val akkaVersion = "2.5.4"

lazy val akkaDependencies = Seq(
  "com.typesafe.akka" %% "akka-stream" % akkaVersion,
  "com.typesafe.akka" %% "akka-stream-testkit" % akkaVersion % Test
)

lazy val loggingDependencies = Seq(
  "com.typesafe.akka" %% "akka-slf4j" % "2.5.22",
  "ch.qos.logback" % "logback-classic" % "1.2.3"
)

lazy val graphicsDependencies = Seq(
  "org.fusesource.jansi" % "jansi" % "1.18"
)

libraryDependencies ++= Seq(
  akkaDependencies,
  loggingDependencies,
  graphicsDependencies
).flatten