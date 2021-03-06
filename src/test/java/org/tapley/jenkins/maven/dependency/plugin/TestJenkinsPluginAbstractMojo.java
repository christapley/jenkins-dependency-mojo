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
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import org.apache.maven.plugin.MojoExecutionException;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.ExpectedException;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
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
    String expectedBuildArtifact = "expectedBuildArtifact";
    
    private class JenkinsPluginAbstractMojoInternal extends JenkinsPluginAbstractMojo {
       
        @Override
        protected void executeForArtifactItem(ArtifactItem artifactItem) throws Exception {
            
        }
        
    }
    
    JenkinsPluginAbstractMojoInternal mojo;
    JenkinsPluginAbstractMojoInternal mojoSpy;
    
    @Before
    public void init() {
        mojo = new JenkinsPluginAbstractMojoInternal();
        mojoSpy = spy(mojo);
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
        assertEquals(new File(expectedOutputDirectory).getAbsolutePath(), mojo.getOutputDirectoryFullPath(expectedOutputDirectory).getAbsolutePath());
    }
    
    @Test
    public void ensureOutputDirectoryExists_cannotCreate() {
        File baseDir = mock(File.class);
        doReturn(baseDir).when(mojoSpy).getOutputDirectoryFullPath(expectedOutputDirectory);
        doReturn(false).when(baseDir).exists();
        
        expectedException.expect(IllegalStateException.class);
        
        mojoSpy.ensureOutputDirectoryExists(expectedOutputDirectory);
    }
    
    @Test
    public void ensureOutputDirectoryExists_alreadyExists() {
        File baseDir = mock(File.class);
        doReturn(baseDir).when(mojoSpy).getOutputDirectoryFullPath(expectedOutputDirectory);
        doReturn(true).when(baseDir).exists();
        mojoSpy.ensureOutputDirectoryExists(expectedOutputDirectory);
        
        verify(baseDir, times(1)).exists();
    }
    
    @Test
    public void ensureOutputDirectoryExists_creates() {
        File baseDir = mock(File.class);
        JenkinsPluginAbstractMojoInternal mojoSpy = spy(mojo);
        doReturn(baseDir).when(mojoSpy).getOutputDirectoryFullPath(expectedOutputDirectory);
        doReturn(true).when(baseDir).exists();
        mojoSpy.ensureOutputDirectoryExists(expectedOutputDirectory);
        
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
        artifactItem.setBuildArtifact(expectedBuildArtifact);
        
        mojo.fillInMissingArtifactItemFieldsFromDefaults(artifactItem);
        
        assertEquals(expectedJenkinsUrl, artifactItem.getJenkinsUrl());
        assertEquals(expectedJobName, artifactItem.getJobName());
        assertEquals(expectedBuildNumber, artifactItem.getBuildNumber());
        assertEquals(expectedOutputDirectory, artifactItem.getOutputDirectory());
        assertEquals(expectedBuildArtifact, artifactItem.getBuildArtifact());
    }

    @Test    
    public void execute_throws() throws Exception {
        expectedException.expect(MojoExecutionException.class);
        ArtifactItem artifactItem = mock(ArtifactItem.class);
        List<ArtifactItem> artifactItems = Arrays.asList(artifactItem);
        ReflectionTestUtils.setField(mojoSpy, "artifactItems", artifactItems);
        doThrow(new IOException("bang!")).when(mojoSpy).executeForArtifactItem(artifactItem);
        mojoSpy.execute();
        verify(mojoSpy, times(1)).executeForArtifactItem(artifactItem);
    }
    
    @Test    
    public void execute_ok() throws Exception {
        ArtifactItem artifactItem = mock(ArtifactItem.class);
        List<ArtifactItem> artifactItems = Arrays.asList(artifactItem);
        ReflectionTestUtils.setField(mojoSpy, "artifactItems", artifactItems);
        doNothing().when(mojoSpy).executeForArtifactItem(artifactItem);
        mojoSpy.execute();
        verify(mojoSpy, times(1)).executeForArtifactItem(artifactItem);
    }
    
    @Test
    public void selectFirstIfNotBlank_firstIsBlank() {
        String second = "expectedSecond";
        assertEquals(second, mojo.selectFirstIfNotBlank(null, second));
        assertEquals(second, mojo.selectFirstIfNotBlank("", second));
        assertEquals(second, mojo.selectFirstIfNotBlank("   ", second));
        assertEquals(second, mojo.selectFirstIfNotBlank("\t", second));
    }
    
    @Test
    public void selectFirstIfNotBlank_firstIsNotBlank() {
        String first = "expectedFirst";
        String second = "notExpectedSecond";
        assertEquals(first, mojo.selectFirstIfNotBlank(first, null));
        assertEquals(first, mojo.selectFirstIfNotBlank(first, ""));
        assertEquals(first, mojo.selectFirstIfNotBlank(first, "   "));
        assertEquals(first, mojo.selectFirstIfNotBlank(first, "\t"));
        assertEquals(first, mojo.selectFirstIfNotBlank(first, second));
    }
}
