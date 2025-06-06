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
package org.apache.syncope.fit.core.reference;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.json.JsonMapper;
import java.io.IOException;
import java.util.Optional;
import org.apache.syncope.common.lib.to.OrgUnit;
import org.apache.syncope.common.lib.to.Provision;
import org.apache.syncope.core.provisioning.api.pushpull.LiveSyncDeltaMapper;
import org.identityconnectors.framework.common.objects.AttributeBuilder;
import org.identityconnectors.framework.common.objects.ConnectorObjectBuilder;
import org.identityconnectors.framework.common.objects.LiveSyncDelta;
import org.identityconnectors.framework.common.objects.ObjectClass;
import org.identityconnectors.framework.common.objects.SyncDelta;
import org.identityconnectors.framework.common.objects.SyncDeltaBuilder;
import org.identityconnectors.framework.common.objects.SyncDeltaType;
import org.identityconnectors.framework.common.objects.SyncToken;
import org.identityconnectors.framework.common.objects.Uid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

public class TestLiveSyncDeltaMapper implements LiveSyncDeltaMapper {

    private static final Logger LOG = LoggerFactory.getLogger(TestLiveSyncDeltaMapper.class);

    private static final JsonMapper JSON_MAPPER = JsonMapper.builder().findAndAddModules().build();

    @Override
    public SyncDelta map(final LiveSyncDelta liveSyncDelta, final OrgUnit orgUnit) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public SyncDelta map(final LiveSyncDelta liveSyncDelta, final Provision provision) {
        if (!provision.getObjectClass().equals(liveSyncDelta.getObjectClass().getObjectClassValue())) {
            throw new IllegalArgumentException("Expected " + provision.getObjectClass()
                    + ", got " + liveSyncDelta.getObjectClass().getObjectClassValue());
        }

        long timestamp = Optional.ofNullable(liveSyncDelta.getObject().getAttributeByName("record.timestamp")).
                filter(attr -> !CollectionUtils.isEmpty(attr.getValue())).
                map(attr -> (Long) attr.getValue().getFirst()).
                orElseThrow(() -> new IllegalArgumentException("No record.timestamp attribute found"));
        String value = Optional.ofNullable(liveSyncDelta.getObject().getAttributeByName("record.value")).
                filter(attr -> !CollectionUtils.isEmpty(attr.getValue())).
                map(attr -> attr.getValue().getFirst().toString()).
                orElseThrow(() -> new IllegalArgumentException("No record.value attribute found"));
        LOG.debug("Received: timestamp {} / value {}", timestamp, value);

        JsonNode tree;
        try {
            tree = JSON_MAPPER.readTree(value);
        } catch (IOException e) {
            throw new IllegalStateException("Could not parse the received value as JSON", e);
        }

        SyncDeltaBuilder builder = new SyncDeltaBuilder().
                setToken(new SyncToken(timestamp)).
                setDeltaType(SyncDeltaType.valueOf(tree.get("type").asText()));
        if (ObjectClass.ACCOUNT.equals(liveSyncDelta.getObjectClass())) {
            Uid uid = new Uid(tree.get("username").asText());
            builder.setObject(new ConnectorObjectBuilder().
                    setObjectClass(liveSyncDelta.getObjectClass()).
                    setUid(uid).
                    setName(uid.getUidValue()).
                    addAttribute(AttributeBuilder.build("email", tree.get("email").asText())).
                    addAttribute(AttributeBuilder.build("givenName", tree.get("givenName").asText())).
                    addAttribute(AttributeBuilder.build("lastName", tree.get("lastName").asText())).
                    addAttribute(AttributeBuilder.build(
                            "fullname",
                            tree.get("givenName").asText() + " " + tree.get("lastName").asText())).
                    build());
        } else if (ObjectClass.GROUP.equals(liveSyncDelta.getObjectClass())) {
            Uid uid = new Uid(tree.get("name").asText());
            builder.setObject(new ConnectorObjectBuilder().
                    setObjectClass(liveSyncDelta.getObjectClass()).
                    setUid(uid).
                    setName(uid.getUidValue()).
                    build());
        } else {
            throw new IllegalArgumentException("Unsupported ObjectClass: " + liveSyncDelta.getObjectClass());
        }

        return builder.build();
    }
}
