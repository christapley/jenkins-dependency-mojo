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
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.util.StringUtils;
import org.tapley.jenkins.maven.dependency.plugin.model.JenkinsClient;

/**
 *
 * @author ctapley
 */
public abstract class JenkinsPluginAbstractMojo extends AbstractMojo {

    @Parameter(required = true)
    String jenkinsUrl;

    @Parameter(required = true)
    String jobName;

    @Parameter(defaultValue = "lastSuccessfulBuild")
    String buildNumber;

    @Parameter(required = true)
    String outputDirectory;
    
    @Parameter(required = true)
    List<ArtifactItem> artifactItems;
    
    @Parameter(defaultValue = "${project}", readonly = true, required = true)
    MavenProject project;
    
    JenkinsClient getJenkinsClient(ArtifactItem artifactItem) {
        return new JenkinsClient(artifactItem.getJenkinsUrl(), artifactItem.getJobName(), artifactItem.getBuildNumber());
    }
    
    protected File getOutputDirectoryFullPath() {
        return new File(outputDirectory);
    }
    
    protected String selectFirstIfNotBlank(String first, String second) {
        if(StringUtils.isNotBlank(first)) { 
            return first;
        } else {
            return second;
        }
    }
    
    protected void fillInMissingArtifactItemFieldsFromDefaults(ArtifactItem artifactItem) {
        if (artifactItem == null) {
            throw new IllegalArgumentException("artifactItem cannot be null");
        }
        artifactItem.setJenkinsUrl(selectFirstIfNotBlank(artifactItem.getJenkinsUrl(), jenkinsUrl));
        artifactItem.setJobName(selectFirstIfNotBlank(artifactItem.getJobName(), jobName));
        artifactItem.setBuildNumber(selectFirstIfNotBlank(artifactItem.getBuildNumber(), buildNumber));
        artifactItem.setOutputDirectory(selectFirstIfNotBlank(artifactItem.getOutputDirectory(), outputDirectory));
    }
    
    protected abstract void executeForArtifactItem(ArtifactItem artifactItem) throws Exception;
    
    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        try {
            for(ArtifactItem artifactItem : artifactItems) {
                fillInMissingArtifactItemFieldsFromDefaults(artifactItem);
                executeForArtifactItem(artifactItem);
            }
        } catch (Exception ex) {
            throw new MojoExecutionException("Failed to process artifacts", ex);
        }
    }
    
    protected File ensureOutputDirectoryExists() {
        File destination = getOutputDirectoryFullPath();
        destination.mkdirs();
        if(!destination.exists()) {
            throw new IllegalStateException(String.format("Output directory '%s' cannot be created", destination.getAbsolutePath()));
        }
        return destination;
    }
}
