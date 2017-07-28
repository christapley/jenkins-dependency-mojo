/*
 * Copyright 2017 ctapley.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.tapley.jenkins.maven.dependency.plugin;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Parameter;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;
import org.springframework.test.util.ReflectionTestUtils;
import org.tapley.jenkins.maven.dependency.plugin.model.JenkinsClient;

/**
 *
 * @author ctapley
 */
public class TestJenkinsPluginAbstractMojo {

    String expectedJenkinsUrl = "expectedJenkinsUrl";
    String expectedJobName = "expectedJobName";
    String expectedBuildNumber = "expectedBuildNumber";
    
    private class JenkinsPluginAbstractMojoInternal extends JenkinsPluginAbstractMojo {
        
        @Override
        public void execute() throws MojoExecutionException, MojoFailureException {
            
        }
        
    }
    
    JenkinsPluginAbstractMojoInternal mojo;
    
    @Before
    public void init() {
        mojo = new JenkinsPluginAbstractMojoInternal();
    }
    
    @Test
    public void getJenkinsClient() {
        ReflectionTestUtils.setField(mojo, "jenkinsUrl", expectedJenkinsUrl);
        ReflectionTestUtils.setField(mojo, "jobName", expectedJobName);
        ReflectionTestUtils.setField(mojo, "buildNumber", expectedBuildNumber);
        
        JenkinsClient client = mojo.getJenkinsClient();
        assertNotNull(client);
        assertEquals(expectedJenkinsUrl, ReflectionTestUtils.getField(client, "jenkinsUrl"));
        assertEquals(expectedJobName, ReflectionTestUtils.getField(client, "jobName"));
        assertEquals(expectedBuildNumber, ReflectionTestUtils.getField(client, "buildNumber"));
    }
}
