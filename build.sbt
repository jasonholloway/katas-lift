name := "liftKata"
organization := "com.woodpigeon"
scalaVersion := "2.12.2"

libraryDependencies ++= Seq(
    "org.scalacheck" %% "scalacheck" % "1.13.4" % "test",
    "org.scalatest" %% "scalatest" % "3.0.1" % "test",
    "org.scalaz" %% "scalaz-core" % "7.2.14"
)

testOptions in Test += Tests.Argument(TestFrameworks.ScalaCheck, "-verbosity", "1")
