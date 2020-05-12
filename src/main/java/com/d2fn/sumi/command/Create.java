package com.d2fn.sumi.command;

import com.d2fn.sumi.Sumi;
import com.d2fn.sumi.SumiSettings;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.PosixFilePermission;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

import static java.nio.file.attribute.PosixFilePermission.*;

public class Create implements Command {

    private static final Configuration ftlConfig = buildFtlConfiguration();

    private final SumiSettings sumi;
    private final SketchView sketchView;

    public Create(SumiSettings sumi, String[] args) {
        if(args.length == 0) {
            throw new RuntimeException("no sketch name given");
        }
        this.sumi = sumi;
        final String workingDirectory = System.getProperty("user.dir");
        final String sketchName = args[0];
        final String sketchDirectory = String.format("%s%s%s", workingDirectory, File.separator, sketchName);
        this.sketchView = new SketchView(sketchName, sketchDirectory);
    }


    @Override
    public CommandResponse run() {

        // steps
        // - create new sketch directory in working directory
        final File sketchDir = new File(sketchView.getSketchDirectory());
        if(sketchDir.exists()) {
            return CommandResponse.error("sketch directory already exists: " + sketchView.getSketchDirectory());
        }

        sketchDir.mkdirs();
        if(!sketchDir.exists() || !sketchDir.isDirectory()) {
            return CommandResponse.error("failed to create sketch directory: " + sketchView.getSketchDirectory());
        }

        // - create template build.gradle and copy into root
        try {
            makeFileFromTemplate("template/build-gradle.ftl", sketchView.getGradleBuildPath());
        } catch (IOException e) {
            return CommandResponse.error("error loading build gradle template", e);
        } catch (TemplateException e) {
            return CommandResponse.error("error creating build.gradle from template", e);
        }

        // - create template run script
        try {
            makeFileFromTemplate("script/run.ftl", sketchView.getRunScriptPath());
            final Set<PosixFilePermission> perms = new HashSet<>();
            perms.add(OWNER_READ);
            perms.add(OWNER_WRITE);
            perms.add(OWNER_EXECUTE);
            Files.setPosixFilePermissions(Paths.get(sketchView.getRunScriptPath()), perms);
        } catch (IOException e) {
            return CommandResponse.error("error loading run script template", e);
        } catch (TemplateException e) {
            return CommandResponse.error("error creating run script from template", e);
        }

        // - copy sumi.jar into lib/
        final File libDir = new File(sketchView.getLibDir());
        libDir.mkdirs();
        if(!libDir.isDirectory()) {
            return CommandResponse.error("Error creating lib subdirectory in sketch directory: " + libDir);
        }
        final String sumiSrcJar = sumi.getSumiHome() + File.separator + sumi.getSumiJarName();
        final String sumiDstJar = sketchView.getLibDir() + File.separator + sumi.getSumiJarName();
        try {
            Files.copy(Paths.get(sumiSrcJar), Paths.get(sumiDstJar), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            return CommandResponse.error("error copying " + sumiSrcJar + " into " + sketchView.getLibDir(), e);
        }

        // - create template sketch Java file and copy into src/main/java
        // first ensure src dir
        final File srcDir = new File(sketchView.getSrcDirectory());
        srcDir.mkdirs();
        if(!srcDir.isDirectory()) {
            return CommandResponse.error("Error creating source subdirectory in sketch directory: " + srcDir);
        }
        try {
            makeFileFromTemplate("template/sketch-java.ftl", sketchView.getJavaSketchPath());
            makeFileFromTemplate("template/main-java.ftl", sketchView.getJavaMainPath());
        } catch (IOException e) {
            return CommandResponse.error("error loading sketch-java.ftl template", e);
        } catch (TemplateException e) {
            return CommandResponse.error("error creating " + sketchView.getJavaSketchPath() + " from template", e);
        }

        final File dataDir = new File(sketchView.getDataDirectory());
        dataDir.mkdirs();
        if(!dataDir.isDirectory()) {
            return CommandResponse.error("Error creating data subdirectory in sketch directory: " + dataDir);
        }

        // - create gradle wrapper
        final GradleWrapper gradleWrapper = new GradleWrapper(sketchView.getSketchDirectory());
        final CommandResponse resp = gradleWrapper.run();
        if(resp.isError()) {
            return resp;
        }

        return CommandResponse.success();
    }

    private void makeFileFromTemplate(String templatePath, String destPath) throws IOException, TemplateException {
        final Template buildGradleTemplate = ftlConfig.getTemplate(templatePath);
        final FileOutputStream fos = new FileOutputStream(new File(destPath));
        final Writer out = new OutputStreamWriter(fos);
        buildGradleTemplate.process(sketchView, out);
    }


    private static Configuration buildFtlConfiguration() {
        final Configuration ftlConfig = new Configuration(Configuration.VERSION_2_3_23);
        final String templatePackagePath = Sumi.class.getPackage().getName().replace(".", "/");
        ftlConfig.setClassLoaderForTemplateLoading(
                Sumi.class.getClassLoader(),
                templatePackagePath
        );
        ftlConfig.setDefaultEncoding("UTF-8");
        ftlConfig.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
        ftlConfig.setLogTemplateExceptions(false);
        return ftlConfig;
    }

    public class SketchView {

        private final String sketchName;
        private final String sketchDirectory;

        public SketchView(String sketchName, String sketchDirectory) {
            this.sketchName = sketchName;
            this.sketchDirectory = sketchDirectory;
        }

        public String getSketchName() {
            return sketchName;
        }

        public String getSketchDirectory() {
            return sketchDirectory;
        }

        public String getSrcDirectory() {
            return sketchDirectory + "/src";
        }

        public String getJavaSketchFileName() {
            return sketchName + ".java";
        }

        public String getJavaSketchPath() {
            return getSrcDirectory() + "/" + getJavaSketchFileName();
        }

        public String getJavaMainPath() {
            return getSrcDirectory() + "/Main.java";
        }

        public String getDataDirectory() {
            return getSketchDirectory() + "/data";
        }

        public String getGradleBuildPath() {
            return sketchDirectory + "/build.gradle";
        }

        public String getRunScriptPath() {
            return sketchDirectory + "/run";
        }

        public String getLibDir() {
            return sketchDirectory + "/lib";
        }
    }
}
