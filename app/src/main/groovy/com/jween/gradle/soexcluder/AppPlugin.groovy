package com.jween.gradle.soexcluder

import org.gradle.api.Plugin
import org.gradle.api.Project

import java.util.Arrays
import java.util.List

/**
 * Created with Atom by Jween on 3/14/2016
 *
 */
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
            def excludeList = []
            def includeList = []
            
            // 
            // find all flavors and build type of this variont
            // 
            def flavors = (variant.productFlavors.name).collect()
            flavors << variant.buildType.name
            
            // 
            // detect all excluding rules for this variant 
            // 
            soexcluder.matching { rule ->
                flavors.any { flavName ->
                    flavName.matches(rule.name)
                }
            }.all { rule ->
                excludeList.addAll(rule.excludeList)
                includeList.addAll(rule.includeList)
            }
            
            // 
            // Find and remove excluded so files before package application
            // 
            variant.outputs.each { output ->
                output.packageApplication.doFirst { pkgApp ->
                    boolean dealWithIt = excludeList.size > 0 || includeList.size > 0
                    
                    if (dealWithIt) {    
            
                        pkgApp.jniFolders.each { dir ->
                            // 
                            // find all so files needed by this output
                            // 
                            FileTree reserve = project.fileTree(dir) { fileTree -> 
                                includeList?.each {
                                    fileTree.include it
                                }
                                
                                excludeList?.each {
                                    fileTree.exclude it
                                }
                            }
                            
                            // 
                            // remove all excluded so files
                            // 
                            FileCollection remove = project.fileTree(dir).minus(reserve)
                            
                            logger.quiet "* Removing for ${output.name}: ${remove.getAsPath()}"
                            remove.each { 
                                it.delete()
                            }
                        }
                    }
                }
            }
        }
    }
}
