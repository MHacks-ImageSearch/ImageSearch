name := "Image Search"

version := "0.1"

scalaVersion := "2.10.1"

libraryDependencies ++= Seq("org.mongodb" %% "casbah" % "2.6.3",
			    "com.typesafe" %% "scalalogging-slf4j" % "1.0.1",
			    "org.slf4j" % "slf4j-api" % "1.7.1",
			    "org.slf4j" % "log4j-over-slf4j" % "1.7.1", 	 // for any java classes looking for this
			    "ch.qos.logback" % "logback-classic" % "1.0.3",
			    "com.novus" %% "salat" % "1.9.2")
