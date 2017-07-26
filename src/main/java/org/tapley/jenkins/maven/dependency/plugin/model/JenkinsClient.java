/*
 * Copyright 2017 Chris Tapley.
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
package org.tapley.jenkins.maven.dependency.plugin.model;

import java.io.File;
import java.util.List;

/**
 *
 * @author Chris Tapley
 */
public class JenkinsClient {
    
    String jenkinsUrl;
    String jobName;
    String buildNumber;
    
    public JenkinsClient(String jenkinsUrl, String jobName, String buildNumber) {
        this.jenkinsUrl = jenkinsUrl;
        this.jobName = jobName;
        this.buildNumber = buildNumber;
    }
    
    public List<String> getMatchingArtifactUrls(String artifactNameMatcher) {
        return null;
    }
    
    public void downloadArtifact(String artifactUrl, File outputFile) {
        
    }
        
}
