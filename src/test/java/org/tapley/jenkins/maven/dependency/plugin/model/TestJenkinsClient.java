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
package org.tapley.jenkins.maven.dependency.plugin.model;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.ExpectedException;
import static org.mockito.Matchers.any;
import org.mockito.Mock;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;
import org.mockito.stubbing.Answer;

/**
 *
 * @author ctapley
 */
public class TestJenkinsClient {

    String jobResponse = "{\"_class\":\"hudson.plugins.project_inheritance.projects.InheritanceBuild\",\"actions\":[{\"_class\":\"hudson.model.ParametersAction\",\"parameters\":[{\"_class\":\"hudson.model.StringParameterValue\",\"name\":\"BRANCH\",\"value\":\"RL_2017_MAPPX_16.4\"},{\"_class\":\"hudson.model.StringParameterValue\",\"name\":\"BRANCH_APOLLO_SERVER_VERSION\",\"value\":\"16.4.0-SNAPSHOT\"},{\"_class\":\"hudson.model.StringParameterValue\",\"name\":\"BRANCH_APOLLO_WINDOWS_COMPILER_VERSION\",\"value\":\"msvc120\"},{\"_class\":\"hudson.model.StringParameterValue\",\"name\":\"BRANCH_ECWJP2SDK_VERSION\",\"value\":\"5.4.0.1102\"},{\"_class\":\"hudson.model.StringParameterValue\",\"name\":\"BRANCH_ENC_VERSION\",\"value\":\"16.04.0000\"},{\"_class\":\"hudson.model.StringParameterValue\",\"name\":\"BRANCH_EUCLIDEON_VERSION\",\"value\":\"1.5.3.0\"},{\"_class\":\"hudson.model.StringParameterValue\",\"name\":\"BRANCH_GDAL_VERSION\",\"value\":\"2.0.0\"},{\"_class\":\"hudson.model.StringParameterValue\",\"name\":\"BRANCH_GEN_4_PREFIX\",\"value\":\"Generation_4\"},{\"_class\":\"hudson.model.StringParameterValue\",\"name\":\"BRANCH_GEN_PREFIX\",\"value\":\"Image_Web_Server_2016_MAppX_16_4\"},{\"_class\":\"hudson.model.StringParameterValue\",\"name\":\"BRANCH_JAVA_VERSION\",\"value\":\"1.7\"},{\"_class\":\"hudson.model.StringParameterValue\",\"name\":\"BRANCH_JDK_VERSION\",\"value\":\"jdk1.8.0_60\"},{\"_class\":\"hudson.model.StringParameterValue\",\"name\":\"BRANCH_MVN_CMD_VERSION\",\"value\":\"3.3.3\"},{\"_class\":\"hudson.model.StringParameterValue\",\"name\":\"BRANCH_P4_GEN_4_PREFIX\",\"value\":\"//depot/${BRANCH_REL_PREFIX}/${BRANCH_GEN_4_PREFIX}\"},{\"_class\":\"hudson.model.StringParameterValue\",\"name\":\"BRANCH_P4_PREFIX\",\"value\":\"//depot/${BRANCH_REL_PREFIX}/${BRANCH_GEN_PREFIX}\"},{\"_class\":\"hudson.model.StringParameterValue\",\"name\":\"BRANCH_PYTHON_POSTFIX\",\"value\":\"ML-SNAPSHOT\"},{\"_class\":\"hudson.model.StringParameterValue\",\"name\":\"BRANCH_REL_PREFIX\",\"value\":\"Release\"},{\"_class\":\"hudson.model.StringParameterValue\",\"name\":\"BRANCH_SNAPSHOT_VERSION\",\"value\":\"16.4.0-SNAPSHOT\"},{\"_class\":\"hudson.model.StringParameterValue\",\"name\":\"BRANCH_Windmill\",\"value\":\"Windmill_Mainline\"},{\"_class\":\"hudson.model.StringParameterValue\",\"name\":\"BUILD_MAJOR\",\"value\":\"$IWS_BUILD_MAJOR\"},{\"_class\":\"hudson.model.StringParameterValue\",\"name\":\"BUILD_MINOR\",\"value\":\"$IWS_BUILD_MINOR\"},{\"_class\":\"hudson.model.StringParameterValue\",\"name\":\"BUILD_PATCH\",\"value\":\"\"},{\"_class\":\"hudson.model.StringParameterValue\",\"name\":\"BUILD_RELEASE\",\"value\":\"$IWS_BUILD_RELEASE\"},{\"_class\":\"hudson.model.StringParameterValue\",\"name\":\"BUILD_TYPE\",\"value\":\"FULL\"},{\"_class\":\"hudson.model.StringParameterValue\",\"name\":\"COMMON_BUILD_NUMBER\",\"value\":\"92\"},{\"_class\":\"hudson.model.StringParameterValue\",\"name\":\"ECHO\",\"value\":\"on\"},{\"_class\":\"hudson.model.StringParameterValue\",\"name\":\"ECWJP2SDK_BUILD_MAJOR\",\"value\":\"5\"},{\"_class\":\"hudson.model.StringParameterValue\",\"name\":\"ECWJP2SDK_BUILD_MINOR\",\"value\":\"4\"},{\"_class\":\"hudson.model.StringParameterValue\",\"name\":\"ECWJP2SDK_BUILD_RELEASE\",\"value\":\"0\"},{\"_class\":\"hudson.model.StringParameterValue\",\"name\":\"ENABLE_PROMOTION_TO_DEVELOPMENT\",\"value\":\"false\"},{\"_class\":\"hudson.model.StringParameterValue\",\"name\":\"INSTALLER_ARTEFACT_APOLLO_UTILITY\",\"value\":\"ERDASAPOLLOUtilities.exe\"},{\"_class\":\"hudson.model.StringParameterValue\",\"name\":\"INSTALLER_ARTEFACT_ERDAS_APOLLO\",\"value\":\"ERDASAPOLLOCore.exe\"},{\"_class\":\"hudson.model.StringParameterValue\",\"name\":\"IS_BLD_CMD\",\"value\":\"\\\"C:\\\\Program Files (x86)\\\\InstallShield\\\\2015 SAB\\\\System\\\\IsCmdBld.exe\\\"\"},{\"_class\":\"hudson.model.StringParameterValue\",\"name\":\"IWS_BUILD_MAJOR\",\"value\":\"16\"},{\"_class\":\"hudson.model.StringParameterValue\",\"name\":\"IWS_BUILD_MINOR\",\"value\":\"4\"},{\"_class\":\"hudson.model.StringParameterValue\",\"name\":\"IWS_BUILD_RELEASE\",\"value\":\"0\"},{\"_class\":\"hudson.model.StringParameterValue\",\"name\":\"IWS_RELEASE_YEAR\",\"value\":\"2017\"},{\"_class\":\"hudson.model.BooleanParameterValue\",\"name\":\"P4CLEANWORKSPACE\",\"value\":true},{\"_class\":\"hudson.model.BooleanParameterValue\",\"name\":\"P4FORCESYNC\",\"value\":true},{\"_class\":\"hudson.model.StringParameterValue\",\"name\":\"SNAPSHOT_VERSION\",\"value\":\"${BUILD_MAJOR}.${BUILD_MINOR}.${BUILD_RELEASE}-SNAPSHOT\"},{\"_class\":\"hudson.model.StringParameterValue\",\"name\":\"TEST_DATABASE_SUFFIX\",\"value\":\"_${BRANCH}\"},{\"_class\":\"hudson.model.StringParameterValue\",\"name\":\"UPSTREAM_BUILD_NUMBER\",\"value\":\"153\"},{\"_class\":\"hudson.model.StringParameterValue\",\"name\":\"UPSTREAM_JOB_NAME\",\"value\":\"RL_2017_MAPPX_16.4_IWS_WINDOWS\"},{\"_class\":\"hudson.model.StringParameterValue\",\"name\":\"UPSTREAM_P4_CHANGELIST\",\"value\":\"549321\"}]},{},{\"_class\":\"hudson.model.CauseAction\",\"causes\":[{\"_class\":\"hudson.model.Cause$UpstreamCause\",\"shortDescription\":\"Started by upstream project \\\"RL_2017_MAPPX_16.4_IWS_WINDOWS\\\" build number 153\",\"upstreamBuild\":153,\"upstreamProject\":\"RL_2017_MAPPX_16.4_IWS_WINDOWS\",\"upstreamUrl\":\"job/RL_2017_MAPPX_16.4_IWS_WINDOWS/\"}]},{},{\"_class\":\"hudson.plugins.perforce.PerforceTagAction\"},{},{\"_class\":\"hudson.plugins.promoted_builds.PromotedBuildAction\"},{},{},{},{\"_class\":\"hudson.model.ParametersAction\",\"parameters\":[]},{},{},{},{},{},{},{}],\"artifacts\":[{\"displayPath\":\"cli-application-16.4.0-SNAPSHOT-windows-msvc120-x64.zip\",\"fileName\":\"cli-application-16.4.0-SNAPSHOT-windows-msvc120-x64.zip\",\"relativePath\":\"cli-application-16.4.0-SNAPSHOT-windows-msvc120-x64.zip\"},{\"displayPath\":\"debug-symbols-16.4.0-SNAPSHOT-windows-msvc120-x64.zip\",\"fileName\":\"debug-symbols-16.4.0-SNAPSHOT-windows-msvc120-x64.zip\",\"relativePath\":\"debug-symbols-16.4.0-SNAPSHOT-windows-msvc120-x64.zip\"},{\"displayPath\":\"FULL_VERSION\",\"fileName\":\"FULL_VERSION\",\"relativePath\":\"FULL_VERSION\"},{\"displayPath\":\"OEMLicense-16.4.0-SNAPSHOT-windows-msvc120-x64.zip\",\"fileName\":\"OEMLicense-16.4.0-SNAPSHOT-windows-msvc120-x64.zip\",\"relativePath\":\"OEMLicense-16.4.0-SNAPSHOT-windows-msvc120-x64.zip\"},{\"displayPath\":\"apollo-essentials-configuration-16.4.0-SNAPSHOT-windows-x64.zip\",\"fileName\":\"apollo-essentials-configuration-16.4.0-SNAPSHOT-windows-x64.zip\",\"relativePath\":\"Release/Image_Web_Server_2016_MAppX_16_4/sources_iws/Master/apollo-essentials-configuration-16.4.0-SNAPSHOT-windows-x64.zip\"},{\"displayPath\":\"ERDASAPOLLOCore.exe\",\"fileName\":\"ERDASAPOLLOCore.exe\",\"relativePath\":\"Release/Image_Web_Server_2016_MAppX_16_4/sources_iws/Master/ERDASAPOLLOCore.exe\"},{\"displayPath\":\"ERDASAPOLLOUtilities.exe\",\"fileName\":\"ERDASAPOLLOUtilities.exe\",\"relativePath\":\"Release/Image_Web_Server_2016_MAppX_16_4/sources_iws/Master/ERDASAPOLLOUtilities.exe\"},{\"displayPath\":\"msm-16.4.0-SNAPSHOT-windows-msvc120-x64.zip\",\"fileName\":\"msm-16.4.0-SNAPSHOT-windows-msvc120-x64.zip\",\"relativePath\":\"Release/Image_Web_Server_2016_MAppX_16_4/sources_iws/Master/msm-16.4.0-SNAPSHOT-windows-msvc120-x64.zip\"}],\"building\":false,\"description\":\"FULL :: 16,4,0,92  \",\"displayName\":\"#142\",\"duration\":5298720,\"estimatedDuration\":5425914,\"executor\":null,\"fullDisplayName\":\"RL_2017_MAPPX_16.4_IWS_WINDOWS_INSTALLER #142\",\"id\":\"142\",\"keepLog\":true,\"number\":142,\"queueId\":2840,\"result\":\"SUCCESS\",\"timestamp\":1500836378112,\"url\":\"https://brewery.ingrnet.com/view/M.App%20X%202017%20(16.4)/job/RL_2017_MAPPX_16.4_IWS_WINDOWS_INSTALLER/142/\",\"builtOn\":\"TweekTweak\",\"changeSet\":{\"_class\":\"hudson.plugins.perforce.PerforceChangeLogSet\",\"items\":[],\"kind\":null}}";
    
