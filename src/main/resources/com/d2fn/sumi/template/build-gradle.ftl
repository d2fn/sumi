apply plugin: 'application'
mainClassName = 'Main'
archivesBaseName = '${sketchName}'
version = '0.1'

apply plugin: 'java'
sourceCompatibility = 1.8

apply plugin: 'idea'

repositories {
    mavenCentral()
}

dependencies {
    compile fileTree(dir: 'lib', include: '*.jar')
}

sourceSets {
    main {
        java {
            srcDirs 'src'
        }
        resources {
            srcDirs 'data'
        }
    }
}

jar {
    from(configurations.compile.collect { it.isDirectory() ? it : zipTree(it) }) {
        exclude "META-INF/*.SF"
        exclude "META-INF/*.DSA"
        exclude "META-INF/*.RSA"
    }
    manifest {
        attributes 'Implementation-Title': '${sketchName}',
                'Implementation-Version': version,
                'Built-By': System.getProperty('user.name'),
                'Built-Date': new Date(),
                'Built-JDK': System.getProperty('java.version'),
                'Main-Class': mainClassName
    }
}

run {
    if(project.hasProperty('args')) {
        args findProperty('args').tokenize(" ")
    }
}

