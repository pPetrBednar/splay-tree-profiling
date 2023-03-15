package io.github.ppetrbednar.stp.logic;

import com.github.cliftonlabs.json_simple.JsonArray;
import com.github.cliftonlabs.json_simple.JsonObject;

import java.util.ArrayList;

public record ExperimentData(ArrayList<Integer> data, Integer depth) {
    public JsonObject toJsonObject() {
        JsonObject output = new JsonObject();
        output.put("depth", depth);
        output.put("data", new JsonArray(data));
        return output;
    }
}
