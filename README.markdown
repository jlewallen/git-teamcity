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

TODO
---------

Make these instructions obsolete with better configuration/scripts/etc...
