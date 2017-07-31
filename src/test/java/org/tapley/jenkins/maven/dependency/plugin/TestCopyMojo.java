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
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import org.springframework.test.util.ReflectionTestUtils;

/**
 *
 * @author ctapley
 */
public class TestCopyMojo extends TestMojoBase {
    
    CopyMojo mojo;
    CopyMojo mojoSpy;
    
    @Before
    public void init() {
        mojo = new CopyMojo();
        mojoSpy = spy(mojo);
        
        doReturn(jenkinsClient).when(mojoSpy).getJenkinsClient(any());
    }
    
    @Test
    public void getFileNameFromUrl() {
        String actual = mojo.getFileNameFromUrl("http://brewery.ingrnet.com/jobs/tests/artifact/path/to/file.zip");
        assertEquals("file.zip", actual);
    }
    
    @Test
    public void executeForArtifactItem_getMatchingArtifactUrlsThrows() throws Exception {
        expectedException.expect(IOException.class);
        
        String destinationPath = "destinationPath";
        destination = new File(destinationPath);
        
        doReturn(destination).when(mojoSpy).ensureOutputDirectoryExists();
        doThrow(new IOException("Bang!")).when(jenkinsClient).getMatchingArtifactUrls(any());
        mojoSpy.executeForArtifactItem(artifactItem);
    }
    
    @Test
    public void executeForArtifactItem_ensureOutputDirectoryExistsThrows() throws Exception {
        expectedException.expect(IllegalStateException.class);
        doReturn(matchingArtifactUrls).when(jenkinsClient).getMatchingArtifactUrls(any());
        doThrow(new IllegalStateException("Bang!")).when(mojoSpy).ensureOutputDirectoryExists();
        mojoSpy.executeForArtifactItem(artifactItem);
    }
    
    @Test
    public void executeForArtifactItem_downloadArtifactThrows() throws Exception {
        expectedException.expect(IOException.class);
        String destinationPath = "destinationPath";
        destination = new File(destinationPath);
        
        doReturn(matchingArtifactUrls).when(jenkinsClient).getMatchingArtifactUrls(anyString());
        doReturn(destination).when(mojoSpy).ensureOutputDirectoryExists();
        doReturn("file.zip").when(mojoSpy).getFileNameFromUrl(anyString());
        doThrow(new IOException("Bang!")).when(jenkinsClient).downloadArtifact(any(), any());
        mojoSpy.executeForArtifactItem(artifactItem);
    }
    
    @Test
    public void executeForArtifactItem_ok() throws Exception {
        String expectedFileName = "file.zip";
        String buildArtifact = "buildArtifact";
        String destinationPath = "destinationPath";
        destination = new File(destinationPath);
        
        doReturn(buildArtifact).when(artifactItem).getBuildArtifact();
        
        doReturn(matchingArtifactUrls).when(jenkinsClient).getMatchingArtifactUrls(buildArtifact);
        doReturn(destination).when(mojoSpy).ensureOutputDirectoryExists();
        doReturn(expectedFileName).when(mojoSpy).getFileNameFromUrl(matchingArtifactUrls.get(0));
        doNothing().when(jenkinsClient).downloadArtifact(matchingArtifactUrls.get(0), new File(destination, expectedFileName));
        mojoSpy.executeForArtifactItem(artifactItem);
        
        verify(jenkinsClient, times(1)).downloadArtifact(matchingArtifactUrls.get(0), new File(destination, expectedFileName));
    }
}
