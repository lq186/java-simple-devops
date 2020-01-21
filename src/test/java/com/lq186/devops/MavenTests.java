package com.lq186.devops;

import org.apache.maven.cli.MavenCli;
import org.junit.Test;

import java.io.*;
import java.nio.charset.StandardCharsets;

/**
 * @author lq
 * @date 2020/1/20
 */
public class MavenTests {

    @Test
    public void testBuild() throws IOException {
        MavenCli mavenCli = new MavenCli();
        System.setProperty(MavenCli.LOCAL_REPO_PROPERTY, "D:\\temp\\devops\\mavenRepo\\");
        System.setProperty(MavenCli.MULTIMODULE_PROJECT_DIRECTORY, "D:\\temp\\devops\\maven\\");
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(byteArrayOutputStream);
        mavenCli.doMain(new String[]{"package", "-am"}, GitTests.localPath, printStream, printStream);
        System.out.println(new String(byteArrayOutputStream.toByteArray(), StandardCharsets.UTF_8));
    }

}
