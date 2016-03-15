package com.jween.gradle.soexcluder

class Rule {
    final String name
    List<String> excludeList = []
    List<String> includeList = []
    
    Rule(String name) {
        this.name = name
    }
    
    void exclude(String...args) {
        excludeList += Arrays.asList(args)
    }
    
    void include(String...args) {
        includeList += Arrays.asList(args)
    }
}