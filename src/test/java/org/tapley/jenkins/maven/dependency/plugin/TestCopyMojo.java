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
import java.util.ArrayList;
import java.util.List;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.ExpectedException;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import org.mockito.Mock;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.spy;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;
import org.tapley.jenkins.maven.dependency.plugin.model.JenkinsClient;

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
        
        doReturn(jenkinsClient).when(mojoSpy).getJenkinsClient();
    }
    
    @Test
    public void getFileNameFromUrl() {
        String actual = mojo.getFileNameFromUrl("http://brewery.ingrnet.com/jobs/tests/artifact/path/to/file.zip");
        assertEquals("file.zip", actual);
    }
    
    @Test
    public void execute_getMatchingArtifactUrlsThrows() throws MojoExecutionException, MojoFailureException, IOException {
        expectedException.expect(MojoExecutionException.class);
        doThrow(new IOException("Bang!")).when(jenkinsClient).getMatchingArtifactUrls(anyString());
        mojoSpy.execute();
    }
    
    @Test
    public void execute_ensureOutputDirectoryExistsThrows() throws MojoExecutionException, MojoFailureException, IOException {
        expectedException.expect(MojoExecutionException.class);
        doReturn(matchingArtifactUrls).when(jenkinsClient).getMatchingArtifactUrls(anyString());
        doThrow(new IllegalStateException("Bang!")).when(mojoSpy).ensureOutputDirectoryExists();
        mojoSpy.execute();
    }
    
    @Test
    public void execute_downloadArtifactThrows() throws MojoExecutionException, MojoFailureException, IOException {
        expectedException.expect(MojoExecutionException.class);
        doReturn(matchingArtifactUrls).when(jenkinsClient).getMatchingArtifactUrls(anyString());
        doReturn(destination).when(mojoSpy).ensureOutputDirectoryExists();
        doReturn("file.zip").when(mojoSpy).getFileNameFromUrl(anyString());
        doThrow(new IOException("Bang!")).when(jenkinsClient).downloadArtifact(any(), any());
        mojoSpy.execute();
    }
    
    @Test
    public void execute_ok() throws MojoExecutionException, MojoFailureException, IOException {
        expectedException.expect(MojoExecutionException.class);
        String buildArtifact = "buildArtifact";
        
        ReflectionTestUtils.setField(mojoSpy, "buildArtifact", buildArtifact);
        
        doReturn(matchingArtifactUrls).when(jenkinsClient).getMatchingArtifactUrls(buildArtifact);
        doReturn(destination).when(mojoSpy).ensureOutputDirectoryExists();
        doReturn("file.zip").when(mojoSpy).getFileNameFromUrl(matchingArtifactUrls.get(0));
        doNothing().when(jenkinsClient).downloadArtifact(any(), any());
        mojoSpy.execute();
    }
}
