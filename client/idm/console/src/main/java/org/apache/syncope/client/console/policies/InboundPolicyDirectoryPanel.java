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
package org.apache.syncope.client.console.policies;

import java.util.List;
import org.apache.syncope.client.console.rest.PolicyRestClient;
import org.apache.syncope.client.console.wicket.markup.html.form.ActionLink;
import org.apache.syncope.client.console.wicket.markup.html.form.ActionsPanel;
import org.apache.syncope.common.lib.policy.InboundPolicyTO;
import org.apache.syncope.common.lib.types.IdRepoEntitlement;
import org.apache.syncope.common.lib.types.PolicyType;
import org.apache.wicket.PageReference;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.authroles.authorization.strategies.role.metadata.MetaDataRoleAuthorizationStrategy;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.StringResourceModel;

/**
 * Pull policies page.
 */
public class InboundPolicyDirectoryPanel extends PolicyDirectoryPanel<InboundPolicyTO> {

    private static final long serialVersionUID = 4984337552918213290L;

    public InboundPolicyDirectoryPanel(
            final String id, final PolicyRestClient restClient, final PageReference pageRef) {

        super(id, restClient, PolicyType.INBOUND, pageRef);

        this.addNewItemPanelBuilder(new PolicyModalPanelBuilder<>(
                PolicyType.INBOUND, new InboundPolicyTO(), modal, restClient, pageRef), true);
        MetaDataRoleAuthorizationStrategy.authorize(addAjaxLink, RENDER, IdRepoEntitlement.POLICY_CREATE);

        initResultTable();
    }

    @Override
    protected void addCustomColumnFields(final List<IColumn<InboundPolicyTO, String>> columns) {
        columns.add(new PropertyColumn<>(new StringResourceModel(
                "conflictResolutionAction", this), "conflictResolutionAction", "conflictResolutionAction"));
    }

    @Override
    protected void addCustomActions(final ActionsPanel<InboundPolicyTO> panel, final IModel<InboundPolicyTO> model) {
        panel.add(new ActionLink<>() {

            private static final long serialVersionUID = -3722207913631435501L;

            @Override
            public void onClick(final AjaxRequestTarget target, final InboundPolicyTO ignore) {
                target.add(policySpecModal.setContent(
                        new ProvisioningPolicyModalPanel(model.getObject(), policySpecModal, pageRef)));

                policySpecModal.header(new StringResourceModel(
                        "policy.rules", InboundPolicyDirectoryPanel.this, Model.of(model.getObject())));

                MetaDataRoleAuthorizationStrategy.authorize(
                        policySpecModal.getForm(), ENABLE, IdRepoEntitlement.POLICY_UPDATE);

                policySpecModal.show(true);
            }
        }, ActionLink.ActionType.COMPOSE, IdRepoEntitlement.POLICY_UPDATE);
    }
}
