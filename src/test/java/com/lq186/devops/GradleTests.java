package com.lq186.devops;

import com.lq186.devops.model.CommandResult;
import com.lq186.devops.util.GradleUtils;
import org.junit.Test;

/**
 * @author lq
 * @date 2020/1/20
 */
public class GradleTests {

    @Test
    public void testGradle() {
        CommandResult<Void> result = GradleUtils.doTask(GitTests.localPath, null, "tasks");
        assert result.isSuccess();
    }

}
