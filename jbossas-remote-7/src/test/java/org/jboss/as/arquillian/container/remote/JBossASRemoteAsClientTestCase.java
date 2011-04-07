/*
 * JBoss, Home of Professional Open Source
 * Copyright 2009, Red Hat Middleware LLC, and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jboss.as.arquillian.container.remote;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URL;

import org.jboss.arquillian.api.ArquillianResource;
import org.jboss.arquillian.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.as.arquillian.container.remote.testmodel.EchoBean;
import org.jboss.as.arquillian.container.remote.testmodel.EchoServlet;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * JBossASRemoteIntegrationTestCase
 *
 * @author <a href="kabir.khan@jboss.com">Kabir Khan</a>
 */
@RunWith(Arquillian.class)
public class JBossASRemoteAsClientTestCase {

    @Deployment(testable = false)
    public static WebArchive createDeployment() throws Exception {
        return ShrinkWrap.create(WebArchive.class)
                    .addClasses(EchoServlet.class, EchoBean.class)
                    .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");
    }

    @Test
    public void shouldbeAbleToCallServlet(@ArquillianResource URL baseURL) throws Exception
    {
        String echoThis = "weeeee";
        String body = readAllAndClose(new URL(baseURL, "echo/" + echoThis).openStream());    

        Assert.assertEquals(
                "Verify that the servlet was deployed and returns expected result",
                echoThis,
                body);

    }
    
    private String readAllAndClose(InputStream is) throws Exception 
    {
       ByteArrayOutputStream out = new ByteArrayOutputStream();
       try
       {
          int read;
          while( (read = is.read()) != -1)
          {
             out.write(read);
          }
       }
       finally 
       {
          try { is.close(); } catch (Exception e) { }
       }
       return out.toString();
    }
}
