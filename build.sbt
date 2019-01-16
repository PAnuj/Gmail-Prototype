name := """gmail-prototype"""
organization := "promolta"

version := "1.0-SNAPSHOT"

lazy val myProject = (project in file(".")).enablePlugins(PlayJava, PlayEbean)

scalaVersion := "2.12.6"

crossScalaVersions := Seq("2.12.6", "2.11.12")

libraryDependencies += guice
libraryDependencies += javaJdbc
libraryDependencies += "mysql" % "mysql-connector-java" % "5.1.45"