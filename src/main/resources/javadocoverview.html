<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<html lang="en">

<head>
	<title>VAPIL Introduction and Getting Started Guide</title>
</head>

<body>
<h1>VAPIL Introduction and Getting Started Guide</h1>

<h1>Introduction</h1>
The Vault API Library (VAPIL) is an open-source Java library for the Vault REST API.
VAPIL provides full API endpoint coverage for Vault integrations.
<p>
<p>
	The VAPIL release cycle follows the Vault API release cycle, with three general releases a year.
	Each VAPIL release aligns with the features of the Vault API release for consistency and coverage.
<ul>
	<li>Vault API version v21.1 is released, VAPIL version v21.1.0 is released</li>
	<li>When defects are fixed, VAPIL version v21.1.1, v21.1.2, etc will be released</li>
	<li>Vault API version v21.2 is released, VAPIL version v22.2.0 is released</li>
</ul>
<b>Consistent integration solution with Vault API requests/responses</b>
<ul>
	<li>One solution to develop and support</li>
	<li>Consistent development patterns that are similar the Vault Java SDK</li>
	<li>Full coverage of all platform API endpoints</li>
	<li>Expose API requests/responses in clearly defined Java classes, getters, setters, and methods</li>
</ul>
<b>Dedication to documentation, available in <a
		href="https://veeva.github.io/vault-api-library/javadoc/21.1.0/index.html" target="_blank">Javadoc</a></b>
<ul>
	<li>Easily find API endpoints from Vault help webpage in the source or Javadoc</li>
	<li>One-to-one alignment between VAPIL methods and the Vault API endpoint/documentation</li>
</ul>
<b>Developer friendly</b>
<ul>
	<li>Easy to start, quick to build</li>
	<li>Allow integration developer full control of design/implementation decisions, processing rules, exception
		handling</li>
</ul>
<b>Developer Ownership</b>
<p>
	VAPIL developers are responsible for:
<ul>
	<li>Following integration best practices with Vault and related systems</li>
	<li>Using bulk and batch processes whenever possible</li>
	<li>Respecting API authentication and burst limits</li>
	<li>Exception handling</li>
</ul>
<h1>Support and Issue Tracking</h1>
Enhancements, Issues, and documentation update requests can be reported via the <a
		href="https://support.veeva.com/hc/en-us/community/topics/360000476694-Vault-Developer" target="_blank">Vault Developer Community</a>
<h1>Source Code</h1>
Source code is available on GitHub: <a
		href="https://github.com/veeva/vault-api-library" target="_blank">Vault API Library</a>

<h1>Getting Started - Hello World</h1>
<ol>
	<li>Import VAPIL to a Maven project by adding the following to the project pom.xml file
		<pre style="border: 1px solid #C4CFE5; background-color: #FBFCFD;">
&lt;repositories&gt;
     &lt;repository&gt;
          &lt;id&gt;vapil&lt;/id&gt;
          &lt;url&gt;https://veeva.github.io/vault-api-library/maven&lt;/url&gt;
          &lt;releases&gt;
               &lt;enabled&gt;true&lt;/enabled&gt;
               &lt;updatePolicy&gt;never&lt;/updatePolicy&gt;
          &lt;/releases&gt;
     &lt;/repository&gt;
&lt;/repositories&gt;
&lt;dependencies&gt;
     &lt;dependency&gt;
          &lt;groupId&gt;com.veeva.vault&lt;/groupId&gt;
          &lt;artifactId&gt;vapil&lt;/artifactId&gt;
          &lt;version&gt;21.1.0&lt;/version&gt;
     &lt;/dependency&gt;
&lt;/dependencies&gt;</pre>
	</li>
	<li>Create a class "HelloWorld" and add the following code
		<pre style="border: 1px solid #C4CFE5; background-color: #FBFCFD;">
public class HelloWorld {
	public static void main(String[] args) {
		// Set the Vault Client Id, which is required for all API calls
		VaultClientId vaultClientId = new VaultClientId("verteobiotech","vault","quality",true,"myintegration");
		// Instantiate the VAPIL VaultClient using user name and password authentication
		VaultClient vaultClient = VaultClientBuilder
				.newClientBuilder(VaultClient.AuthenticationType.BASIC)
				.withVaultDNS("verteobiotech.veevavault.com")
				.withVaultUsername("username@verteobiotech.com")
				.withVaultPassword("password")
				.withVaultClientId(vaultClientId)
				.build();
		// Perform a VQL query and display the results
		QueryResponse resp = vaultClient.newRequest(QueryRequest.class)
				.query("SELECT name__v, email__sys FROM user__sys MAXROWS 3");
		if (resp != null) {
			System.out.println("-----------------------------------------");
			System.out.println("Response Status = " + resp.getResponseStatus());
			System.out.println("Total Records = " + resp.getData().size());
			for (QueryResponse.QueryResult row : resp.getData())
				System.out.println("Name = " + row.get("name__v") + ", Email = " + row.get("email__sys"));
		}
	}
}
		</pre>
	</li>
	<li>Run the code and verify the results
		<pre style="border: 1px solid #C4CFE5; background-color: #FBFCFD;">
