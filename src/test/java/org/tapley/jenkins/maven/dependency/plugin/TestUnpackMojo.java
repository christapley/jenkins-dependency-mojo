/*
 * Copyright 2017 Chris.
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
import org.codehaus.plexus.archiver.UnArchiver;
import org.codehaus.plexus.archiver.manager.ArchiverManager;
import org.codehaus.plexus.archiver.manager.DefaultArchiverManager;
import org.codehaus.plexus.archiver.manager.NoSuchArchiverException;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;
import org.mockito.Mock;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 *
 * @author Chris
 */
public class TestUnpackMojo extends TestMojoBase {

    UnpackMojo mojo;
    UnpackMojo mojoSpy;
    
    @Mock
    ArchiverManager archiveManager;
    
    @Before
    public void init() {
        mojo = new UnpackMojo();
        mojoSpy = spy(mojo);
        
        doReturn(jenkinsClient).when(mojoSpy).getJenkinsClient();
    }
    
    @Test
    public void getFileExtension() throws IOException {
        assertEquals("zip", mojo.getFileExtension("http://host.com/path/file.zip"));
        assertEquals("", mojo.getFileExtension("http://host.com/path/file"));
    }
    
    @Test
    public void getTemporaryFileWithExtension() throws IOException {
        File actual = mojo.getTemporaryFileWithExtension("zip");
        assertTrue(actual.getAbsolutePath().endsWith(".zip"));
    }
    
    @Test
    public void getArchiverManager() {
        ArchiverManager manager = mojo.getArchiverManager();
        assertNotNull(manager);
        assertTrue(manager instanceof DefaultArchiverManager);
    }
    
    @Test
    public void unpack() throws NoSuchArchiverException {
        File archive = mock(File.class);
        UnArchiver unArchiver = mock(UnArchiver.class);
        
        doReturn(destination).when(mojoSpy).ensureOutputDirectoryExists();
        doReturn(archiveManager).when(mojoSpy).getArchiverManager();
        doReturn(unArchiver).when(archiveManager).getUnArchiver(archive);
                
        mojoSpy.unpack(archive);
        
        verify(unArchiver, times(1)).setSourceFile(archive);
        verify(unArchiver, times(1)).setDestDirectory(destination);
        verify(unArchiver, times(1)).extract();
    }
}
