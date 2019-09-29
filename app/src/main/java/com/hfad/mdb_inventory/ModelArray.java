package com.hfad.mdb_inventory;

import java.util.ArrayList;

public class ModelArray {
    public static ArrayList<Model> models = new ArrayList<>();

    public static void addModel(Model model) {
        models.add(model);
    }

    public static Model get(int i) {
        return models.get(i);
    }
}