-----------------------------------------
Response Status = SUCCESS
Total Records = 3
Name = System, Email = System
Name = Todd Taylor, Email = todd.taylor@verteobiotech.com
Name = Mark Arnold, Email = mark.arnold@verteobiotech.com
		</pre>
	</li>
	<li>Now that you have a starting point, check out the sample code and go build your interface!</li>
</ol>
<h1>Sample Code</h1>

<b>Instantiate the Vault Client with an Existing Session</b>
<pre style="border: 1px solid #C4CFE5; background-color: #FBFCFD;">
VaultClientId vaultClientId = new VaultClientId("verteobiotech","vault","quality",true,"myintegration");

String sessionId = "xxxxxxxxxxxxxxxxx";
VaultClient vaultClient = VaultClientBuilder
	.newClientBuilder(VaultClient.AuthenticationType.SESSION_ID)
	.withVaultDNS("verteobiotech.veevavault.com")
	.withVaultSessionId(sessionId)
	.withVaultClientId(vaultClientId)
	.build();
</pre>

<b>Create a Single Document</b>
<pre style="border: 1px solid #C4CFE5; background-color: #FBFCFD;">
Document doc = new Document();

doc.setName("VAPIL Single Document");
doc.setLifecycle("General Lifecycle");
doc.setType("General");
doc.setTitle("Test Upload VAPIL");
doc.set("custom_field__c", "value");

DocumentResponse response = vaultClient.newRequest(DocumentRequest.class)
	.setInputPath(filePath)
	.createSingleDocument(doc);
</pre>

<b>Bulk Create Multiple Documents from CSV File</b>
<pre style="border: 1px solid #C4CFE5; background-color: #FBFCFD;">
DocumentBulkResponse response = vaultClient.newRequest(DocumentRequest.class)
	.setInputPath(csvFilePath)
	.createMultipleDocuments();
</pre>

<b>Bulk Update Object Records (Input CSV, JSON Response)</b>
<pre style="border: 1px solid #C4CFE5; background-color: #FBFCFD;">
ObjectRecordResponse resp = vaultClient.newRequest(ObjectRecordRequest.class)
	.setContentTypeCsv()
	.setInputPath(localPath)
	.updateObjectRecords("product__v");
</pre>

<b>Retrieve the Audit Trail for Documents in past 30 days</b>
<pre style="border: 1px solid #C4CFE5; background-color: #FBFCFD;">
DocumentAuditResponse resp = vaultClient.newRequest(LogRequest.class)
		.setStartDate(ZonedDateTime.now(ZoneId.of("UTC")).minusDays(29))
		.setEndDate(ZonedDateTime.now(ZoneId.of("UTC")).minusDays(1))
		.setLimit(4)
		.retrieveAuditDetails(LogRequest.AuditTrailType.DOCUMENT);

AuditDetailsResponse.ResponseDetails details = resp.getResponseDetails();
System.out.println("Offset = " + details.getOffset());
System.out.println("Limit = " + details.getLimit());
System.out.println("Size = " + details.getSize());
System.out.println("Total = " + details.getTotal());
System.out.println("Object/Name = " + details.getDetailsObject().getName());
System.out.println("Object/Label = " + details.getDetailsObject().getLabel());
System.out.println("Object/Url = " + details.getDetailsObject().getUrl());

System.out.println("Items ****");
for (DocumentAuditResponse.DocumentAudit audit : resp.getData()) {
	System.out.println("\n**** Data Item **** ");
	System.out.println("id = " + audit.getId());
	System.out.println("timestamp = " + audit.getTimestamp());
	System.out.println("UserName = " + audit.getUserName());
	System.out.println("Full Name = " + audit.getFullName());
	System.out.println("Action = " + audit.getAction());
	System.out.println("Item = " + audit.getItem());
	System.out.println("Field Name = " + audit.getFieldName());
	System.out.println("Workflow Name = " + audit.getWorkflowName());
	System.out.println("Task Name = " + audit.getTaskName());
	System.out.println("Signature Meaning = " + audit.getSignatureMeaning());
	System.out.println("View License = " + audit.getViewLicense());
	System.out.println("Job Instance ID = " + audit.getJobInstanceId());
	System.out.println("Doc ID = " + audit.getDocId());
	System.out.println("Version = " + audit.getVersion());
	System.out.println("Document Url = " + audit.getDocumentUrl());
	System.out.println("Event Description = " + audit.getEventDescription());
}
</pre>

</body>

</html>