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
package org.apache.syncope.common.lib.to;

import jakarta.ws.rs.Path;

public class RelationshipTypeTO implements EntityTO {

    private static final long serialVersionUID = -1884088415277925817L;

    private String key;

    private String description;

    private String leftEndAnyType;

    private String rightEndAnyType;

    @Override
    public String getKey() {
        return key;
    }

    @Path("{key}")
    @Override
    public void setKey(final String key) {
        this.key = key;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public String getLeftEndAnyType() {
        return leftEndAnyType;
    }

    public void setLeftEndAnyType(final String leftEndAnyType) {
        this.leftEndAnyType = leftEndAnyType;
    }

    public String getRightEndAnyType() {
        return rightEndAnyType;
    }

    public void setRightEndAnyType(final String rightEndAnyType) {
        this.rightEndAnyType = rightEndAnyType;
    }
}
