/**
 * Licensed to jclouds, Inc. (jclouds) under one or more
 * contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  jclouds licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unles required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either expres or implied.  See the License for the
 * specific language governing permisions and limitations
 * under the License.
 */
package org.jclouds.ec2.features;
import static org.testng.Assert.assertEquals;

import java.util.TimeZone;

import org.jclouds.ec2.EC2Client;
import org.jclouds.ec2.internal.BaseEC2ExpectTest;
import org.jclouds.ec2.parse.DescribeTagsResponseTest;
import org.jclouds.http.HttpRequest;
import org.jclouds.http.HttpResponse;
import org.jclouds.rest.ResourceNotFoundException;
import org.testng.annotations.Test;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.ImmutableSet;

/**
 * @author Adrian Cole
 */
@Test(groups = "unit")
public class TagApiExpectTest extends BaseEC2ExpectTest<EC2Client> {

   public TagApiExpectTest() {
      TimeZone.setDefault(TimeZone.getTimeZone("America/Los_Angeles"));
   }

   HttpRequest list = HttpRequest.builder()
                                       .method("POST")
                                       .endpoint("https://ec2.us-east-1.amazonaws.com/")
                                       .addHeader("Host", "ec2.us-east-1.amazonaws.com")
                                       .payload(
                                          payloadFromStringWithContentType(
                                                   "Action=DescribeTags" +
                                                   "&Signature=tRTjSOEYQjjwAB7319oVYaHES8aRpJviHYqtst26M2I%3D" +
                                                   "&SignatureMethod=HmacSHA256" +
                                                   "&SignatureVersion=2" +
                                                   "&Timestamp=2012-04-16T15%3A54%3A08.897Z" +
                                                   "&Version=2010-06-15" +
                                                   "&AWSAccessKeyId=identity",
                                                "application/x-www-form-urlencoded"))
                                       .build();
   
   public void testListWhenResponseIs2xx() throws Exception {

      HttpResponse listResponse = HttpResponse.builder().statusCode(200)
            .payload(payloadFromResourceWithContentType("/describe_tags.xml", "text/xml")).build();

      EC2Client apiWhenExist = requestSendsResponse(
            list, listResponse);

      assertEquals(apiWhenExist.getTagApi().list().toString(), new DescribeTagsResponseTest().expected().toString());
   }

   public void testListWhenResponseIs404() throws Exception {

      HttpResponse listResponse = HttpResponse.builder().statusCode(404).build();

      EC2Client apiWhenDontExist = requestSendsResponse(
            list, listResponse);

      apiWhenDontExist.getTagApi().list();
   }
   
   public void testFilterWhenResponseIs2xx() throws Exception {
      HttpRequest filter =
            HttpRequest.builder()
                       .method("POST")
                       .endpoint("https://ec2.us-east-1.amazonaws.com/")
                       .addHeader("Host", "ec2.us-east-1.amazonaws.com")
                       .payload(payloadFromStringWithContentType(
                                                  "Action=DescribeTags" +
                                                  "&Filter.1.Name=resource-type" +
                                                  "&Filter.1.Value.1=instance" +
                                                  "&Filter.2.Name=key" +
                                                  "&Filter.2.Value.1=stack" +
                                                  "&Signature=2XCMA6TMDg%2BL8E0TcX%2BnQgasJ0WMdBrcbt%2B2WST8KPg%3D" +
                                                  "&SignatureMethod=HmacSHA256" +
                                                  "&SignatureVersion=2" +
                                                  "&Timestamp=2012-04-16T15%3A54%3A08.897Z" +
                                                  "&Version=2010-06-15" +
                                                  "&AWSAccessKeyId=identity",
                                            "application/x-www-form-urlencoded"))
                       .build();
      
      HttpResponse filterResponse = HttpResponse.builder().statusCode(200)
               .payload(payloadFromResourceWithContentType("/describe_tags.xml", "text/xml")).build();

      EC2Client apiWhenExist = requestSendsResponse(filter, filterResponse);

      assertEquals(apiWhenExist.getTagApi().filter(ImmutableMultimap.<String, String> builder()
                                                         .put("resource-type", "instance")
                                                         .put("key", "stack")
                                                         .build()).toString(),
               new DescribeTagsResponseTest().expected().toString());
   }
   
   HttpRequest delete = HttpRequest.builder()
            .method("POST")
            .endpoint("https://ec2.us-east-1.amazonaws.com/")
            .addHeader("Host", "ec2.us-east-1.amazonaws.com")
            .payload(
               payloadFromStringWithContentType(
                        "Action=DeleteTags" +
                        "&ResourceId.1=i-43532" +
                        "&Signature=ArC3WJMreQSMR3S0IjF4%2BEtZ8q1EZTLTP%2FM%2BoRwlYME%3D" +
                        "&SignatureMethod=HmacSHA256" +
                        "&SignatureVersion=2" +
                        "&Tag.1.Key=tag" +
                        "&Timestamp=2012-04-16T15%3A54%3A08.897Z" +
                        "&Version=2010-06-15" +
                        "&AWSAccessKeyId=identity",
                     "application/x-www-form-urlencoded"))
            .build();

   public void testDeleteWhenResponseIs2xx() throws Exception {

      HttpResponse deleteResponse = HttpResponse.builder().statusCode(200).build();

      EC2Client apiWhenExist = requestSendsResponse(delete, deleteResponse);

      apiWhenExist.getTagApi().deleteTagsFromResources(ImmutableSet.of("tag"), ImmutableSet.of("i-43532"));
   }

   @Test(expectedExceptions = ResourceNotFoundException.class)
   public void testDeleteWhenResponseIs404() throws Exception {

      HttpResponse deleteResponse = HttpResponse.builder().statusCode(404).build();

      EC2Client apiWhenDontExist = requestSendsResponse(delete, deleteResponse);

      apiWhenDontExist.getTagApi().deleteTagsFromResources(ImmutableSet.of("tag"), ImmutableSet.of("i-43532"));
   }
}
