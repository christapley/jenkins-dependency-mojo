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

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugins.annotations.Parameter;

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
}