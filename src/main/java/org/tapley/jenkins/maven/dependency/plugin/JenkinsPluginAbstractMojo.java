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
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
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
    String buildArtifact;

    @Parameter(required = true)
    String outputDirectory;
    
    @Parameter(defaultValue = "${project}", readonly = true, required = true)
    MavenProject project;
    
    JenkinsClient getJenkinsClient() {
        return new JenkinsClient(jenkinsUrl, jobName, buildNumber);
    }
    
    protected File getOutputDirectoryFullPath() {
        return new File(project.getBasedir(), outputDirectory);
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
