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

import java.io.File;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.ExpectedException;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import org.springframework.test.util.ReflectionTestUtils;
import org.tapley.jenkins.maven.dependency.plugin.model.JenkinsClient;

/**
 *
 * @author ctapley
 */
public class TestJenkinsPluginAbstractMojo {

    @Rule
    public final ExpectedException expectedException = ExpectedException.none();
    
    String expectedJenkinsUrl = "expectedJenkinsUrl";
    String expectedJobName = "expectedJobName";
    String expectedBuildNumber = "expectedBuildNumber";
    String expectedOutputDirectory = "expectedOutputDirectory";
    
    private class JenkinsPluginAbstractMojoInternal extends JenkinsPluginAbstractMojo {
       
        @Override
        protected void executeForArtifactItem(ArtifactItem artifactItem) throws Exception {
            
        }
        
    }
    
    JenkinsPluginAbstractMojoInternal mojo;
    
    @Before
    public void init() {
        mojo = new JenkinsPluginAbstractMojoInternal();
    }
    
    @Test
    public void getJenkinsClient() {
        ArtifactItem artifactItem = mock(ArtifactItem.class);
        doReturn(expectedJenkinsUrl).when(artifactItem).getJenkinsUrl();
        doReturn(expectedJobName).when(artifactItem).getJobName();
        doReturn(expectedBuildNumber).when(artifactItem).getBuildNumber();
        
        JenkinsClient client = mojo.getJenkinsClient(artifactItem);
        assertNotNull(client);
        assertEquals(expectedJenkinsUrl, ReflectionTestUtils.getField(client, "jenkinsUrl"));
        assertEquals(expectedJobName, ReflectionTestUtils.getField(client, "jobName"));
        assertEquals(expectedBuildNumber, ReflectionTestUtils.getField(client, "buildNumber"));
    }
    
    @Test
    public void getOutputDirectoryFullPath() {
        ReflectionTestUtils.setField(mojo, "outputDirectory", expectedOutputDirectory);
        assertEquals(new File(expectedOutputDirectory).getAbsolutePath(), mojo.getOutputDirectoryFullPath().getAbsolutePath());
    }
    
    @Test
    public void ensureOutputDirectoryExists_cannotCreate() {
        File baseDir = mock(File.class);
        JenkinsPluginAbstractMojoInternal mojoSpy = spy(mojo);
        doReturn(baseDir).when(mojoSpy).getOutputDirectoryFullPath();
        doReturn(false).when(baseDir).exists();
        
        expectedException.expect(IllegalStateException.class);
        
        mojoSpy.ensureOutputDirectoryExists();
    }
    
    @Test
    public void ensureOutputDirectoryExists_alreadyExists() {
        File baseDir = mock(File.class);
        JenkinsPluginAbstractMojoInternal mojoSpy = spy(mojo);
        doReturn(baseDir).when(mojoSpy).getOutputDirectoryFullPath();
        doReturn(true).when(baseDir).exists();
        mojoSpy.ensureOutputDirectoryExists();
        
        verify(baseDir, times(1)).exists();
    }
    
    @Test
    public void ensureOutputDirectoryExists_creates() {
        File baseDir = mock(File.class);
        JenkinsPluginAbstractMojoInternal mojoSpy = spy(mojo);
        doReturn(baseDir).when(mojoSpy).getOutputDirectoryFullPath();
        doReturn(true).when(baseDir).exists();
        mojoSpy.ensureOutputDirectoryExists();
        
        verify(baseDir, times(1)).exists();
        verify(baseDir, times(1)).mkdirs();
    }
    
    @Test
    public void fillInMissingArtifactItemFieldsFromDefaults_nullarg() {
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage("artifactItem");
        
        mojo.fillInMissingArtifactItemFieldsFromDefaults(null);
    }

    @Test
    public void fillInMissingArtifactItemFieldsFromDefaults_ok() {
        ArtifactItem artifactItem = new ArtifactItem();
        ReflectionTestUtils.setField(mojo, "jenkinsUrl", expectedJenkinsUrl);
        ReflectionTestUtils.setField(mojo, "jobName", expectedJobName);
        ReflectionTestUtils.setField(mojo, "buildNumber", expectedBuildNumber);
        ReflectionTestUtils.setField(mojo, "outputDirectory", expectedOutputDirectory);
        mojo.fillInMissingArtifactItemFieldsFromDefaults(artifactItem);
        
        assertEquals(expectedJenkinsUrl, artifactItem.getJenkinsUrl());
        assertEquals(expectedJobName, artifactItem.getJobName());
        assertEquals(expectedBuildNumber, artifactItem.getBuildNumber());
        assertEquals(expectedOutputDirectory, artifactItem.getOutputDirectory());
    }    
}
