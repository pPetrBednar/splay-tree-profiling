package io.github.ppetrbednar.stp.logic;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public final class Math {
    private Math() {
    }

    public static int min(final List<Integer> values) {
        return Collections.min(values);
    }

    public static int max(final List<Integer> values) {
        return Collections.max(values);
    }

    public static double mean(final List<Integer> values) {

        if (values == null || values.isEmpty()) {
            return 0;
        }

        double sum = 0;
        for (Integer value : values) {
            sum += value;
        }
        return sum / values.size();
    }

    public static double median(final List<Integer> values) {

        if (values == null || values.isEmpty()) {
            return 0;
        }

        List<Integer> valuesSorted = new ArrayList<>(values);
        Collections.sort(valuesSorted);

        int middle = valuesSorted.size() / 2;
        if (valuesSorted.size() % 2 == 1) {
            return valuesSorted.get(middle);
        } else {
            return (valuesSorted.get(middle - 1) + valuesSorted.get(middle)) / 2.0;
        }
    }

    public static List<Integer> getModes(final List<Integer> values) {

        if (values == null || values.isEmpty()) {
            return new LinkedList<>();
        }

        final Map<Integer, Long> countFrequencies = values.stream()
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

        final long maxFrequency = countFrequencies.values().stream()
                .mapToLong(count -> count)
                .max().orElse(-1);

        return countFrequencies.entrySet().stream()
                .filter(tuple -> tuple.getValue() == maxFrequency)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    public static List<Double> cumulativeMeans(final List<Integer> values) {
        Deque<Integer> stack = new ArrayDeque<>(values);
        List<Double> cumulativeMeans = new LinkedList<>();
        int cumulativeSum = stack.pop();
        int count = 1;
        cumulativeMeans.add((double) cumulativeSum);

        while (!stack.isEmpty()) {
            cumulativeSum += stack.pop();
            cumulativeMeans.add(cumulativeSum / (double) ++count);
        }
        return cumulativeMeans;
    }
}