    JenkinsClient client;
    JenkinsClient clientSpy;
    
    String expectedJenkinsUrl = "expectedJenkinsUrl";
    String expectedJobName = "expectedJobName";
    String expectedBuildNumber = "expectedBuildNumber";
    
    @Mock
    HttpClient httpClient;
    
    @Rule
    public final ExpectedException expectedException = ExpectedException.none();
    
    @Before
    public void init() {
        client = new JenkinsClient(expectedJenkinsUrl, expectedJobName, expectedBuildNumber);
        clientSpy = spy(client);
        
        MockitoAnnotations.initMocks(this);
    }
    
    @Test
    public void getJobApiJsonUrl() {
        String actual = client.getJobApiJsonUrl();
        assertEquals(String.format("%s/job/%s/%s/api/json", expectedJenkinsUrl, expectedJobName, expectedBuildNumber), actual);
    }
    
    @Test
    public void getHttpClient() {
        HttpClient actualHttpClient = client.getHttpClient();
        assertNotNull(actualHttpClient);
        assertTrue(actualHttpClient instanceof DefaultHttpClient);
    }
    
    @Test
    public void performHttpGet_throws() throws IOException {
        expectedException.expect(IOException.class);
        doReturn(httpClient).when(clientSpy).getHttpClient();
        when(httpClient.execute(any())).thenThrow(new IOException("Bang!"));
        String expectedUrl = "expectedUrl";
        clientSpy.performHttpGet(expectedUrl);
    }
    
