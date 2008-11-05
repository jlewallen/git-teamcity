A plugin for JetBrains TeamCity (http://www.jetbrains.com/teamcity/) that allows it to use the Git version control system (http://git.or.cz/).

Compiling
---------
Sorry this is so annyoing... You'll have to change the *absolute* URL in the
pom.xml to point to where the maven subdirectory is. In the pom.xml it's
file:///third/temp/git-teamcity/maven. Just change this to reflect your
installation.

Running mvn install will build/compile.

mvn install

Installing
---------

Inside pkg there's the steps necessary to create the agent-side package file.
Then deploy just shows where the jar and the zip should be copied. You'll
notice your agents go down for an ugprade and to get the server side workign
you'll have to restart.

TODO
---------

Make these instructions obsolete with better configuration/scripts/etc...
