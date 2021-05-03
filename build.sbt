enablePlugins(ScalaJSPlugin)

scalaVersion := "2.13.3"
publishTo := Some( Resolver.file("file",  new File( "/var/www/maven" ) ) )
scalacOptions ++= Seq("-feature", "-deprecation")
resolvers += "dragonfly.ai" at "https://code.dragonfly.ai/"
organization := "ai.dragonfly.code"
version := "0.01"
libraryDependencies += "org.scala-js" %%% "scalajs-dom" % "1.1.0"
scalaJSUseMainModuleInitializer := true

