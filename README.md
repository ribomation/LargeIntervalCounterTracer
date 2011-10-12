Large Interval Counter Tracer
=============================

This is a CA-Wily Introscope Agent extension that is similar to the standard PerIntervalCounter tracer-type,
that counts the number of invocations per 15 seconds, but this one handles large intervals in minutes and hours.

Requirements
============

This Java library is an agent-side plug-in for CA-Wily Introscope. Introscope is a commercial tool for
application performance management (APM) of (large) Java applications in production. In order to use
and/or compile this library you need to have a valid Introscope license. For compilation, a valid Agent.jar
file is required as well. You will need to manually install this JAR file into your local Maven cache,
using the instructions in the POM file of this project.

This library has been developed and tested using Introscope version 8.

Installation
============

* Unpack the distribution ZIP (e.g. LargeIntervalCounterTracer-1.2-bin.zip) file.

* Drop the tracer JAR file (e.g. LargeIntervalCounterTracer-1.2.jar) into the ext/ directory
  of an Introscope Agent installation.

* Next, copy the LargeIntervalCounterTracer.pbd file into the Introscope Agent installation directory
  and add the PBD file to the directives list in the Agent's profile. Finally, review the settings in the PBD file.


Configuration
=============

Configuration of the tracer is performed within it's PBD file. See the user guide.

