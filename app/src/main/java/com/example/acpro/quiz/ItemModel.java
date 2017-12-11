package com.example.acpro.quiz;

import java.util.Comparator;

public class ItemModel {
    private String name;
    private int score;

    public ItemModel() {
    }

    public ItemModel(String name, int score) {
        this.name = name;
        this.score = score;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public static final Comparator<ItemModel> COMPARE_BY_SCORE = new Comparator<ItemModel>() {
        @Override
        public int compare(ItemModel itemModel, ItemModel t1) {
            return itemModel.getScore() - t1.getScore();
        }
    };
}
