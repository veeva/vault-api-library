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

import java.math.BigDecimal;
import java.util.List;

/**
 * Response model for the following API calls:
 * <p>
 * POST /api/mdl/execute_async
 */
public class MdlExecuteAsyncResponse extends VaultResponse {
    @JsonProperty("script_execution")
    public ScriptExecution getScriptExecution() {
        return (ScriptExecution) this.get("script_execution");
    }

    public void setScriptExecution(ScriptExecution scriptExecution) {
        this.set("script_execution", scriptExecution);
    }

    @JsonProperty("job_id")
    public Integer getJobId() {
        return this.getInteger("job_id");
    }
    public void setJobId(Integer jobId) {
        this.set("job_id", jobId);
    }

    @JsonProperty("url")
    public String getUrl() {
        return this.getString("url");
    }
    public void setUrl(String url) {
        this.set("url", url);
    }


    public static class ScriptExecution extends VaultModel {
        @JsonProperty("code")
        public String getCode() {
            return this.getString("code");
        }

        public void setCode(String code) {
            this.set("code", code);
        }

        @JsonProperty("message")
        public String getMessage() {
            return this.getString("message");
        }

        public void setMessage(String message) {
            this.set("message", message);
        }

        @JsonProperty("warnings")
        public Integer getWarnings() {
            return this.getInteger("warnings");
        }

        public void setWarnings(Integer warnings) {
            this.set("warnings", warnings);
        }

        @JsonProperty("failures")
        public Integer getFailures() {
            return this.getInteger("failures");
        }

        public void setFailures(Integer failures) {
            this.set("failures", failures);
        }

        @JsonProperty("exceptions")
        public Integer getExceptions() {
            return this.getInteger("exceptions");
        }

        public void setExceptions(Integer exceptions) {
            this.set("exceptions", exceptions);
        }

        @JsonProperty("components_affected")
        public Integer getComponentsAffected() {
			return this.getInteger("components_affected");
		}

        public void setComponentsAffected(Integer componentsAffected) {
            this.set("components_affected", componentsAffected);
        }

        @JsonProperty("execution_time")
        public BigDecimal getExecutionTime() {
            return this.getBigDecimal("execution_time");
        }

        public void setExecutionTime(BigDecimal executionTime) {
            this.set("execution_time", executionTime);
        }
    }
}