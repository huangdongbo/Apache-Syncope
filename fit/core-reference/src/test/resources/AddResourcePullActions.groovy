/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
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
import groovy.transform.CompileStatic

import org.apache.syncope.core.provisioning.api.pushpull.InboundActions;
import org.apache.syncope.common.lib.request.AnyUR;
import org.apache.syncope.common.lib.to.EntityTO;
import org.apache.syncope.core.provisioning.api.job.JobExecutionException;
import org.apache.syncope.common.lib.request.UserUR;
import org.apache.syncope.common.lib.request.PasswordPatch;
import org.apache.syncope.common.lib.request.StringPatchItem;
import org.apache.syncope.core.provisioning.api.pushpull.ProvisioningProfile;
import org.apache.syncope.common.lib.types.PatchOperation;
import org.apache.syncope.common.lib.Attr;
import org.apache.syncope.common.lib.request.AttrPatch;
import org.identityconnectors.framework.common.objects.LiveSyncDelta;

/**
 * Class for integration tests: add new resource and put a password only for it.
 */
@CompileStatic
class AddResourcePullActions implements InboundActions {
  
  void beforeUpdate(
          final ProvisioningProfile<?, ?> profile,
          final LiveSyncDelta delta,
          final EntityTO entity,
          final AnyUR anyUR) throws JobExecutionException {

    if (anyUR instanceof UserUR) {
      UserUR userUR = (UserUR) anyUR;
      Attr attr = new Attr();
      attr.setSchema("surname");
      attr.getValues().add("surname2");
      AttrPatch attrPatch = new AttrPatch();
      attrPatch.setAttr(attr);
      attrPatch.setOperation(PatchOperation.ADD_REPLACE);
      userUR.getPlainAttrs().add(attrPatch);

      PasswordPatch patch = new PasswordPatch();
      patch.setOnSyncope(false);
      patch.setValue("Password123");
      patch.setOperation(PatchOperation.ADD_REPLACE);
      patch.getResources().add("resource-testdb2");
      userUR.setPassword(patch);

      StringPatchItem resPatchItem = new StringPatchItem();
      resPatchItem.setValue("resource-testdb2");
      resPatchItem.setOperation(PatchOperation.ADD_REPLACE);
      userUR.getResources().add(resPatchItem);
    }
  }
}