name := "simple-jmx-agent"

organization := "woshilaiceshide"

version := "1.0-SNAPSHOT"

description := "a simple-jmx-agent using a single fixed port"

publishMavenStyle := true

crossPaths := false

autoScalaLibrary := false

transitiveClassifiers := Seq("sources")

EclipseKeys.withSource := true

EclipseKeys.projectFlavor := EclipseProjectFlavor.Java

javacOptions ++= Seq("-source", "1.7", "-target", "1.7")

net.virtualvoid.sbt.graph.Plugin.graphSettings

unmanagedSourceDirectories in Compile <+= baseDirectory( _ / "src" / "java" )

packageOptions in (Compile, packageBin) +=  {
  Package1.ManifestAttributes( "Premain-Class" -> "woshilaiceshide.jxm.agent.Agent" )
}