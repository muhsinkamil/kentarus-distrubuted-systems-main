package com.kentarus.distributed_systems.structures;

import java.util.ArrayList;

public class PostWordsRequestStructure {
    private ArrayList<String> words = new ArrayList<>();
    private int startIndex = 0;

    public PostWordsRequestStructure() {
    }

    public PostWordsRequestStructure(ArrayList<String> words, int startIndex) {
        this.setWords(words);
        this.setStartIndex(startIndex);
    }

    public int getStartIndex() {
        return startIndex;
    }

    public void setStartIndex(int startIndex) {
        this.startIndex = startIndex;
    }

    public ArrayList<String> getWords() {
        return words;
    }

    public void setWords(ArrayList<String> words) {
        this.words = words;
    }
}
