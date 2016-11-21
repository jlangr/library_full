# library_full

Eclipse Instructions
---

* From the Eclipse menu, select `File->Import...`.
* From the `Import` wizard dialog, select `Gradle->Gradle Project` and click `Next>`.
* From the `Import Gradle Project` wizard page, enter the location of this directory. For example:
    `c:\Users\jeff\library_full`
  Click `Finish`.
* From the dialog titled `Overwrite existing Eclipse project descriptors?`, click `Overwrite`.
* In the `Package Explorer`, expand the tree and click on the entry `library->src/test/junit->default package`.
* Right-click the selected `Package Explorer` entry and click `Run As->JUnit Test`. You should see at least 190 green unit tests, and they should run in a few seconds at most.

This source base requires JDK 8.

Please don't hesitate to contact me at jeff @ langrsoft.com if you have any problems.

Disclaimer: Some of the source in the codebase deliberately stinks. Some of it stinks because, well, it's easy for all of us to write code we're soon not proud of. (No worries--we accept that reality and know that we can incrementally improve the code.)
