/**
 *
 * Copyright (C) 2010 Cloud Conscious, LLC. <info@cloudconscious.com>
 *
 * ====================================================================
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
 * ====================================================================
 */

package org.jclouds.compute.domain;

import javax.annotation.Nullable;

import org.jclouds.compute.domain.internal.ImageImpl;
import org.jclouds.domain.Credentials;

import com.google.common.annotations.Beta;
import com.google.inject.ImplementedBy;

/**
 * Running Operating system
 * 
 * @author Adrian Cole
 */
@ImplementedBy(ImageImpl.class)
public interface Image extends ComputeMetadata {
   /**
    * The operating system installed on this image
    */
   @Beta
   OperatingSystem getOperatingSystem();

   /**
    * Version of the image
    */
   String getVersion();

   /**
    * Description of the image.
    */
   String getDescription();

   /**
    * please use {#link {@link #getOperatingSystem()}
    * 
    * @see OperatingSystem#getFamily()
    */
   @Deprecated
   OsFamily getOsFamily();

   /**
    * please use {#link {@link #getOperatingSystem()}
    * 
    * @see OperatingSystem#getDescription()
    */
   @Deprecated
   String getOsDescription();

   /**
    * please use {#link {@link #getOperatingSystem()}
    * 
    * @see OperatingSystem#getDescription()
    */
   @Nullable
   @Deprecated
   Architecture getArchitecture();

   /**
    * Default credentials for the current image
    */
   Credentials getDefaultCredentials();

}