// shadow sbt-scalajs' crossProject and CrossType until Scala.js 1.0.0 is released
import sbtcrossproject.CrossPlugin.autoImport.{crossProject, CrossType}
import com.typesafe.tools.mima.core.{ProblemFilters, ReversedMissingMethodProblem}

lazy val scala210 = "2.10.7"
lazy val scala211 = "2.11.12"
lazy val scala212 = "2.12.8"
lazy val scala213 = "2.13.0"

lazy val sprayJson =
  crossProject(JVMPlatform, JSPlatform, NativePlatform)
    .crossType(CrossType.Full)
    .in(file("."))
    .settings(
      name := "spray-json",
      version := "1.3.6-SNAPSHOT",
      scalaVersion := crossScalaVersions.value.head,
      scalacOptions ++= Seq("-feature", "-language:_", "-unchecked", "-deprecation", "-Xlint", "-encoding", "utf8"),
      (scalacOptions in doc) ++= Seq("-doc-title", name.value + " " + version.value),
      scalaBinaryVersion := {
        val sV = scalaVersion.value
        if (CrossVersion.isScalaApiCompatible(sV))
          CrossVersion.binaryScalaVersion(sV)
        else
          sV
      },
      // Workaround for "Shared resource directory is ignored"
      // https://github.com/portable-scala/sbt-crossproject/issues/74
      unmanagedResourceDirectories in Test += (baseDirectory in ThisBuild).value / "shared/src/test/resources"
    )
    .enablePlugins(spray.boilerplate.BoilerplatePlugin)
    .platformsSettings(JVMPlatform, JSPlatform)(
      libraryDependencies ++= (CrossVersion.partialVersion(scalaVersion.value) match {
        case Some((2, 10)) => Seq(
          "org.specs2" %%% "specs2-core" % "3.8.9" % "test",
          "org.specs2" %%% "specs2-scalacheck" % "3.8.9" % "test",
          "org.scalacheck" %%% "scalacheck" % "1.13.4" % "test"
        )
        case Some((2, n)) if n >= 11 => Seq(
          "org.specs2" %%% "specs2-core" % "4.5.1" % "test",
          "org.specs2" %%% "specs2-scalacheck" % "4.5.1" % "test",
          "org.scalacheck" %%% "scalacheck" % "1.14.0" % "test"
        )
        case _ => Nil
      })
    )
    .configurePlatforms(JVMPlatform)(_.enablePlugins(SbtOsgi))
    .jvmSettings(
      crossScalaVersions := Seq(scala213, scala212, scala211, scala210),
      OsgiKeys.exportPackage := Seq("""spray.json.*;version="${Bundle-Version}""""),
      OsgiKeys.importPackage := Seq("""scala.*;version="$<range;[==,=+);%s>"""".format(scalaVersion.value)),
      OsgiKeys.importPackage ++= Seq("""spray.json;version="${Bundle-Version}"""", "*"),
      OsgiKeys.additionalHeaders := Map("-removeheaders" -> "Include-Resource,Private-Package"),
      mimaPreviousArtifacts := (CrossVersion.partialVersion(scalaVersion.value) match {
        case Some((2, 13)) => Set("io.spray" %% "spray-json" % "1.3.5")
        case _ => Set("1.3.2", "1.3.3", "1.3.4", "1.3.5").map { v => "io.spray" %% "spray-json" % v }
      }),
      mimaBinaryIssueFilters := Seq(
        ProblemFilters.exclude[ReversedMissingMethodProblem]("spray.json.PrettyPrinter.organiseMembers")
      )
    )
    .jsSettings(
      crossScalaVersions := Seq(scala212, scala211)
    )
    .nativeSettings(
      crossScalaVersions := Seq(scala211),
      // Disable tests in Scala Native until testing frameworks for it become available
      unmanagedSourceDirectories in Test := Seq.empty
    )

lazy val sprayJsonJVM = sprayJson.jvm
lazy val sprayJsonJS = sprayJson.js
lazy val sprayJsonNative = sprayJson.native

lazy val root = (project in file("."))
  .aggregate(sprayJsonJVM, sprayJsonJS, sprayJsonNative)
  .settings(
    publish := {},
    publishLocal := {}
  )
