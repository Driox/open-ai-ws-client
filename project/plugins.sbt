logLevel := Level.Warn

addSbtPlugin("org.xerial.sbt" % "sbt-sonatype" % "3.10.0")
// addSbtPlugin("com.github.sbt" % "sbt-pgp" % "2.2.1")
addSbtPlugin("ch.epfl.scala" % "sbt-scalafix" % "0.14.5")
addSbtPlugin("org.scalameta" % "sbt-scalafmt" % "2.5.6")

// Test Coverage plugin.
// ~
// sbt-scoverage is a plugin for SBT that integrates the scoverage code coverage library.
// See more: https://github.com/scoverage/sbt-scoverage
addSbtPlugin("org.scoverage" % "sbt-scoverage" % "2.4.3")

// use to display dependencies graph : sbt dependencyBrowseTree
addDependencyTreePlugin
