Gradle Android So Excluder Plugin
=========================================

This plugin will help you exclude so files by flavor or buildType


Usage
-----

1. Add the following scripts to your build.gradle

    ```groovy
    buildscript {
       repositories {
          jcenter()
       }

       dependencies {
          classpath 'com.jween.gradle:android-soexcluder:1.0'
       }
    }


    apply plugin: 'com.android.application' //or apply plugin: 'java'
    apply plugin: 'android-soexcluder'
    ```
    **Note**: You MUST apply android-soexcluder AFTER android application plugin

2. Wish we have step 2! 
 
    But that's it, it a quite simple small tool to deal with so files.

Configuration
-------------

1. Exclude specific so files by flavors

    ```groovy
    soexcluder {
        // exclude armeabi-v7a/foo.so and armeabi-v8a/bar.so for flavor1
        flavor1 {
            exclude "lib/armeabi-v7a/foo.so", "lib/armeabi-v8a/bar.so"
        }
        
        // Reserve only v7a so files except foo.so for debug buildType
        debug {
            include "lib/armeabi-v7a/*.so" 
            exclude "lib/armeabi-v7a/foo.so"
        }
        
        ".*y" {
            exclude "**/*"
        }
    }
    ```

2. Exact the same path pattern with gradle include/exclude

    ```groovy
    soexcluder {
        
        // Reserve all so files for debug buildType except foo.so
        debug {
            include "**/*" 
            exclude "**/foo.so"
        }
        
        ".*y" {
            exclude "**/*"
        }
    }
    ```

3. You can even use regex in flavor/buildType entry!
 
     ```groovy
     soexcluder {
         
         // Remove all so files if the flavor or buildType name ends with 'o' 
         ".*o" {
             exclude "**/*"
         }
     }
     ```

License   
-------   
 
    "THE BEER-WARE LICENSE" (Revision 42):

    <Jween.Lau@gmail.com> wrote this file. As long as you retain this notice
    you can do whatever you want with this stuff. If we meet some day, and you think
    this stuff is worth it, you can buy me a beer in return. -Jween Lau
 