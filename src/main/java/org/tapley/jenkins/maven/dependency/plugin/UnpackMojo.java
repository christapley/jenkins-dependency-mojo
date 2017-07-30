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
package org.tapley.jenkins.maven.dependency.plugin;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;
import org.apache.commons.io.FilenameUtils;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.tapley.jenkins.maven.dependency.plugin.model.JenkinsClient;
import org.codehaus.plexus.archiver.UnArchiver;
import org.codehaus.plexus.archiver.manager.ArchiverManager;
import org.codehaus.plexus.archiver.manager.DefaultArchiverManager;
import org.codehaus.plexus.archiver.manager.NoSuchArchiverException;
import org.codehaus.plexus.components.io.fileselectors.IncludeExcludeFileSelector;
import org.codehaus.plexus.util.StringUtils;

/**
 *
 * @author Chris Tapley
 */
@Mojo( name = "unpack", requiresProject = false, threadSafe = true )
public class UnpackMojo extends JenkinsPluginAbstractMojo {

    @Parameter(defaultValue = "**/**")
    String includes;

    @Parameter(required = false)
    String excludes;

    protected File getTemporaryFileWithExtension(String extension) throws IOException {
        return java.io.File.createTempFile(UUID.randomUUID().toString(), "." + extension);
    }

    protected String getFileExtension(String url) throws IOException {
        return FilenameUtils.getExtension(url);
    }

    protected ArchiverManager getArchiverManager() {
        return new DefaultArchiverManager();
    }
    
    protected void unpack(File archive) throws NoSuchArchiverException {
        File destination = ensureOutputDirectoryExists();
        ArchiverManager archiverManager = getArchiverManager();
        UnArchiver unArchiver = archiverManager.getUnArchiver(archive);
        unArchiver.setSourceFile(archive);
        unArchiver.setDestDirectory(destination);
        
        if (StringUtils.isNotEmpty(excludes) || StringUtils.isNotEmpty(includes)) {
            IncludeExcludeFileSelector[] selectors = new IncludeExcludeFileSelector[]{new IncludeExcludeFileSelector()};

            if (StringUtils.isNotEmpty(excludes)) {
                selectors[0].setExcludes(excludes.split(","));
            }

            if (StringUtils.isNotEmpty(includes)) {
                selectors[0].setIncludes(includes.split(","));
            }

            unArchiver.setFileSelectors(selectors);
        }
        
        unArchiver.extract();
    }

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {

        JenkinsClient jenkinsClient = getJenkinsClient();
        try {
            List<String> matchingArtifactUrls = jenkinsClient.getMatchingArtifactUrls(buildArtifact);

            getLog().info(String.format("Unpacking %s from job %s with build %s from %s", buildArtifact, jobName, buildNumber, jenkinsUrl));

            for (String url : matchingArtifactUrls) {
                getLog().info(String.format("Processing detected artifact url %s", url));
                String extension = getFileExtension(url);
                File archiveFile = getTemporaryFileWithExtension(extension);
                jenkinsClient.downloadArtifact(url, archiveFile);
                unpack(archiveFile);
            }
        } catch (Exception ex) {
            throw new MojoExecutionException("Failed to process artifacts", ex);
        }
    }
}