    @Test
    public void performHttpGet_non200response() throws IOException {
        expectedException.expect(IllegalStateException.class);
        expectedException.expectMessage("500");
        doReturn(httpClient).when(clientSpy).getHttpClient();
        String expectedUrl = "http://brewery.ingrnet.com/job/test/1/api/json";
        HttpResponse response = mock(HttpResponse.class);
        StatusLine statusLine = mock(StatusLine.class);
        
        when(httpClient.execute(any())).thenReturn(response);
        when(response.getStatusLine()).thenReturn(statusLine);
        when(statusLine.getStatusCode()).thenReturn(500);
        
        
        
        clientSpy.performHttpGet(expectedUrl);
    }
    
    @Test
    public void performHttpGet_ok() throws IOException {
        
        doReturn(httpClient).when(clientSpy).getHttpClient();
        String expectedUrl = "http://brewery.ingrnet.com/job/test/1/api/json";
        HttpResponse response = mock(HttpResponse.class);
        StatusLine statusLine = mock(StatusLine.class);
        HttpEntity httpEntity = mock(HttpEntity.class);
        InputStream inputStream = new ByteArrayInputStream(jobResponse.getBytes());
        
        when(httpClient.execute(any())).thenAnswer((Answer) (InvokationArguments) -> {
            HttpGet request = (HttpGet)InvokationArguments.getArgumentAt(0, HttpGet.class);
            assertEquals(expectedUrl, request.getURI().toASCIIString());
            return response;
        });
        
        when(response.getStatusLine()).thenReturn(statusLine);
        when(statusLine.getStatusCode()).thenReturn(200);
        when(response.getEntity()).thenReturn(httpEntity);
        when(httpEntity.getContent()).thenReturn(inputStream);
        
        assertEquals(jobResponse, IOUtils.toString(clientSpy.performHttpGet(expectedUrl)));
    }
}