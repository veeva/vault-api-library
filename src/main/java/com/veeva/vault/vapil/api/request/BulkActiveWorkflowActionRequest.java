package com.veeva.vault.vapil.api.request;


import com.veeva.vault.vapil.api.model.response.BulkWorkflowActionDetailsResponse;
import com.veeva.vault.vapil.api.model.response.BulkWorkflowActionsResponse;
import com.veeva.vault.vapil.api.model.response.BulkWorkflowJobStartResponse;
import com.veeva.vault.vapil.connector.HttpRequestConnector;

import java.util.List;
import java.util.stream.Collectors;

/**
 * The Bulk Active Workflow APIs retrieve information about actions and action details and to execute actions.
 *
 * @vapil.apicoverage <a href="https://developer.veevavault.com/api/23.1/#bulk-active-workflow-actions">https://developer.veevavault.com/api/23.1/#bulk-active-workflow-actions</a>
 */
public class BulkActiveWorkflowActionRequest extends VaultRequest {

	// API Endpoints
	private static final String URL_BULK_WORKFLOW_ACTIONS = "/object/workflow/actions";
	private static final String URL_BULK_WORKFLOW_ACTION_DETAILS = "/object/workflow/actions/{action}";
	private static final String URL_INITIATE_BULK_WORKFLOW_ACTION = URL_BULK_WORKFLOW_ACTION_DETAILS;

	private List<Integer> taskIds;
	private List<Integer> userIds;

	BulkActiveWorkflowActionRequest() {
	}

	/**
	 * <b>Cancel Workflow Tasks</b> <br>
	 *
	 * @return BulkWorkflowJobStartResponse
	 * @vapil.api <pre>
	 * POST /api/{version}/object/workflow/actions/canceltasks</pre>
	 * @vapil.vaultlink <a href='https://developer.veevavault.com/api/23.1/#initiate-workflow-actions-on-multiple-workflows' target='_blank'>https://developer.veevavault.com/api/23.1/#initiate-workflow-actions-on-multiple-workflows</a>
	 * @vapil.request <pre>
	 * <i>Example 1 - Cancel By Task Ids</i>
	 * List&lt;Integer&gt; ids = Arrays.asList(new Integer[]{1000,1001});
	 * BulkWorkflowJobStartResponse response = vaultClient.newRequest(BulkActiveWorkflowActionRequest.class)
	 * 				.setTaskIds(ids)
	 * 				.cancelWorkflowTasks();</pre>
	 * @vapil.request <pre>
	 * <i>Example 2 - Cancel By User Ids</i>
	 * List&lt;Integer&gt; ids = Arrays.asList(new Integer[]{1000,1001});
	 * BulkWorkflowJobStartResponse response = vaultClient.newRequest(BulkActiveWorkflowActionRequest.class)
	 * 				.setUserIds(ids)
	 * 				.cancelWorkflowTasks();</pre>
	 * @vapil.response <pre>
	 * if (response.isSuccessful() {
	 *   if (response.getData()) {
	 *     System.out.println("JobId = " + response.getData().getJobId());
	 *   }
	 * }
	 * </pre>
	 */
	public BulkWorkflowJobStartResponse cancelWorkflowTasks() {
		String url = vaultClient.getAPIEndpoint(URL_INITIATE_BULK_WORKFLOW_ACTION)
				.replace("{action}", "canceltasks");
		HttpRequestConnector request = new HttpRequestConnector(url);
		request.addHeaderParam(HttpRequestConnector.HTTP_HEADER_ACCEPT, HttpRequestConnector.HTTP_CONTENT_TYPE_JSON);

		if (taskIds != null) {
			request.addBodyParam("task_ids", taskIds.stream().map(String::valueOf).collect(Collectors.joining(",")));
		}

		if (userIds != null) {
			request.addBodyParam("user_ids", userIds.stream().map(String::valueOf).collect(Collectors.joining(",")));
		}

		return send(HttpRequestConnector.HttpMethod.POST, request, BulkWorkflowJobStartResponse.class);
	}


