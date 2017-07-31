/*
 * Copyright 2017 Chris.
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
import org.codehaus.plexus.archiver.UnArchiver;
import org.codehaus.plexus.archiver.manager.ArchiverManager;
import org.codehaus.plexus.archiver.manager.DefaultArchiverManager;
import org.codehaus.plexus.archiver.manager.NoSuchArchiverException;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import org.mockito.Mock;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import org.springframework.test.util.ReflectionTestUtils;

/**
 *
 * @author Chris
 */
public class TestUnpackMojo extends TestMojoBase {

    UnpackMojo mojo;
    UnpackMojo mojoSpy;
    
    @Mock
    ArchiverManager archiverManager;
    
    @Before
    public void init() {
        mojo = new UnpackMojo();
        mojoSpy = spy(mojo);
        
        doReturn(jenkinsClient).when(mojoSpy).getJenkinsClient(any());
    }
    
    @Test
    public void getFileExtension() throws IOException {
        assertEquals("zip", mojo.getFileExtension("http://host.com/path/file.zip"));
        assertEquals("", mojo.getFileExtension("http://host.com/path/file"));
    }
    
    @Test
    public void getTemporaryFileWithExtension() throws IOException {
        File actual = mojo.getTemporaryFileWithExtension("zip");
        assertTrue(actual.getAbsolutePath().endsWith(".zip"));
    }
    
    @Test
    public void unpack() throws NoSuchArchiverException {
        File archive = mock(File.class);
        UnArchiver unArchiver = mock(UnArchiver.class);
        String expectedIncludes = "expectedIncludes";
        String expectedExcludes = "expectedExcludes";
        
        ReflectionTestUtils.setField(mojoSpy, "includes", expectedIncludes);
        ReflectionTestUtils.setField(mojoSpy, "excludes", expectedExcludes);
        ReflectionTestUtils.setField(mojoSpy, "archiverManager", archiverManager);
        
        doReturn(destination).when(mojoSpy).ensureOutputDirectoryExists();
        doReturn(unArchiver).when(archiverManager).getUnArchiver(archive);
                
        mojoSpy.unpack(archive);
        
        verify(unArchiver, times(1)).setSourceFile(archive);
        verify(unArchiver, times(1)).setDestDirectory(destination);
        verify(unArchiver, times(1)).setFileSelectors(any());
        verify(unArchiver, times(1)).extract();
    }
   
    @Test
    public void executeForArtifactItem_getMatchingArtifactUrlsThrows() throws Exception {
        expectedException.expect(IOException.class);
        doThrow(new IOException("Bang!")).when(jenkinsClient).getMatchingArtifactUrls(anyString());
        mojoSpy.executeForArtifactItem(artifactItem);
    }
    
    @Test
    public void executeForArtifactItem_ensureOutputDirectoryExistsThrows() throws Exception {
        expectedException.expect(IllegalStateException.class);
        doReturn(matchingArtifactUrls).when(jenkinsClient).getMatchingArtifactUrls(anyString());
        doThrow(new IllegalStateException("Bang!")).when(mojoSpy).ensureOutputDirectoryExists();
        mojoSpy.executeForArtifactItem(artifactItem);
    }
    
    @Test
    public void executeForArtifactItem_downloadArtifactThrows() throws Exception {
        expectedException.expect(IOException.class);
        doReturn(matchingArtifactUrls).when(jenkinsClient).getMatchingArtifactUrls(anyString());
        doReturn(destination).when(mojoSpy).ensureOutputDirectoryExists();
        doThrow(new IOException("Bang!")).when(jenkinsClient).downloadArtifact(any(), any());
        mojoSpy.executeForArtifactItem(artifactItem);
    }
    
    @Test
    public void executeForArtifactItem_ok() throws Exception {
        String buildArtifact = "buildArtifact";
        String destinationPath = "destinationPath";
        destination = new File(destinationPath);
        
        doReturn(buildArtifact).when(artifactItem).getBuildArtifact();
        
        doReturn(matchingArtifactUrls).when(jenkinsClient).getMatchingArtifactUrls(buildArtifact);
        doReturn(destination).when(mojoSpy).getTemporaryFileWithExtension(any());
        doNothing().when(mojoSpy).unpack(any());
        
        doNothing().when(jenkinsClient).downloadArtifact(matchingArtifactUrls.get(0), destination);
        mojoSpy.executeForArtifactItem(artifactItem);
        
        verify(jenkinsClient, times(1)).downloadArtifact(matchingArtifactUrls.get(0), destination);
        verify(mojoSpy, times(1)).unpack(any());
    }
}
