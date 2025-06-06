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
package org.apache.syncope.common.lib.types;

public enum OIDCGrantType {
    authorization_code("authorization_code"),
    password("password"),
    client_credentials("client_credentials"),
    refresh_token("refresh_token"),
    ciba("urn:openid:params:grant-type:ciba"),
    token_exchange("urn:ietf:params:oauth:grant-type:token-exchange"),
    device_code("urn:ietf:params:oauth:grant-type:device_code"),
    uma_ticket("urn:ietf:params:oauth:grant-type:uma-ticket");

    private final String externalForm;

    OIDCGrantType(final String external) {
        this.externalForm = external;
    }

    public String getExternalForm() {
        return externalForm;
    }
}
