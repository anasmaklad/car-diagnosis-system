name := "car-diagnosis-system"
version := "1.0"
scalaVersion := "2.13.16"

libraryDependencies ++= Seq(
  "io.circe" %% "circe-generic" % "0.14.13",
  "io.circe" %% "circe-parser" % "0.14.13"
)

// Enable better error messages
scalacOptions ++= Seq(
  "-deprecation",
  "-feature",
  "-unchecked",
  "-Xlint"
)

Compile / mainClass := Some("CarDiagnosisSystem") 