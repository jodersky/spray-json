inThisBuild(Seq(
  organization := "io.crashbox",
  organizationHomepage := Some(new URL("https://crashbox.io")),
  description := "A Scala library for easy and idiomatic JSON (de)serialization",
  homepage := Some(new URL("https://github.com/jodersky/spray-json")),
  startYear := Some(2011),
  licenses := Seq("Apache 2" -> new URL("http://www.apache.org/licenses/LICENSE-2.0.txt")),
  publishMavenStyle := true,
  useGpg := true,
  publishTo := {
    val nexus = "https://oss.sonatype.org/"
    if (version.value.trim.endsWith("SNAPSHOT"))
      Some("snapshots" at nexus + "content/repositories/snapshots")
    else
      Some("releases" at nexus + "service/local/staging/deploy/maven2")
  },
  pomIncludeRepository := { _ => false },
  pomExtra :=
    <scm>
      <url>git://github.com/jodersky/spray-json.git</url>
      <connection>scm:git:git@github.com:jodersky/spray-json.git</connection>
    </scm>
      <developers>
        <developer><id>sirthias</id><name>Mathias Doenitz</name></developer>
        <developer><id>jrudolph</id><name>Johannes Rudolph</name></developer>
        <developer><id>jodersky</id><name>Jakob Odersky</name></developer>
      </developers>
))
