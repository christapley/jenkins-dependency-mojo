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

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

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
    
    protected String getJobApiJsonUrl() {
        String url = String.format("%s/job/%s/%s/api/json", jenkinsUrl, jobName, buildNumber);
        return url;
    }
    
    protected String getStringFromInputStream(InputStream inputStream) throws IOException {
        return IOUtils.toString(inputStream, "UTF8");
    }
    
    protected InputStream performHttpGet(String url) throws IOException {
        HttpClient client = new DefaultHttpClient();
        HttpGet request = new HttpGet(url);
        HttpResponse response = client.execute(request);
        if(response.getStatusLine().getStatusCode() == 200) {
            return response.getEntity().getContent();
        }
        throw new IllegalStateException(String.format("%s returned %d", url, response.getStatusLine().getStatusCode()));
    }
    
    public List<String> getMatchingArtifactUrls(String artifactNameMatcher) throws IOException {
        
        String url = getJobApiJsonUrl();
        InputStream jsonStream = performHttpGet(url);
        String json = getStringFromInputStream(jsonStream);
        
		JsonParser parser = new JsonParser();
        JsonElement rootElement = parser.parse(json);
        return null;
    }
    
    public void downloadArtifact(String artifactUrl, File outputFile) {
        
    }
        
}
