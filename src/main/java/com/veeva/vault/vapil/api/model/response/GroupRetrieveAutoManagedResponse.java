package com.veeva.vault.vapil.api.model.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.veeva.vault.vapil.api.model.VaultModel;
import com.veeva.vault.vapil.api.model.common.Group;

import java.util.List;

public class GroupRetrieveAutoManagedResponse extends VaultResponse {

    @JsonProperty("data")
    public List<Data> getData() {return (List<Data>) this.get("data");}

    public void setData(List<Data> data) {this.set("data", data);}

    public static class Data extends VaultModel {

        @JsonProperty("group")
        public Group getGroup() {return (Group) this.get("group");}

        public void setGroup(Group group) {this.set("group", group);}


    }
}