	/**
	 * <b>Cancel Active Workflows</b> <br>
	 *
	 * @param workflowIds workflow_id__v field values. Maximum 500 workflows.
	 * @return BulkWorkflowJobStartResponse
	 * @vapil.api <pre>
	 * POST /api/{version}/object/workflow/actions/cancelworkflows</pre>
	 * @vapil.vaultlink <a href='https://developer.veevavault.com/api/23.1/#initiate-workflow-actions-on-multiple-workflows' target='_blank'>https://developer.veevavault.com/api/23.1/#initiate-workflow-actions-on-multiple-workflows</a>
	 * @vapil.request <pre>
	 * List&lt;String&gt; ids = Arrays.asList(new String[]{"1","2","3"});
	 * BulkWorkflowJobStartResponse response = vaultClient.newRequest(BulkActiveWorkflowActionRequest.class)
	 * 				.cancelWorkflows(ids);</pre>
	 * @vapil.response <pre>
	 * if (response.isSuccessful() {
	 *   if (response.getData()) {
	 *     System.out.println("JobId = " + response.getData().getJobId());
	 *   }
	 * }
	 * </pre>
	 */
	public BulkWorkflowJobStartResponse cancelWorkflows(List<Integer> workflowIds) {
		String url = vaultClient.getAPIEndpoint(URL_INITIATE_BULK_WORKFLOW_ACTION)
				.replace("{action}", "cancelworkflows");
		HttpRequestConnector request = new HttpRequestConnector(url);
		request.addHeaderParam(HttpRequestConnector.HTTP_HEADER_ACCEPT, HttpRequestConnector.HTTP_CONTENT_TYPE_JSON);
		request.addBodyParam("workflow_ids", workflowIds.stream().map(String::valueOf).collect(Collectors.joining(",")));
		return send(HttpRequestConnector.HttpMethod.POST, request, BulkWorkflowJobStartResponse.class);
	}

	/**
	 * <b>Reassign Workflow Tasks</b> <br>
	 *
	 * Note that this action can reassign a maximum of 500 tasks.
	 *
	 * @param currentTaskAssignee Input the user ID of the user whose tasks you wish to reassign. You cannot reassign specific tasks for a user, only all tasks currently assigned to a user.
	 * @param newTaskAssignee Input the user ID of the user who will receive the newly assigned tasks.
	 * @return BulkWorkflowJobStartResponse
	 * @vapil.api <pre>
	 * POST /api/{version}/object/workflow/actions/reassigntasks</pre>
	 * @vapil.vaultlink <a href='https://developer.veevavault.com/api/23.1/#initiate-workflow-actions-on-multiple-workflows' target='_blank'>https://developer.veevavault.com/api/23.1/#initiate-workflow-actions-on-multiple-workflows</a>
	 * @vapil.request <pre>
	 * BulkWorkflowJobStartResponse response = vaultClient.newRequest(BulkActiveWorkflowActionRequest.class)
	 * 				.reassignTasks(12345, 67890);</pre>
	 * @vapil.response <pre>
	 * if (response.isSuccessful() {
	 *   if (response.getData()) {
	 *     System.out.println("JobId = " + response.getData().getJobId());
	 *   }
	 * }
	 * </pre>
	 */
	public BulkWorkflowJobStartResponse reassignTasks(Integer currentTaskAssignee, Integer newTaskAssignee) {
		String url = vaultClient.getAPIEndpoint(URL_INITIATE_BULK_WORKFLOW_ACTION)
				.replace("{action}", "reassigntasks");
		HttpRequestConnector request = new HttpRequestConnector(url);
		request.addHeaderParam(HttpRequestConnector.HTTP_HEADER_ACCEPT, HttpRequestConnector.HTTP_CONTENT_TYPE_JSON);
		request.addBodyParam("current_task_assignee", String.valueOf(currentTaskAssignee));
		request.addBodyParam("new_task_assignee", String.valueOf(newTaskAssignee));

		return send(HttpRequestConnector.HttpMethod.POST, request, BulkWorkflowJobStartResponse.class);
	}

