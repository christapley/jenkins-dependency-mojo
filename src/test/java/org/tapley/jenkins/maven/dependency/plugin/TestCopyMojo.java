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

import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;

/**
 *
 * @author ctapley
 */
public class TestCopyMojo {
    
    CopyMojo mojo;
    
    @Before
    public void init() {
        mojo = new CopyMojo();
    }
    
    @Test
    public void getFileNameFromUrl() {
        String actual = mojo.getFileNameFromUrl("http://brewery.ingrnet.com/jobs/tests/artifact/path/to/file.zip");
        assertEquals("file.zip", actual);
    }
}
