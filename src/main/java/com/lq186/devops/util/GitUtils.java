package com.lq186.devops.util;

import com.lq186.devops.model.CommandResult;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;

/**
 * @author lq
 * @date 2020/1/19
 */
public final class GitUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(GitUtils.class);

    /**
     * 克隆资源库
     *
     * @param repositoryUrl 资源库地址
     * @param localPath     本地路径
     * @throws Exception
     */
    public static CommandResult<Void> clone(String repositoryUrl, String localPath) {
        LOGGER.info("[clone] repositoryUrl: {}, localPath: {}", repositoryUrl, localPath);
        try {
            File localDirectory = new File(localPath);
            cleanup(localDirectory);
            Git.cloneRepository()
                    .setURI(repositoryUrl)
                    .setDirectory(localDirectory)
                    .setCloneAllBranches(true)
                    .setCloneSubmodules(true)
                    .call();
            return CommandResult.success();
        } catch (Exception e) {
            LOGGER.error("[clone] repositoryUrl: {}, localPath: {}", repositoryUrl, localPath, e);
            return CommandResult.failed(e.getMessage());
        }
    }

    /**
     * 克隆资源库
     *
     * @param repositoryUrl 资源库地址
     * @param localPath     本地路径
     * @param username      资源库用户名
     * @param password      资源库密码
     * @throws Exception
     */
    public static CommandResult<Void> clone(String repositoryUrl, String localPath,
                                            String username, String password) {
        LOGGER.info("[clone] repositoryUrl: {}, localPath: {}", repositoryUrl, localPath);
        try {
            File localDirectory = new File(localPath);
            cleanup(localDirectory);
            Git.cloneRepository()
                    .setURI(repositoryUrl)
                    .setDirectory(localDirectory)
                    .setCloneAllBranches(true)
                    .setCredentialsProvider(new UsernamePasswordCredentialsProvider(username, password))
                    .call();
            return CommandResult.success();
        } catch (Exception e) {
            LOGGER.error("[clone] repositoryUrl: {}, localPath: {}", repositoryUrl, localPath, e);
            return CommandResult.failed(e.getMessage());
        }
    }

    private static void cleanup(File directory) {
        if (directory != null) {
            LOGGER.info("[cleanup] directory absolute path: {}", directory.getAbsolutePath());
            deleteDirectory(directory);
        }
    }

    public static void deleteDirectory(File directory) {
        if (directory == null) {
            LOGGER.error("[deleteDirectory] directory is null.");
            throw new NullPointerException("directory");
        }
        if (directory.isDirectory()) {
            File[] files = directory.listFiles();
            if (files != null || files.length > 0) {
                for (int i = 0; i < files.length; ++i) {
                    deleteDirectory(files[i]);
                }
            }
            LOGGER.info("[deleteDirectory] directory: {}", directory.getAbsolutePath());
            directory.delete();
        } else {
            LOGGER.info("[deleteDirectory] file: {}", directory.getAbsolutePath());
            directory.delete();
        }
    }

}
