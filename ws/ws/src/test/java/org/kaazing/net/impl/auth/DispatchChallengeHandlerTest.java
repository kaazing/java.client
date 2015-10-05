/**
 * Copyright 2007-2015, Kaazing Corporation. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.kaazing.net.impl.auth;

import java.util.Collection;
import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import org.kaazing.net.auth.ChallengeHandler;
import org.kaazing.net.auth.ChallengeRequest;
import org.kaazing.net.auth.DispatchChallengeHandler;
import org.kaazing.net.impl.auth.DefaultDispatchChallengeHandler;

public class DispatchChallengeHandlerTest {
    DefaultDispatchChallengeHandler dispatch = (DefaultDispatchChallengeHandler) DispatchChallengeHandler.create();
    ChallengeHandler sampleHandler;

    @Before
    public void setUp() throws Exception {
        dispatch = (DefaultDispatchChallengeHandler) DispatchChallengeHandler.create();
        sampleHandler = SampleChallengeHandler.create();
    }

    @After
    public void tearDown() throws Exception {

    }


    @Test
    public void testLookup() throws Exception {
        matches("http://foo.example.com", "http://foo.example.com");
    }

    @Test
    public void shouldFindChallengerHandlerRegisteredWithNoSchemeNoPortOrPath() throws Exception {
        matches("foo.example.com", "ws://foo.example.com");
    }

    @Test
    public void rulesForSingleWildcardMatching() throws Exception {
        matches("*", "foo.example.com");
        matches("*", "foo.example.com:80");
        matches("*", "foo.example.com/");
        matches("*", "ws://foo.example.com/");
        matches("*", "ws://foo.example.com");
        matches("*", "ws://foo.example.com:80");
        matches("*", "http://foo.example.com/");
        matches("*:443", "https://foo.example.com");

        // must wildcard non-default port, or path, to match
        doesNotMatch("*", "ws://foo.example.com:8000");
        doesNotMatch("*", "ws://foo.example.com/path");
        doesNotMatch("*", "ws://foo.example.com:80/path");
        // no match since userinfo used
        doesNotMatch("*", "ws://user:pass@foo.example.com");
    }

    @Test
    public void rulesForWildcardedHostAndPortMatching() throws Exception {
        doesNotMatch("*:*", "foo.example.com/path");
        doesNotMatch("*:*", "user:pass@foo.example.com");

        matches("*:*", "foo.example.com:8000");
        matches("*:*", "foo.example.com");
        matches("*:*", "https://foo.example.com");
        matches("*:*", "wss://foo.example.com");
    }

    @Test
    public void rulesForWildcardedHostAndPathMatching() throws Exception {
        matches("*/*", "ws://foo.example.com/path1/path2");
        // no match since non-default port used
        doesNotMatch("*/*", "ws://foo.example.com:8000/path1/path2");
        // no match since no path used
        doesNotMatch("*/*", "ws://foo.example.com:80");
        // ok since default port used
        matches("*/*", "ws://foo.example.com:80/path1/path2");
    }

    @Test
    public void rulesForWildcardedUserInfoMatching() throws Exception {
        // must wildcard user/password to match
        matches("*@*", "ws://user:pass@foo.example.com");
        doesNotMatch("*@*", "ws://user:pass@foo.example.com/path"); // must wildcard path to match
        matches("*@*/*", "ws://user:pass@foo.example.com/path"); // must wildcard path to match

        // specific user matching
        matches("admin:*@*/*", "admin:pass@foo.example.com/path");
        doesNotMatch("admin:admin@*/*", "admin:pass@foo.example.com/path");
        matches("admin:admin@*/*", "admin:admin@foo.example.com/path");

        doesNotMatch("*:admin@*/*", "admin:foo@foo.example.com/path");
    }
    
    @Test
    public void shouldFindChallengerHandlerRegisteredWithSimpleWildCardHost() throws Exception {


        matches("*:*/*", "ws://foo.example.com/path1/path2");
        matches("*:*/*", "ws://foo.example.com:8000/path1/path2");

        doesNotMatch("*:*/*", "ws://user:pass@foo.example.com:8000/path1/path2");
        doesNotMatch("*@*:*/*", "ws://foo.example.com:8000/path1/path2");

        doesNotMatch("*@*:*/*", "ws://foo.example.com:8000/path1/path2");
        matches("*@*:*/*", "ws://user:pass@foo.example.com:8000/path1/path2");
    }


    private void matches(String locationDescription, String location) throws Exception {
        dispatch.register(locationDescription, sampleHandler);
        Collection<ChallengeHandler> challengeHandlers = dispatch.lookup(location);
        Assert.assertNotNull("Expecting " + locationDescription + " to match location " + location, challengeHandlers);
        Assert.assertEquals("Expecting "+locationDescription+" to match location "+location, 1, challengeHandlers.size());
        Assert.assertSame(sampleHandler, challengeHandlers.iterator().next());
        setUp();
    }

    private void doesNotMatch(String locationDescription, String location) throws Exception {
        dispatch.register(locationDescription, sampleHandler);
        Collection<ChallengeHandler> challengeHandlers = dispatch.lookup(location);
        assertIsEmpty("Not expecting " + locationDescription + " to match location " + location, challengeHandlers);
        setUp();
    }

    private <T> void assertIsEmpty(String description, Collection<T> collection) {
        if ( collection == null ) {
            Assert.fail(description);
        }
        if ( collection.size() != 0) {
            Assert.fail(description);
        }
        Assert.assertTrue(description, collection.isEmpty());
    }

    
    @Test
    public void shouldFindChallengerHandlerRegisteredWithNoSchemeExplicitPortAndPath() throws Exception {
        matches("foo.example.com:80/path", "ws://foo.example.com/path");
    }

    @Test
    public void shouldFindChallengerHandlerRegisteredWithNoSchemeWildcardPortAndPath() throws Exception {
        matches("foo.example.com:*/*", "ws://foo.example.com:8181/path");
    }

    @Test
    public void shouldNotFindChallengerHandlerDueToNonMatchingHost() throws Exception {
        doesNotMatch("foo.example.com:*/*", "ws://foo2.example.com:8181/path");
    }

    @Test
    public void shouldFindChallengerHandlerRegisteredWithNoSchemeAndAWildcardSubdomain() throws Exception {
        matches("*.hostname.com:8000", "ws://foo.hostname.com:8000");
    }

    @Test
    public void shouldFindChallengerHandlerRegisteredWithNoSchemeWildcardSubDomainAndPath() throws Exception {
        matches("*.hostname.com:8000/path", "ws://foo.hostname.com:8000/path");
    }

    @Test
    public void testFailedLookup() throws Exception {
        doesNotMatch("http://localhost:8000", "http://localhost");
    }

    @Test
    public void testLookupByChallengeExactMatch() throws Exception {
        dispatch.register("http://localhost:8000", sampleHandler);
        ChallengeHandler ChallengeHandler2 = dispatch.lookup(new ChallengeRequest("http://localhost:8000", "test_challenge foo"));
        Assert.assertSame(sampleHandler, ChallengeHandler2);
    }

    @Test
    public void testLookupByChallengeCloseWildcardMatch() throws Exception {
        dispatch.register("http://*.example.com", sampleHandler);
        ChallengeHandler challengeHandler =
                dispatch.lookup(new ChallengeRequest("http://foo.example.com", "test_challenge SOME CHALLENGE STRING"));
        Assert.assertSame(sampleHandler, challengeHandler);
    }

    @Test
    public void testLookupByChallengeNodeAboveWildcardMatch() throws Exception {
        dispatch.register("http://*.example.com", sampleHandler);
        ChallengeHandler challengeHandler =
                dispatch.lookup(new ChallengeRequest("http://foo.example.com:80", "test_challenge SOME CHALLENGE STRING"));
        Assert.assertSame(sampleHandler, challengeHandler);
    }

    @Test
    public void testPureWildcardRegistration() throws Exception {
        dispatch.register("http://*/", sampleHandler);
        ChallengeHandler challengeHandler =
                dispatch.lookup(new ChallengeRequest("http://foo.example.com:80", "test_challenge SOME CHALLENGE STRING"));
        Assert.assertSame(sampleHandler, challengeHandler);
    }

    @Test
    public void testTokenWildcardZeroLengthMatches() throws Exception {
        dispatch.register("http://sub.hostname.com:8000/path1/path2/*", sampleHandler);
        Collection<ChallengeHandler> lookup = dispatch.lookup("http://sub.hostname.com:8000/path1/path2");
        Assert.assertSame("Expected to find a challenge handler factory registered under " + "http://sub.hostname.com:8000/path1/path2/*" +
                " using " + "http://sub.hostname.com:8000/path1/path2" + " but failed.",
                sampleHandler, lookup.toArray()[0]);

    }

    @Test
    public void testSubdomainIsSignificant() throws Exception {
        doesNotMatch("http://sub.hostname.com:8000/path1/path2/*",
                "http://sub2.hostname.com:8000/path1/path2");

    }


    @Test
    public void testUriTokenizationOfEmptyStrings() throws Exception {
        List<DefaultDispatchChallengeHandler.Token<DefaultDispatchChallengeHandler.UriElement>> tokens = dispatch.tokenize(null);
        Assert.assertEquals(0, tokens.size());

        tokens = dispatch.tokenize("");
        Assert.assertEquals(0, tokens.size());

    }

    //----------------------------------------------------------
    // Tokenization tests
    //----------------------------------------------------------

    @Test
    public void testWildcardUriTokenization() throws Exception {
        List<DefaultDispatchChallengeHandler.Token<DefaultDispatchChallengeHandler.UriElement>> tokens =
                dispatch.tokenize("http://*:*");
        Assert.assertArrayEquals(new String[]{"*", "*"}, toStringArray(tokens));

    }

    @Test
    public void testKaazingCreateEmulatedUriTokenization() throws Exception {
        List<DefaultDispatchChallengeHandler.Token<DefaultDispatchChallengeHandler.UriElement>> tokens =
                dispatch.tokenize("http://echo.websocket.org/;e/cb");
        Assert.assertArrayEquals(new String[]{"org", "websocket", "echo",
                String.valueOf(dispatch.getDefaultPort("http")),
                ";e", "cb"}, toStringArray(tokens));
    }

    @Test
    public void testKaazingCreateEmulatedUriWithNonDefaultPortTokenization() throws Exception {
        List<DefaultDispatchChallengeHandler.Token<DefaultDispatchChallengeHandler.UriElement>> tokens =
                dispatch.tokenize("http://echo.websocket.org:8001/;e/cb");
        Assert.assertArrayEquals(new String[]{"org", "websocket", "echo", "8001", ";e", "cb"}, toStringArray(tokens));
    }

    @Test
    public void testFullUriTokenization() throws Exception {
        List<DefaultDispatchChallengeHandler.Token<DefaultDispatchChallengeHandler.UriElement>> tokens =
                dispatch.tokenize("http://user:password@sub.hostname.com:8000/path1/path2/*");
        Assert.assertArrayEquals(new String[]{"com", "hostname", "sub", "8000", "user", "password", "path1", "path2", "*"}, toStringArray(tokens));

    }

    @Test(expected = IllegalArgumentException.class)
    public void testBadUriInput() {
        dispatch.tokenize("http://");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMissingHost() {
        dispatch.tokenize("http:///foo");
    }

    @Test
    public void testCorrectlyHandleWildcardSubdomain() {
        List<DefaultDispatchChallengeHandler.Token<DefaultDispatchChallengeHandler.UriElement>> tokens =
                dispatch.tokenize("http://*.hostname.com/path1/path2/*");
        Assert.assertArrayEquals(new String[]{"com", "hostname", "*",
                String.valueOf(dispatch.getDefaultPort("http")),
                "path1", "path2", "*"}, toStringArray(tokens));
    }


    @Test
    public void testCorrectlyHandleUnspecifiedPort() {
        List<DefaultDispatchChallengeHandler.Token<DefaultDispatchChallengeHandler.UriElement>> tokens =
                dispatch.tokenize("http://user:password@sub.hostname.com/path1/path2/*");
        Assert.assertArrayEquals(new String[]{"com", "hostname", "sub",
                String.valueOf(dispatch.getDefaultPort("http")),
                "user", "password", "path1", "path2", "*"}, toStringArray(tokens));
    }

    @Test
    public void testCorrectlyHandleUnspecifiedAuthInfo() {
        List<DefaultDispatchChallengeHandler.Token<DefaultDispatchChallengeHandler.UriElement>> tokens =
                dispatch.tokenize("http://sub.hostname.com/path1/path2/*");
        Assert.assertArrayEquals(new String[]{"com", "hostname", "sub",
                String.valueOf(dispatch.getDefaultPort("http")),
                "path1", "path2", "*"}, toStringArray(tokens));
    }

    @Test
    public void testCorrectlyHandleUnspecifiedPathInfo() {
        List<DefaultDispatchChallengeHandler.Token<DefaultDispatchChallengeHandler.UriElement>> tokens =
                dispatch.tokenize("http://sub.hostname.com");
        Assert.assertArrayEquals(new String[]{"com", "hostname", "sub",
        String.valueOf(dispatch.getDefaultPort("http"))
        }, toStringArray(tokens));
    }

    @Test
    public void testCorrectlyHandleTrailingSlashAfterHostName() {
        List<DefaultDispatchChallengeHandler.Token<DefaultDispatchChallengeHandler.UriElement>> tokens =
                dispatch.tokenize("http://sub.hostname.com/");
        Assert.assertArrayEquals(new String[]{"com", "hostname", "sub",
            String.valueOf(dispatch.getDefaultPort("http")),
            }, toStringArray(tokens));
    }

    private String[] toStringArray(List<DefaultDispatchChallengeHandler.Token<DefaultDispatchChallengeHandler.UriElement>> tokens) {
        String[] result = new String[tokens.size()];
        int i = 0;
        for ( DefaultDispatchChallengeHandler.Token<DefaultDispatchChallengeHandler.UriElement> token: tokens) {
            result[i++] = token.getName();
        }
        return result;
    }
}
