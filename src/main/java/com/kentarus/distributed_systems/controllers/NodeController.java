package com.kentarus.distributed_systems.controllers;

import java.util.ArrayList;

import com.kentarus.distributed_systems.constants.ResponseConstants;
import com.kentarus.distributed_systems.services.NodeService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class NodeController {

    @Autowired
    NodeService nodeService;

    @PatchMapping(value = "/node/{nodeId}")
    public ResponseEntity<String> nodeMaster(@PathVariable String nodeId, @RequestParam("action") String action) {
        String result = nodeService.changeStatusOfNode(nodeId, action);
        if (result.equals(ResponseConstants.NOK)) {
            return ResponseEntity.internalServerError().body(ResponseConstants.NOK);
        }

        return ResponseEntity.ok().body(ResponseConstants.OK);
    }

    @GetMapping(value = "/nodes/status")
    public ArrayList<Integer> nodeHealthChecker(@RequestParam("type") String type) {
        return nodeService.getStatusOfNodes(type);
    }
}