	/**
	 * <b>Replace Workflow Owner</b> <br>
	 *
	 * @param currentWorkflowOwner Input the ID of the user who will become the current workflow owner.
	 * @param newWorkflowOwner Input the ID of the user who will become the new workflow owner.
	 * @return BulkWorkflowJobStartResponse
	 * @vapil.api <pre>
	 * POST /api/{version}/object/workflow/actions/replaceworkflowowner</pre>
	 * @vapil.vaultlink <a href='https://developer.veevavault.com/api/23.1/#initiate-workflow-actions-on-multiple-workflows' target='_blank'>https://developer.veevavault.com/api/23.1/#initiate-workflow-actions-on-multiple-workflows</a>
	 * @vapil.request <pre>
	 * BulkWorkflowJobStartResponse response = vaultClient.newRequest(BulkActiveWorkflowActionRequest.class)
	 * 				.replaceWorkflowOwner(12345, 67890);</pre>
	 * @vapil.response <pre>
	 * if (response.isSuccessful() {
	 *   if (response.getData()) {
	 *     System.out.println("JobId = " + response.getData().getJobId());
	 *   }
	 * }
	 * </pre>
	 */
	public BulkWorkflowJobStartResponse replaceWorkflowOwner(Integer currentWorkflowOwner, Integer newWorkflowOwner) {
		String url = vaultClient.getAPIEndpoint(URL_INITIATE_BULK_WORKFLOW_ACTION)
				.replace("{action}", "replaceworkflowowner");
		HttpRequestConnector request = new HttpRequestConnector(url);
		request.addHeaderParam(HttpRequestConnector.HTTP_HEADER_ACCEPT, HttpRequestConnector.HTTP_CONTENT_TYPE_JSON);
		request.addBodyParam("current_workflow_owner", String.valueOf(currentWorkflowOwner));
		request.addBodyParam("new_workflow_owner", String.valueOf(newWorkflowOwner));
		return send(HttpRequestConnector.HttpMethod.POST, request, BulkWorkflowJobStartResponse.class);
	}

	/**
	 * <b>Retrieve Cancel Workflow Action Details</b> <br>
	 *
	 * @param action The name of the workflow action
	 * @return BulkWorkflowActionDetailsResponse
	 * @vapil.api <pre>
	 * GET /api/{version}/object/workflow/actions/{action}</pre>
	 * @vapil.vaultlink <a href='https://developer.veevavault.com/api/23.1/#retrieve-bulk-workflow-actions-details' target='_blank'>https://developer.veevavault.com/api/23.1/#retrieve-bulk-workflow-actions-details</a>
	 * @vapil.request <pre>
	 * BulkWorkflowActionDetailsResponse resp = vaultClient.newRequest(BulkActiveWorkflowActionRequest.class)
	 * 				.retrieveBulkWorkflowActionDetails();</pre>
	 * @vapil.response <pre>
	 * if (resp.isSuccessful() {
	 *   if (resp.getDetails() != null) {
	 *     System.out.println("Name " + resp.getName() +"\n");
	 *     if resp.getDetails().getControls() != null &amp;&amp; !resp.getDetails().getControls().isEmpty()) {
	 *       System.out.println("Controls Exist\n");
	 *
	 *       for (Control control : resp.getControls()) {
	 *         System.out.println("  ------------------------------");
	 *         System.out.println("  Type " + control.getType());
	 *         System.out.println("  Label " + action.getLabel());
	 *
	 *         if control.getPrompts() != null &amp;&amp; !control.getPrompts().isEmpty()) {
	 *           System.out.println("  Prompts Exist");
	 *           System.out.println("    ------------------------------");
	 *           for (Prompt prompt : control.getPrompts()) {
	 *               System.out.println("    Name " + prompt.getName());
	 *               System.out.println("    Label " + prompt.getLabel());
	 *               System.out.println("    Required " + prompt.getRequired());
	 *               System.out.println("    Multi-Value " + prompt.getMultiValue());
	 *           }
	 *         } else {
	 *             System.out.println("  No Prompts Exist");
	 *         }
	 *       } else {
	 *         System.out.println("No Controls Exist\n");
	 *       }
	 *     }
	 *   }
	 * }
	 *   </pre>
	 */
	public BulkWorkflowActionDetailsResponse retrieveBulkWorkflowActionDetails(String action) {
		String url = vaultClient.getAPIEndpoint(URL_BULK_WORKFLOW_ACTION_DETAILS)
				.replace("{action}", action);
		HttpRequestConnector request = new HttpRequestConnector(url);
		request.addHeaderParam(HttpRequestConnector.HTTP_HEADER_ACCEPT, HttpRequestConnector.HTTP_CONTENT_TYPE_JSON);
		return send(HttpRequestConnector.HttpMethod.GET, request, BulkWorkflowActionDetailsResponse.class);
	}

