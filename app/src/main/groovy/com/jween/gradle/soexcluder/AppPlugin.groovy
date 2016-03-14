
package com.jween.gradle.soexcluder

import com.jween.gradle.soexcluder.extension.*

import org.gradle.api.Plugin
import org.gradle.api.Project

import java.util.Arrays
import java.util.List


class AppPlugin implements Plugin<Project> {

    def /* lateinit */ logger
    
    @Override
    def void apply(Project project) {    
        logger = project.logger
    
        def soexcluder = project.container(Rule)

        soexcluder.all {
            // do nothing
        }
        
        project.extensions.soexcluder = soexcluder   
        
        project.afterEvaluate {
            configPlugin(project, soexcluder)
        }
    }
    
    void configPlugin(Project project, def soexcluder) {
        
        
        project.android.applicationVariants.all { variant ->
            // variant.productFlavors.find { it.flavorDimension == "foo" }
    
            
    
            def excludeList = []
            def includeList = []
            def flavors = (variant.productFlavors.name).collect()
            flavors << variant.buildType.name
            flavors.each {print "*${it}"}
            println "+++++"
            
            soexcluder.matching { rule ->
                flavors.any { flavName ->
                    flavName.matches(rule.name)
                }
            }.all { rule ->
                
                println "excludeList is ${rule.excludeList*.toString()}, includeList is ${rule.includeList*.toString()}"
                excludeList.addAll(rule.excludeList)
                includeList.addAll(rule.includeList)
            }
            
            variant.outputs.each { output ->
                output.packageApplication.doFirst { pkgApp ->
                    
                    println "↓"
                    println "-->variant[${variant.name}], output[${output.name}]"
                    println "JNI: ${pkgApp.jniFolders*.toString()}"
    
                    
                    // boolean dealWithIt = inFlavors?.any { inFlavor ->
                    //     flavors.any { vari ->
                    //         vari.matches(inFlavor)
                    //     }
                    // }
                    
                    boolean dealWithIt = excludeList.size > 0 || includeList.size > 0
                    
                    if (dealWithIt) {    
                        println "---->test exclude ${excludeList*.toString()},  include ${includeList*.toString()}"
                        
                        pkgApp.jniFolders.each { dir ->
                            FileTree reserve = project.fileTree(dir) { fileTree -> 
                                includeList?.each {
                                    println "include $it"
                                    fileTree.include it
                                }
                                
                                excludeList?.each {
                                    println "exclude $it"
                                    fileTree.exclude it
                                }
                            }
                            
                            FileCollection remove = project.fileTree(dir).minus(reserve)
                            
                            println "remove paths are ${remove.getAsPath()}"
                            remove.each { 
                                it.delete()
                                println "$it has been deleted" 
                            }
                            // onSoFiles.each { rule ->
                            //     
                            //     println "----> Scanning dir ${dir} with rule ${rule}"
                            //     Helper.visit(dir.getAbsolutePath(), rule).each {
                            //         println "----> delete file ${it.name}"
                            //         file.delete()
                            //     }
                            // }
                        }
                        
                        
                    }
                    
    
                    
                }
            }
        }
    }
}


class Rule {
    final String name
    List<String> excludeList
    List<String> includeList
    
    Rule(String name) {
        this.name = name
    }
    
    void exclude(String...args) {
        excludeList = Arrays.asList(args)
    }
    
    void include(String...args) {
        includeList = Arrays.asList(args)
    }
}