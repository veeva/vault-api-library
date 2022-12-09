/*---------------------------------------------------------------------
 *	Copyright (c) 2021 Veeva Systems Inc.  All Rights Reserved.
 *	This code is based on pre-existing content developed and
 *	owned by Veeva Systems Inc. and may only be used in connection
 *	with the deliverable with which it was provided to Customer.
 *---------------------------------------------------------------------
 */
/*---------------------------------------------------------------------
 *	Copyright (c) 2021 Veeva Systems Inc.  All Rights Reserved.
 *	This code is based on pre-existing content developed and
 *	owned by Veeva Systems Inc. and may only be used in connection
 *	with the deliverable with which it was provided to Customer.
 *---------------------------------------------------------------------
 */
package com.veeva.vault.vapil.api.model.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.veeva.vault.vapil.api.model.VaultModel;

/**
 * Model for the following API calls responses:
 * <p>
 * POST /api/{version}/objects/objectworkflows/actions/{workflow_name}
 */
public class ObjectMultiRecordWorkflowInitiateResponse extends VaultResponse {

    @JsonProperty("data")
    public MultiRecordWorkflowData getData() {
        return (MultiRecordWorkflowData) this.get("data");
    }

    public void setData(MultiRecordWorkflowData data) {
        this.set("data", data);
    }

    public static class MultiRecordWorkflowData extends VaultModel {

        @JsonProperty("record_id__v")
        public String getRecordId() {
            return this.getString("record_id__v");
        }

        public void setRecordId(String recordId) {
            this.set("record_id__v", recordId);
        }

        @JsonProperty("workflow_id")
        public String getWorkflowId() {
            return this.getString("workflow_id");
        }

        public void setWorkflowId(String workflowId) {
            this.set("workflow_id", workflowId);
        }
    }
}