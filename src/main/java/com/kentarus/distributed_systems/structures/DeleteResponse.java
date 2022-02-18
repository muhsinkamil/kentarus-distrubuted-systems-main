package com.kentarus.distributed_systems.structures;

public class DeleteResponse {
    private String status;

    public DeleteResponse(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
