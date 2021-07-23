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
import com.veeva.vault.vapil.api.model.response.VaultResponse;

import java.util.List;

/**
 * Response model for the following API calls:
 * <p>
 * GET /api/{version}/services/queues
 * <br>
 * GET /api/{version}/services/queues/{queue_name}
 */
public class QueueResponse extends VaultResponse {

	@JsonProperty("data")
	public List<Queue> getData() {
		return (List<Queue>) this.get("data");
	}

	public void setData(List<Queue> data) {
		this.set("data", data);
	}

	public static class Queue extends VaultModel {

		@JsonProperty("connections")
		public List<Queue.Connection> getConnections() {
			return (List<Queue.Connection>) this.get("connections");
		}

		public void setConnections(List<Queue.Connection> connections) {
			this.set("connections", connections);
		}

		@JsonProperty("delivery")
		public String getDelivery() {
			return this.getString("delivery");
		}

		public void setDelivery(String delivery) {
			this.set("delivery", delivery);
		}

		@JsonProperty("messages_in_queue")
		public Integer getMessagesInQueue() {
			return this.getInteger("messages_in_queue");
		}

		public void setMessagesInQueue(Integer messagesInQueue) {
			this.set("messages_in_queue", messagesInQueue);
		}

		@JsonProperty("name")
		public String getName() {
			return this.getString("name");
		}

		public void setName(String name) {
			this.set("name", name);
		}

		@JsonProperty("status")
		public String getStatus() {
			return this.getString("status");
		}

		public void setStatus(String status) {
			this.set("status", status);
		}

		@JsonProperty("type")
		public String getType() {
			return this.getString("type");
		}

		public void setType(String type) {
			this.set("type", type);
		}

		@JsonProperty("url")
		public String getUrl() {
			return this.getString("url");
		}

		public void setUrl(String url) {
			this.set("url", url);
		}

		public static class Connection extends VaultModel {

			@JsonProperty("last_message_delivered")
			public String getLastMessageDelivered() {
				return this.getString("last_message_delivered");
			}

			public void setLastMessageDelivered(String lastMessageDelivered) {
				this.set("last_message_delivered", lastMessageDelivered);
			}

			@JsonProperty("error")
			public QueueError getError() {
				return (QueueError) this.get("error");
			}

			public void setError(Queue.Connection.QueueError error) {
				this.set("error", error);
			}

			@JsonProperty("name")
			public String getName() {
				return this.getString("name");
			}

			public void setName(String name) {
				this.set("name", name);
			}

			public static class QueueError extends VaultModel {
				@JsonProperty("date_time")
				public String getDateTime() {
					return this.getString("date_time");
				}

				public void setDateTime(String dateTime) {
					this.set("date_time", dateTime);
				}

				@JsonProperty("message")
				public String getMessage() {
					return this.getString("message");
				}

				public void setMessage(String message) {
					this.set("message", message);
				}

				@JsonProperty("message_id")
				public String getMessageId() {
					return this.getString("message_id");
				}

				public void setMessageId(String messageId) {
					this.set("message_id", messageId);
				}
			}
		}
	}
}