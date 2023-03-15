package io.github.ppetrbednar.stp.logic;

import com.github.cliftonlabs.json_simple.JsonArray;
import com.github.cliftonlabs.json_simple.JsonObject;
import com.github.cliftonlabs.json_simple.Jsoner;
import io.github.ppetrbednar.stp.logic.structures.SplayTree;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class STProfiler {
    public static final File PROFILING_FOLDER = new File("./experiment");
    public static final File PROFILING_DATA_FILE = new File("./experiment/data.json");
    public static final File PROFILING_STATISTICS_FILE = new File("./experiment/statistics.json");
    public static final int EXPERIMENTS = 10000;
    public static final int VALUES = 1023;
    public static final boolean SAVE_DATA = true;
    private final HashMap<Integer, ExperimentData> experimentData = new HashMap<>();

    public void profile() {

        for (int i = 1; i <= EXPERIMENTS; i++) {
            experiment(i);
        }

        checkDataFolder();
        try {
            saveStatistics();
            if (SAVE_DATA) {
                saveData();
            }
        } catch (IOException e) {
            System.out.println("Data saving failed.");
        }
    }

    private void checkDataFolder() {
        if (!PROFILING_FOLDER.exists()) {
            PROFILING_FOLDER.mkdir();
        }
    }

    private void saveStatistics() throws IOException {
        ArrayList<Integer> treeDepths = new ArrayList<>();
        experimentData.forEach((integer, data) -> treeDepths.add(data.depth()));

        int min = Math.min(treeDepths);
        int max = Math.max(treeDepths);
        double mean = Math.mean(treeDepths);
        List<Double> cumulativeMeans = Math.cumulativeMeans(treeDepths);
        double median = Math.median(treeDepths);
        List<Integer> modes = Math.getModes(treeDepths);

        JsonObject statistics = new JsonObject();
        statistics.put("experiments", EXPERIMENTS);
        statistics.put("values", VALUES);
        statistics.put("min", min);
        statistics.put("max", max);
        statistics.put("mean", mean);
        statistics.put("cumulative_means", new JsonArray(cumulativeMeans));
        statistics.put("median", median);
        statistics.put("modes", new JsonArray(modes));

        FileUtils.writeStringToFile(PROFILING_STATISTICS_FILE, Jsoner.prettyPrint(statistics.toJson()), StandardCharsets.UTF_8);
    }

    private void saveData() throws IOException {
        JsonArray experiments = new JsonArray();
        for (Integer id : experimentData.keySet()) {
            var data = experimentData.get(id);
            experiments.add(data.toJsonObject());
        }

        FileUtils.writeStringToFile(PROFILING_DATA_FILE, Jsoner.prettyPrint(experiments.toJson()), StandardCharsets.UTF_8);
    }

    private void experiment(int experimentId) {
        SplayTree<Integer, Integer> splayTree = new SplayTree<>();
        ArrayList<Integer> data = new ArrayList<>();
        for (int i = 1; i <= VALUES; i++) {
            data.add(i);
        }

        Collections.shuffle(data);

        for (Integer id : data) {
            splayTree.add(id, id);
        }

        experimentData.put(experimentId, new ExperimentData(data, splayTree.depth()));
    }
}
