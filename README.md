# library_full

This source base requires JDK 8.

Please don't hesitate to contact me at jeff @ langrsoft.com if you have any problems.

Disclaimer: Some of the source in the codebase deliberately stinks. Some of it stinks because, well, it's easy for all of us to write code we're soon not proud of. (No worries--we accept that reality and know that we can incrementally improve the code.)

Eclipse Instructions
---

* From the Eclipse menu, select `File->Import...`.
* From the `Import` wizard dialog, select `Gradle->Gradle Project` and click `Next>`.
* From the `Import Gradle Project` wizard page, enter the location of this directory. For example:
    `c:\Users\jeff\library_full`
  Click `Finish`.
* From the dialog titled `Overwrite existing Eclipse project descriptors?`, click `Overwrite`.
* In the `Package Explorer`, expand the tree and click on the entry `library->src/test/junit->default package`.
* Right-click the selected `Package Explorer` entry and click `Run As->JUnit Test`. You should see at least 200 green unit tests, and they should run in a seconds or so at most.

IntelliJ IDEA Instructions
---

* Import project: Select the build.gradle located in the root of this repo. You should be able to retain the defaults in the `Import Project from Gradle` dialog.
* From the dialog `Gradle Project Data To Import`, keep all modules selected and click OK.
* In the project tool window, expand library_full -> library -> src -> test -> junit.
* Select AllFastTests. Right-click and select Run `AllFastTests`.
* You should see at least 200 green unit tests, and they should run in a second or so at most.
