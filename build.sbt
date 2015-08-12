name := "simple-jmx-agent"

organization := "woshilaiceshide"

version := "1.0"

description := "a simple-jmx-agent using a single fixed port"

licenses += ("MIT", url("http://opensource.org/licenses/MIT"))

publishMavenStyle := true

crossPaths := false

autoScalaLibrary := false

enablePlugins(BintrayPlugin)

bintrayRepository := "maven"

bintrayOrganization := None

bintrayVcsUrl := Some(s"git@github.com:woshilaiceshide/${name.value}.git")

bintrayReleaseOnPublish in ThisBuild := false

transitiveClassifiers := Seq("sources")

EclipseKeys.withSource := true

EclipseKeys.projectFlavor := EclipseProjectFlavor.Java

javacOptions ++= Seq("-source", "1.7")

net.virtualvoid.sbt.graph.Plugin.graphSettings

unmanagedSourceDirectories in Compile <+= baseDirectory( _ / "src" / "java" )

packageOptions in (Compile, packageBin) +=  {
  Package.ManifestAttributes( "Premain-Class" -> "woshilaiceshide.jxm.agent.Agent" )
}