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
import java.util.ArrayList;
import java.util.List;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.ExpectedException;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.tapley.jenkins.maven.dependency.plugin.model.JenkinsClient;

/**
 *
 * @author Chris
 */
public class TestMojoBase {
    
    @Rule
    public final ExpectedException expectedException = ExpectedException.none();
    
    @Mock
    JenkinsClient jenkinsClient;
    
    @Mock
    File destination;
    
    @Mock
    ArtifactItem artifactItem;
    
    List<String> matchingArtifactUrls;
    
    @Before
    public void initBase() {
        MockitoAnnotations.initMocks(this);
        
        matchingArtifactUrls = new ArrayList<>();
        matchingArtifactUrls.add("http://download.com/file.zip");
    }
}
