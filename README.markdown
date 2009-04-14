A plugin for JetBrains TeamCity (http://www.jetbrains.com/teamcity/) that allows it to use the Git version control system (http://git.or.cz/).

Compiling
---------
Running mvn install will build/compile.

mvn install

Installing
---------

Inside pkg there's the steps necessary to create the agent-side package file.
Then deploy just shows where the jar and the zip should be copied. You'll
notice your agents go down for an ugprade and to get the server side workign
you'll have to restart.

Logging
---------
Add the following lines to <teamcity installation directory>/conf/teamcity-server-log4j.xml

  <appender name="GIT.LOG" class="org.apache.log4j.RollingFileAppender">
    <param name="file" value="${teamcity_logs}teamcity-git.log"/>
    <layout class="org.apache.log4j.PatternLayout">
      <param name="ConversionPattern" value="[%d] %6p [%t] - %30.30c - %m %n"/>
    </layout>
  </appender>

  <category name="jetbrains.buildServer.buildTriggers.vcs.git">
    <priority value="DEBUG"/>
    <appender-ref ref="GIT.LOG"/>
  </category>

TODO
---------

Make these instructions obsolete with better configuration/scripts/etc...
