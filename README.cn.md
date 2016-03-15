Gradle Android so文件过滤插件
============================

此插件可以根据buildType或者flavor来过滤so文件


使用
-----

1. 在 build.gradle 中添加以下脚本

    ```groovy
    buildscript {
       repositories {
          jcenter()
          maven { // package is not in jcenter for now
              url  "http://dl.bintray.com/jween/maven" 
          }
       }

       dependencies {
          classpath 'com.jween.gradle:android-soexcluder:1.1'
       }
    }


    apply plugin: 'com.android.application'
    apply plugin: 'android-soexcluder'
    ```
    **注意**: 你必须在apply了android application插件后apply本插件


配置
----

1. 根据flavor移除指定so文件

    ```groovy
    soexcluder {
        // 为flavor1 移除v7a的foo.so与v8a的bar.so文件
        flavor1 {
            exclude "lib/armeabi-v7a/foo.so", "lib/armeabi-v8a/bar.so"
        }
        
        // 对debug buildType保留v7a和x86 abi的除foo.so之外的所有so文件
        debug {
            include "lib/armeabi-v7a/*.so" 
            include "lib/x86/*.so"
            exclude "lib/armeabi-v7a/foo.so"
            exclude "lib/x86/foo.so"
        }
    }
    ```

2. 与 gradle 的 include/exclude 的Ant路径正则用法完全一致

    ```groovy
    soexcluder {
        
        // 对debug buildType保留除foo.so之外的所有so文件
        debug {
            include "**/*" 
            exclude "**/foo.so"
        }
    }
    ```

3. 你甚至可以对buildType以及flavor使用正则表达式!
 
     ```groovy
     soexcluder {
         
         // 当flavor或者buildType的名字以o结尾的时候, 移除所有so文件
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
 