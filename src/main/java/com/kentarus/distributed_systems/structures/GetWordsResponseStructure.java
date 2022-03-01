package com.kentarus.distributed_systems.structures;

import java.util.ArrayList;

public class GetWordsResponseStructure {
    private ArrayList<String> words = new ArrayList<>();
    private String status;

    public GetWordsResponseStructure() {
    }

    public GetWordsResponseStructure(String status, ArrayList<String> words) {
        this.status = status;
        this.words = words;
    }

    public String getStatus() {
        return status;
    }

    public ArrayList<String> getWords() {
        return words;
    }

    public void setWords(ArrayList<String> words) {
        this.words = words;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
