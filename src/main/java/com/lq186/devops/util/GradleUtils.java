package com.lq186.devops.util;

import com.lq186.devops.model.CommandResult;
import org.gradle.tooling.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.io.File;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.stream.Collectors;

/**
 * @author lq
 * @date 2020/1/20
 */
public final class GradleUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(GradleUtils.class);

    public static CommandResult<Void> doTask(String rootProjectDir, String subProjectName, String... tasks) {
        LOGGER.info("[doTask] rootProjectDir: {}, subProjectName: {}, tasks: {}",
                rootProjectDir, subProjectName, tasks);
        if (StringUtils.isEmpty(rootProjectDir)) {
            LOGGER.error("[doTask] rootProjectDir is empty.");
            return CommandResult.failed("rootProjectDir is empty.");
        }
        final File rootProjectDirectory = new File(rootProjectDir);
        if (!rootProjectDirectory.exists() || !rootProjectDirectory.isDirectory()) {
            LOGGER.error("[doTask] root project directory: {} not exists.", rootProjectDir);
            return CommandResult.failed("root project directory not exists.");
        }
        if (tasks == null || tasks.length == 0) {
            LOGGER.error("[doTask] no tasks.");
            return CommandResult.failed("no tasks.");
        }
        final List<String> arguments = Arrays.asList("-xTest");
        final Map<String, String> jvmDefines = new LinkedHashMap<>();
        if (!StringUtils.isEmpty(subProjectName)) {
            for (int i = 0; i < tasks.length; ++i) {
                tasks[i] = subProjectName + ":" + tasks[i];
            }
        }
        return doTask(rootProjectDirectory, arguments, jvmDefines, tasks);
    }

    private static CommandResult<Void> doTask(File rootProjectDirectory, List<String> arguments,
                                              Map<String, String> jvmDefines, String... execTasks) {
        try (ProjectConnection projectConnection = getProjectConnection(rootProjectDirectory)) {
            BuildLauncher launcher = projectConnection.newBuild().forTasks(execTasks).withArguments(arguments);
            List<String> jvmArgs = jvmDefines.entrySet().stream()
                    .map(it -> "-D" + it.getKey() + "=" + it.getValue())
                    .collect(Collectors.toList());
            launcher.setJvmArguments(jvmArgs);
            launcher.addProgressListener((ProgressListener) event -> {
                LOGGER.info("[doTask] status changed: {}", event.getDescription());
            });
            final CountDownLatch latch = new CountDownLatch(1);
            final CommandResult<Void> commandResult = new CommandResult<>();
            launcher.run(new ResultHandler<Void>() {
                @Override
                public void onComplete(Void result) {
                    LOGGER.error("[doTask] success. rootProjectDirectory: {}, arguments: {}, jvmDefines: {}, tasks: {}",
                            rootProjectDirectory.getAbsolutePath(), arguments, jvmDefines, execTasks);
                    commandResult.setSuccess(true);
                    latch.countDown();
                }

                @Override
                public void onFailure(GradleConnectionException failure) {
                    LOGGER.error("[doTask] failed. rootProjectDirectory: {}, arguments: {}, jvmDefines: {}, tasks: {}",
                            rootProjectDirectory.getAbsolutePath(), arguments, jvmDefines, execTasks, failure);
                    commandResult.setSuccess(false).setMessage(failure.getMessage());
                    latch.countDown();
                }
            });
            latch.await();
            return commandResult;
        } catch (Exception e) {
            LOGGER.error("[doTask] do task error. rootProjectDirectory: {}, arguments: {}, jvmDefines: {}, tasks: {}",
                    rootProjectDirectory.getAbsolutePath(), arguments, jvmDefines, execTasks, e);
            return CommandResult.failed(e.getMessage());
        }
    }

    private static ProjectConnection getProjectConnection(File rootProjectDirectory) {
        return GradleConnector.newConnector()
                .forProjectDirectory(rootProjectDirectory)
                .useGradleVersion("5.5")
                .connect();
    }


}
