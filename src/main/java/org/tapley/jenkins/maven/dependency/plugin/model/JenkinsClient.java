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

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.net.ssl.SSLContext;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLContextBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.util.AntPathMatcher;

/**
 *
 * @author Chris Tapley
 */
public class JenkinsClient {

    String jenkinsUrl;
    String jobName;
    String buildNumber;

    AntPathMatcher pathMatcher;

    public JenkinsClient(String jenkinsUrl, String jobName, String buildNumber) {
        this.jenkinsUrl = jenkinsUrl;
        this.jobName = jobName;
        this.buildNumber = buildNumber;
        pathMatcher = new AntPathMatcher();
    }

    protected String getJobApiJsonUrl() throws UnsupportedEncodingException {
        
        return String.format("%s/job/%s/%s/api/json", 
                jenkinsUrl, 
                URLEncoder.encode(jobName, "UTF-8").replaceAll("\\+", "%20"), 
                URLEncoder.encode(buildNumber, "UTF-8").replaceAll("\\+", "%20"));
    }
    
    public void ResolveBuildLabelToCurrentNumber() throws IOException, URISyntaxException {
        String jobApiJsonUrl = getJobApiJsonUrl();
        InputStream jsonStream = performHttpGet(jobApiJsonUrl);
        String json = getStringFromInputStream(jsonStream);
        JsonParser parser = new JsonParser();
        JsonElement rootElement = parser.parse(json);

        int buildNumberFromJson = rootElement.getAsJsonObject().get("number").getAsInt();
        this.buildNumber = Integer.toString(buildNumberFromJson);
    }
    
    protected String getStringFromInputStream(InputStream inputStream) throws IOException {
        return IOUtils.toString(inputStream, "UTF8");
    }

    protected SSLContext getSSLContext() throws NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
        return new SSLContextBuilder().loadTrustMaterial(null, (certificate, authType) -> true).build();
    }
    
    protected HttpClient getHttpClient() {
        try {
            
            CloseableHttpClient client = HttpClients.custom()
                    .setSSLContext(getSSLContext())
                    .setSSLHostnameVerifier(new NoopHostnameVerifier())
                    .build();

            return client;
        } catch(Exception ex) {
            throw new IllegalStateException("Failed to create http client", ex);
        }
    }
    
    protected InputStream performHttpGet(String url) throws IOException, URISyntaxException {
        HttpClient client = getHttpClient();
        HttpGet request = new HttpGet(url);
        HttpResponse response = client.execute(request);
        if (response.getStatusLine().getStatusCode() == 200) {
            return response.getEntity().getContent();
        }
        throw new IllegalStateException(String.format("%s returned %d", url, response.getStatusLine().getStatusCode()));
    }

    protected List<String> getArtifactPathsFromJenkins() throws IOException, URISyntaxException {
        String url = getJobApiJsonUrl();
        InputStream jsonStream = performHttpGet(url);
        String json = getStringFromInputStream(jsonStream);

        List<String> artifactPaths = new ArrayList<>();

        JsonParser parser = new JsonParser();
        JsonElement rootElement = parser.parse(json);

        JsonArray artifacts = rootElement.getAsJsonObject().getAsJsonArray("artifacts");

        for (JsonElement artifactElement : artifacts) {
            if (artifactElement.getAsJsonObject().has("relativePath")) {
                String artifactRelativePath = artifactElement.getAsJsonObject().get("relativePath").getAsString();
                artifactPaths.add(artifactRelativePath);
            }
        }
        return artifactPaths;
    }

    protected String getUrlForArtifactRelativePath(String artifactRelativePath) throws UnsupportedEncodingException {
        return String.format("%s/job/%s/%s/artifact/%s", jenkinsUrl, 
                URLEncoder.encode(jobName, "UTF-8").replaceAll("\\+", "%20"), 
                URLEncoder.encode(buildNumber, "UTF-8").replaceAll("\\+", "%20"), 
                artifactRelativePath);
    }

    public List<String> getMatchingArtifactUrls(String commaSeparatedArtifactNameMatchers) throws IOException, URISyntaxException {

        List<String> matchingArtifactUrls = new ArrayList<>();
        List<String> matchers = Arrays.asList(commaSeparatedArtifactNameMatchers.split(","));

        List<String> artifactRelativePaths = getArtifactPathsFromJenkins();
        for (String artifactRelativePath : artifactRelativePaths) {
            for (String matcher : matchers) {
                if (pathMatcher.match(matcher, artifactRelativePath)) {
                    matchingArtifactUrls.add(getUrlForArtifactRelativePath(artifactRelativePath));
                    break;
                }
            }
        }
        return matchingArtifactUrls;
    }

    public void downloadArtifact(String artifactUrl, File outputFile) throws IOException, URISyntaxException {
        InputStream inputStream = performHttpGet(artifactUrl);
        Files.copy(inputStream, outputFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
    }
}
