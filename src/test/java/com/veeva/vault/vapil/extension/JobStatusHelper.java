package com.veeva.vault.vapil.extension;

import com.veeva.vault.vapil.api.client.VaultClient;
import com.veeva.vault.vapil.api.model.response.JobStatusResponse;
import com.veeva.vault.vapil.api.request.JobRequest;

public class JobStatusHelper {

    public static boolean checkJobCompletion(VaultClient vaultClient, int jobId) {
//        	Check status of job
        String jobStatus = "";
        boolean jobCompleted = false;
        for (int i = 0; i < 42; i++) {
            if (jobStatus.equals("SUCCESS")) break;
            JobStatusResponse jobStatusResponse = vaultClient.newRequest(JobRequest.class)
                    .retrieveJobStatus(jobId);
            jobStatus = jobStatusResponse.getData().getStatus();
            switch (jobStatus) {
                case "SUCCESS":
                    jobCompleted = true;
                    break;
                case "QUEUED":
                case "QUEUING":
                case "RUNNING":
                    try {
                        Thread.sleep(11000);
                        break;
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
            }
        }
        return jobCompleted;
    }
}
