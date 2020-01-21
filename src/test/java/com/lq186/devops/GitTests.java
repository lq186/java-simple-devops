package com.lq186.devops;

import com.lq186.devops.model.CommandResult;
import com.lq186.devops.util.GitUtils;
import org.junit.Test;

import java.io.File;

/**
 * @author lq
 * @date 2020/1/19
 */
public class GitTests {

    //static final String repositoryUrl = "https://github.com/lq186/java-simple-devops.git";
    static final String repositoryUrl = "https://github.com/lq186/algorithm.git";

    static final String localPath = "D:\\temp\\devops\\git\\";

    final String username = "liuqing";

    final String password = "liuqing4public";

    @Test
    public void testClone() {
        CommandResult<Void> result = GitUtils.clone(repositoryUrl, localPath);
        assert result.isSuccess();
    }

    @Test
    public void testDelete() {
        GitUtils.deleteDirectory(new File(localPath));
    }

}
