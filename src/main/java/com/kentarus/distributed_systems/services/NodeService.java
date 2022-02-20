package com.kentarus.distributed_systems.services;

import java.util.ArrayList;

import com.kentarus.distributed_systems.constants.InstancesUrl;
import com.kentarus.distributed_systems.constants.ResponseConstants;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class NodeService {

    private WebClient webClient = WebClient.create();

    public ArrayList<Integer> getStatusOfNodes(String type) {
        ArrayList<Integer> result = new ArrayList<>();

        // If the node is enabled, it returns 1 else -1
        int checkCondition = type.equals("active") ? 1 : -1;

        for (Integer key : InstancesUrl.instances.keySet()) {
            int status = webClient
                    .get()
                    .uri(InstancesUrl.instances.get(key) + "/nodes/status")
                    .retrieve()
                    .bodyToMono(Integer.class)
                    .block();

            if (status == checkCondition) {
                result.add(key);
            }
        }
        return result;
    }

    public String changeStatusOfNode(String nodeId, String action) {
        ArrayList<Integer> instancesList = new ArrayList<>(InstancesUrl.instances.keySet());
        String instanceUrl = "";

        for (int i = 0; i < instancesList.size(); i++) {
            Integer key = instancesList.get(i);
            String stringifiedKey = key.toString();
            if (stringifiedKey.equals(nodeId)) {
                instanceUrl = InstancesUrl.instances.get(key);
            }
        }

        // If the nodeId is invalid
        if (instanceUrl.isEmpty()) {
            return ResponseConstants.NOK;
        }

        String result = webClient
                .patch()
                .uri(instanceUrl + "/node?action=" + action)
                .retrieve()
                .bodyToMono(String.class)
                .block();

        return result;
    }
}
