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
import java.util.List;
import org.apache.commons.io.FilenameUtils;
import org.apache.maven.plugins.annotations.Mojo;
import org.tapley.jenkins.maven.dependency.plugin.model.JenkinsClient;

/**
 *
 * @author ctapley
 */
@Mojo( name = "copy", requiresProject = false, threadSafe = true )
public class CopyMojo extends JenkinsPluginAbstractMojo {

    protected String getFileNameFromUrl(String url) {
        return FilenameUtils.getName(url);
    }

    @Override
    protected void executeForArtifactItem(ArtifactItem artifactItem) throws Exception {
        
        JenkinsClient jenkinsClient = getJenkinsClient(artifactItem);
        File destination = ensureOutputDirectoryExists(artifactItem.getOutputDirectory());
        List<String> matchingArtifactUrls = jenkinsClient.getMatchingArtifactUrls(artifactItem.getBuildArtifact());
            
        getLog().info(String.format("Copying %s from job %s with build %s from %s", artifactItem.getBuildArtifact(), artifactItem.getJobName(), artifactItem.getBuildNumber(), artifactItem.getJenkinsUrl()));

        for (String url : matchingArtifactUrls) {
            getLog().info(String.format("Processing detected artifact url %s", url));
            File outputFile = new File(destination, getFileNameFromUrl(url));
                
            jenkinsClient.downloadArtifact(url, outputFile);
        }
    }
}
