/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.sling.api.resource.uri;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.when;

import org.apache.sling.api.request.RequestPathInfo;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ResourceUriToSlingRequestPathInfoCompatibilityTest {

    @Mock
    ResourceResolver resourceResolver;

    @Mock
    Resource resource;

    private RequestPathInfo createResourceUri(String resolutionPath, String resolutionPathInfo) {
        when(resourceResolver.getResource(resolutionPath)).thenReturn(resource);
        return ResourceUriBuilder.parse(resolutionPath + (resolutionPathInfo != null ? resolutionPathInfo : ""), resourceResolver).build();
    }

    @Test
    public void testTrailingDot() {
        RequestPathInfo p = createResourceUri("/some/path", ".");
        assertEquals("/some/path", p.getResourcePath());
        assertNull("Selectors are null", p.getSelectorString());
        assertEquals(0, p.getSelectors().length);
        assertNull("Extension is null", p.getExtension());
        assertNull("Suffix is null", p.getSuffix());
    }

    @Test
    @Ignore
    public void testTrailingDotWithSuffix() {
        RequestPathInfo p = createResourceUri("/some/path", "./suffix");
        assertEquals("/some/path", p.getResourcePath());
        assertNull("Selectors are null", p.getSelectorString());
        assertEquals(0, p.getSelectors().length);
        assertNull("Extension is null", p.getExtension());
        assertEquals("/suffix", p.getSuffix());
    }

    @Test
    public void testTrailingDotDot() {
        RequestPathInfo p = createResourceUri("/some/path", "..");
        assertEquals("/some/path", p.getResourcePath());
        assertNull("Selectors are null", p.getSelectorString());
        assertEquals(0, p.getSelectors().length);
        assertNull("Extension is null", p.getExtension());
        assertNull("Suffix is null", p.getSuffix());
    }

    @Test
    @Ignore
    public void testTrailingDotDotWithSuffix() {
        RequestPathInfo p = createResourceUri("/some/path", "../suffix");
        assertEquals("/some/path", p.getResourcePath());
        assertNull("Selectors are null", p.getSelectorString());
        assertEquals(0, p.getSelectors().length);
        assertNull("Extension is null", p.getExtension());
        assertEquals("/suffix", p.getSuffix());
    }

    @Test
    public void testTrailingDotDotDot() {
        RequestPathInfo p = createResourceUri("/some/path", "...");
        assertEquals("/some/path", p.getResourcePath());
        assertNull("Selectors are null", p.getSelectorString());
        assertEquals(0, p.getSelectors().length);
        assertNull("Extension is null", p.getExtension());
        assertNull("Suffix is null", p.getSuffix());
    }

    @Test
    public void testTrailingDotDotDotWithSuffix() {
        RequestPathInfo p = createResourceUri("/some/path", ".../suffix");
        assertEquals("/some/path", p.getResourcePath());
        assertNull("Selectors are null", p.getSelectorString());
        assertEquals(0, p.getSelectors().length);
        assertNull("Extension is null", p.getExtension());
        assertEquals("/suffix", p.getSuffix());
    }

    @Test
    public void testAllOptions() {
        RequestPathInfo p = createResourceUri("/some/path", ".print.a4.html/some/suffix");
        assertEquals("/some/path", p.getResourcePath());
        assertEquals("print.a4", p.getSelectorString());
        assertEquals(2, p.getSelectors().length);
        assertEquals("print", p.getSelectors()[0]);
        assertEquals("a4", p.getSelectors()[1]);
        assertEquals("html", p.getExtension());
        assertEquals("/some/suffix", p.getSuffix());
    }

    @Test
    public void testAllEmpty() {
        RequestPathInfo p = createResourceUri("/", null);
        assertEquals("/", p.getResourcePath());
        assertNull("Selectors are null", p.getSelectorString());
        assertEquals(0, p.getSelectors().length);
        assertNull("Extension is null", p.getExtension());
        assertNull("Suffix is null", p.getSuffix());
    }

    @Test
    public void testPathOnly() {
        RequestPathInfo p = createResourceUri("/some/path/here", "");
        assertEquals("/some/path/here", p.getResourcePath());
        assertNull("Selectors are null", p.getSelectorString());
        assertEquals(0, p.getSelectors().length);
        assertNull("Extension is null", p.getExtension());
        assertNull("Suffix is null", p.getSuffix());
    }

    @Test
    public void testPathWithExtensionOnly() {
        RequestPathInfo p = createResourceUri("/some/path/here.html", "");
        assertEquals("/some/path/here.html", p.getResourcePath());
        assertNull("Selectors are null", p.getSelectorString());
        assertEquals(0, p.getSelectors().length);
        assertNull("Extension is null", p.getExtension());
        assertNull("Suffix is null", p.getSuffix());
    }

    @Test
    public void testPathAndExtensionOnly() {
        RequestPathInfo p = createResourceUri("/some/path/here", ".html");
        assertEquals("/some/path/here", p.getResourcePath());
        assertNull("Selectors are null", p.getSelectorString());
        assertEquals(0, p.getSelectors().length);
        assertEquals("html", p.getExtension());
        assertNull("Suffix is null", p.getSuffix());
    }

    @Test
    public void testPathAndOneSelectorOnly() {
        RequestPathInfo p = createResourceUri("/some/path/here", ".print.html");
        assertEquals("/some/path/here", p.getResourcePath());
        assertEquals("print", p.getSelectorString());
        assertEquals(1, p.getSelectors().length);
        assertEquals("print", p.getSelectors()[0]);
        assertEquals("html", p.getExtension());
        assertNull("Suffix is null", p.getSuffix());
    }

    @Test
    public void testPathExtAndSuffix() {
        RequestPathInfo p = createResourceUri("/some/path/here", ".html/something");
        assertEquals("/some/path/here", p.getResourcePath());
        assertNull("Selectors are null", p.getSelectorString());
        assertEquals(0, p.getSelectors().length);
        assertEquals("html", p.getExtension());
        assertEquals("/something", p.getSuffix());
    }

    @Test
    public void testSelectorsSplit() {
        RequestPathInfo p = createResourceUri("/some/path", ".print.a4.html/some/suffix");
        assertEquals("/some/path", p.getResourcePath());
        assertEquals(2, p.getSelectors().length);
        assertEquals("print", p.getSelectors()[0]);
        assertEquals("a4", p.getSelectors()[1]);
        assertEquals("html", p.getExtension());
        assertEquals("/some/suffix", p.getSuffix());
    }

    @Test
    public void testPartialResolutionB() {
        RequestPathInfo p = createResourceUri("/some/path", ".print.a4.html/some/suffix");
        assertEquals("/some/path", p.getResourcePath());
        assertEquals("print.a4", p.getSelectorString());
        assertEquals(2, p.getSelectors().length);
        assertEquals("print", p.getSelectors()[0]);
        assertEquals("a4", p.getSelectors()[1]);
        assertEquals("html", p.getExtension());
        assertEquals("/some/suffix", p.getSuffix());
    }

    @Test
    public void testPartialResolutionC() {
        RequestPathInfo p = createResourceUri("/some/path.print", ".a4.html/some/suffix");
        assertEquals("/some/path.print", p.getResourcePath());
        assertEquals("a4", p.getSelectorString());
        assertEquals(1, p.getSelectors().length);
        assertEquals("a4", p.getSelectors()[0]);
        assertEquals("html", p.getExtension());
        assertEquals("/some/suffix", p.getSuffix());
    }

    @Test
    public void testPartialResolutionD() {
        RequestPathInfo p = createResourceUri("/some/path.print.a4", ".html/some/suffix");
        assertEquals("/some/path.print.a4", p.getResourcePath());
        assertNull("Selectors are null", p.getSelectorString());
        assertEquals(0, p.getSelectors().length);
        assertEquals("html", p.getExtension());
        assertEquals("/some/suffix", p.getSuffix());
    }

    @Test
    public void testDotsAroundSuffix() {
        RequestPathInfo p = createResourceUri("/libs/foo/content/something/formitems", ".json/image/vnd/xnd/knd.xml");
        assertEquals("/libs/foo/content/something/formitems", p.getResourcePath());
        assertEquals("json", p.getExtension());
        assertNull("Selectors are null", p.getSelectorString());
        assertEquals("/image/vnd/xnd/knd.xml", p.getSuffix());
    }

    @Test
    public void testJIRA_250_a() {
        RequestPathInfo p = createResourceUri("/bunkai", ".1.json");
        assertEquals("/bunkai", p.getResourcePath());
        assertEquals("json", p.getExtension());
        assertEquals("1", p.getSelectorString());
    }

    @Test
    public void testJIRA_250_b() {
        RequestPathInfo p = createResourceUri("/", ".1.json");
        assertEquals("/", p.getResourcePath());
        assertEquals("json", p.getExtension());
        assertNull("Suffix is null", p.getSuffix());
        assertEquals("Selector string must not be null", "1",
                p.getSelectorString());
    }

    @Test
    public void testJIRA_250_c() {
        RequestPathInfo p = createResourceUri("/", ".1.json/my/suffix");
        assertEquals("/", p.getResourcePath());
        assertEquals("json", p.getExtension());
        assertEquals("/my/suffix", p.getSuffix());
        assertEquals("Selector string must not be null", "1",
                p.getSelectorString());
    }

    @Test
    public void testJIRA_250_d() {
        RequestPathInfo p = createResourceUri("/", ".json");
        assertEquals("/", p.getResourcePath());
        assertEquals("json", p.getExtension());
        assertNull("Suffix is null", p.getSuffix());
        assertNull("Selectors are null", p.getSelectorString());
    }

}
