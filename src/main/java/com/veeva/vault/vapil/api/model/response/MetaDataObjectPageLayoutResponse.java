package com.veeva.vault.vapil.api.model.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.veeva.vault.vapil.api.model.metadata.VaultObjectPageLayout;

import java.util.List;

/**
 * Response model for the following API calls:
 * <p>
 * /api/{version}/metadata/vobjects/{object_name}/page_layouts
 * /api/{version}/metadata/vobjects/{object_name}/page_layouts/{layout_name}
 */
public class MetaDataObjectPageLayoutResponse extends VaultResponse {

    @JsonProperty("data")
    public List<VaultObjectPageLayout> getData() { return (List<VaultObjectPageLayout>) this.get("data"); }

    public void setData(List<VaultObjectPageLayout> data) { this.set("data", data); }

}