	/**
	 * <b>Retrieve Bulk Workflow Actions</b> <br>
	 * Retrieve All available workflow actions that can be initiated on a workflow which:
	 * <ul>
	 *     <li>The authenticated user has permissions to view or initiate</li>
	 *     <li>Can be initiated through the API</li>
	 * </ul>
	 *
	 * @return BulkWorkflowActionsResponse
	 * @vapil.api <pre>
	 * GET /api/{version}/object/workflow/actions</pre>
	 * @vapil.vaultlink <a href='https://developer.veevavault.com/api/23.1/#retrieve-bulk-workflow-actions' target='_blank'>https://developer.veevavault.com/api/23.1/#retrieve-bulk-workflow-actions</a>
	 * @vapil.request <pre>
	 * BulkWorkflowActionsResponse resp = vaultClient.newRequest(BulkActiveWorkflowActionRequest.class)
	 * 				.retrieveBulkWorkflowActions();</pre>
	 * @vapil.response <pre>
	 * if (resp.isSuccessful() &amp;&amp; resp.getActionsItems() != null &amp;&amp; !resp.getActionsItems().isEmpty()) {
	 *   System.out.println("Actions Exist\n");
	 *
	 *   for (ActionItem action : resp.getActionsItems()) {
	 *     System.out.println("------------------------------");
	 *     System.out.println("Name " + action.getName());
	 *     System.out.println("Label " + action.getLabel());
	 *     }
	 * } else {
	 *   System.out.println("No Actions Exist\n");
	 * }</pre>
	 */
	public BulkWorkflowActionsResponse retrieveBulkWorkflowActions() {
		HttpRequestConnector request = new HttpRequestConnector(vaultClient.getAPIEndpoint(URL_BULK_WORKFLOW_ACTIONS));
		request.addHeaderParam(HttpRequestConnector.HTTP_HEADER_ACCEPT, HttpRequestConnector.HTTP_CONTENT_TYPE_JSON);
		return send(HttpRequestConnector.HttpMethod.GET, request, BulkWorkflowActionsResponse.class);
	}

	/**
	 *
	 *
	 * @param taskIds list of task ids
	 * @return BulkActiveWorkflowActionRequest
	 */
	public BulkActiveWorkflowActionRequest setTaskIds(List<Integer> taskIds) {
		this.taskIds = taskIds;
		return this;
	}

	/**
	 *
	 *
	 * @param userIds list of user ids
	 * @return BulkActiveWorkflowActionRequest
	 */
	public BulkActiveWorkflowActionRequest setUserIds(List<Integer> userIds) {
		this.userIds = userIds;
		return this;
	}
}
