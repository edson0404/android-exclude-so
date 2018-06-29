[Gradle Android .so Exclude filter plugin]
============================

Plugin Forked from @Jween. Repo (https://github.com/Jween/android-soexcluder)

This plugin can be based on buildType or flavor to filter out .so files and Exclude those file from your compiled artifact


Use
---

1. In build.gradle Add the following script

    ```groovy
    buildscript {
       repositories {
          jcenter()
       }

       dependencies {
          classpath 'com.android.exclude.so:android-exclude-so-plugin:2.0'
       }
    }


    apply plugin: 'com.android.application'
    apply plugin: 'android-exclude-so-plugin'
    ```
    **Note**: `android-exclude-so-plugin`Plugins must be imported with `com.android.application` Plugin use


Configuration
-------------

1. According to flavor remove specific .so files

    ```groovy
    soexcluder {
        // For flavor1 remove v7a of foo.so versus v8a of bar.so file
        flavor1 {
            exclude "lib/armeabi-v7a/foo.so", "lib/armeabi-v8a/bar.so"
        }

        // Correct debug buildType reserved v7a with x86 abi with foo.so all other than .so files.
        debug {
            include "lib/armeabi-v7a/*.so"
            include "lib/x86/*.so"
            exclude "lib/armeabi-v7a/foo.so"
            exclude "lib/x86/foo.so"
        }
    }
    ```

2. Versus gradle of include/exclude of Ant The path is exactly the same usage

    ```groovy
    soexcluder {

        // Correct debug buildType Reserved foo.so All other .so files.
        debug {
            include "**/*"
            exclude "**/foo.so"
        }
    }
    ```

3. You can even use regular expressions for buildType and flavor!

     ```groovy
     soexcluder {

         // When the name of the flavor or buildType ends with o, remove all so files
         ".*o" {
             exclude "**/*"
         }
     }
     ```

License
-------