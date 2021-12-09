/*
 * The MIT License
 *
 * Copyright 2015 Christopher Simons
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package hudson.jobs;

import static org.junit.Assert.assertEquals;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebRequest;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.MessageFormat;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.jvnet.hudson.test.Issue;
import org.jvnet.hudson.test.JenkinsRule;

/**
 * Tests the /disable and /enable job methods for the REST API.
 *
 * @author Tyler Zender
 */
public class DisableJobTest {
    @Rule
    public JenkinsRule rule = new JenkinsRule();

    @Before
    public void setup() {
        rule.jenkins.setSecurityRealm(rule.createDummySecurityRealm());
    }

    // CS 427 issue link: https://issues.jenkins.io/browse/JENKINS-31011
    /**
     * Tests the /disable method for the REST API.
     */
    @Issue("JENKINS-31011")
    @Test
    public void testDisableJobFromAPI() throws Exception {
        rule.jenkins.setCrumbIssuer(null);

        String jobName = "testJob";
        rule.createFreeStyleProject(jobName);


        URL apiURL = new URL(MessageFormat.format(
                "{0}job/{1}/disable",
                rule.getURL().toString(), jobName));

        WebRequest request = new WebRequest(apiURL, HttpMethod.POST);

        Page p = rule.createWebClient()
                .withThrowExceptionOnFailingStatusCode(false)
                .getPage(request);
        assertEquals("Disabling job should succeed.",
                HttpURLConnection.HTTP_OK,
                p.getWebResponse().getStatusCode());
    }
    
    // CS 427 issue link: https://issues.jenkins.io/browse/JENKINS-31011
    /**
     * Tests the /disable and /enable methods for the REST API.
     */
    @Issue("JENKINS-31011")
    @Test
    public void testDisableEnableJobFromAPI() throws Exception {
        rule.jenkins.setCrumbIssuer(null);

        String jobName = "testJob";
        rule.createFreeStyleProject(jobName);


        URL apiDisableURL = new URL(MessageFormat.format(
                "{0}job/{1}/disable",
                rule.getURL().toString(), jobName));

        WebRequest disableRequest = new WebRequest(apiDisableURL, HttpMethod.POST);

        Page p = rule.createWebClient()
                .withThrowExceptionOnFailingStatusCode(false)
                .getPage(disableRequest);
        assertEquals("Disabling job should succeed.",
                HttpURLConnection.HTTP_OK,
                p.getWebResponse().getStatusCode());

        URL apiURLEnable = new URL(MessageFormat.format(
                "{0}job/{1}/enable",
                rule.getURL().toString(), jobName));

        WebRequest enableRequest = new WebRequest(apiURLEnable, HttpMethod.POST);

        p = rule.createWebClient()
                .withThrowExceptionOnFailingStatusCode(false)
                .getPage(enableRequest);
        assertEquals("Enabling job should succeed.",
                HttpURLConnection.HTTP_OK,
                p.getWebResponse().getStatusCode());
    }
}
