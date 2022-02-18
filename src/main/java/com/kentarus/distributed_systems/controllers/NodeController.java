package com.kentarus.distributed_systems.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class NodeController {

    @PatchMapping(value = "/node/{nodeId}")
    public String nodeMaster(@PathVariable String nodeId, @RequestParam("action") String action) {
        return "Hello" + nodeId + action;
    }

    @GetMapping(value = "/nodes/status")
    public String nodeHealthChecker(@RequestParam("type") String type) {
        return "OK" + type;
    }
}
