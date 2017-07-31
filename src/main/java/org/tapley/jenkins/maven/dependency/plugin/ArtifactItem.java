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

/**
 *
 * @author ctapley
 */
public class ArtifactItem {
    String jenkinsUrl;
    String jobName;
    String buildNumber;
    String buildArtifact;
    String outputDirectory;

    /**
     * @return the jenkinsUrl
     */
    public String getJenkinsUrl() {
        return jenkinsUrl;
    }

    /**
     * @param jenkinsUrl the jenkinsUrl to set
     */
    public void setJenkinsUrl(String jenkinsUrl) {
        this.jenkinsUrl = jenkinsUrl;
    }

    /**
     * @return the jobName
     */
    public String getJobName() {
        return jobName;
    }

    /**
     * @param jobName the jobName to set
     */
    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    /**
     * @return the buildNumber
     */
    public String getBuildNumber() {
        return buildNumber;
    }

    /**
     * @param buildNumber the buildNumber to set
     */
    public void setBuildNumber(String buildNumber) {
        this.buildNumber = buildNumber;
    }

    /**
     * @return the buildArtifact
     */
    public String getBuildArtifact() {
        return buildArtifact;
    }

    /**
     * @param buildArtifact the buildArtifact to set
     */
    public void setBuildArtifact(String buildArtifact) {
        this.buildArtifact = buildArtifact;
    }

    /**
     * @return the outputDirectory
     */
    public String getOutputDirectory() {
        return outputDirectory;
    }

    /**
     * @param outputDirectory the outputDirectory to set
     */
    public void setOutputDirectory(String outputDirectory) {
        this.outputDirectory = outputDirectory;
    }
}
