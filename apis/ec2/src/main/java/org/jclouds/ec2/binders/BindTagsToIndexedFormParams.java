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
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.jclouds.ec2.binders;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.collect.Maps.transformValues;
import static org.jclouds.aws.util.AWSUtils.indexMapToFormValuesWithPrefix;

import java.util.Map;

import org.jclouds.http.HttpRequest;
import org.jclouds.rest.Binder;

import com.google.common.base.Function;
import com.google.common.base.Optional;

/**
 * @author Adrian Cole
 */
public class BindTagsToIndexedFormParams implements Binder {

   @Override
   public <R extends HttpRequest> R bindToRequest(R request, Object input) {
      checkArgument(checkNotNull(input, "input") instanceof Map,
            "This binder is only valid for Map<String,Optional<String>>");
      @SuppressWarnings("unchecked")
      Map<String, Optional<String>> values = (Map<String, Optional<String>>) input;
      return indexMapToFormValuesWithPrefix(request, "Tag", "Key", "Value", transformValues(values, orEmptyString));
   }

   private final static Function<Optional<String>, String> orEmptyString = new Function<Optional<String>, String>() {

      @Override
      public String apply(Optional<String> in) {
         return in.or("");
      }

   };

}
