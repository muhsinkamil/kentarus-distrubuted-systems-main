package com.kentarus.distributed_systems.services;

import java.util.ArrayList;

import com.kentarus.distributed_systems.constants.InstancesUrl;
import com.kentarus.distributed_systems.constants.NodeStatus;
import com.kentarus.distributed_systems.constants.ResponseConstants;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class NodeService {

    private WebClient webClient = WebClient.create();

    public ArrayList<Integer> disabledNodes = new ArrayList<>();

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
        int matchedNode = 0;

        if(!action.equals(NodeStatus.enableNode) && !action.equals(NodeStatus.disableNode)){
            return ResponseConstants.NOK;
        }

        for (int i = 0; i < instancesList.size(); i++) {
            Integer key = instancesList.get(i);
            String stringifiedKey = key.toString();
            if (stringifiedKey.equals(nodeId)) {
                matchedNode = key;
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

        if(action.equals(NodeStatus.disableNode) && result.equals(ResponseConstants.OK) && matchedNode > 0){
            disabledNodes.add(matchedNode);
        }

        if(action.equals(NodeStatus.enableNode) && result.equals(ResponseConstants.OK) && matchedNode > 0) {
            int toEnableNodeIndex = disabledNodes.indexOf(matchedNode);
            if(toEnableNodeIndex > -1) {
                disabledNodes.remove(toEnableNodeIndex);
            }
        }

        return result;
    }
}
