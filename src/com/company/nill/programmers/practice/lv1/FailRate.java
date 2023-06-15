package com.company.nill.programmers.practice.lv1;

import java.util.*;
import java.util.stream.Collectors;

public class FailRate {
        public Object solution(int N, int[] stages) {
            Map<Integer, Integer> map = new HashMap<>();
            Map<Integer, Double> rateMap = new HashMap<>();

            for (int i=1; i<=N; i++) {
                map.put(i,0);
            }

            for (int j : stages) {
                if (N>=j) {
                    map.put(j, map.get(j)+1);
                }
            }

            int notCounted = stages.length;
            for (int k=1; k<=map.size(); k++) {
                Double d = 0.0;

                if (notCounted>=map.get(k) && map.get(k)>0) {
                    d = Double.valueOf(map.get(k)) / Double.valueOf(notCounted - map.get(k));
                }

                rateMap.put(k, d);
                notCounted -= map.get(k);
            }

            List<Map.Entry<Integer, Double>> result = rateMap.entrySet()
                    .stream()
                    .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                    .collect(Collectors.toList());

            List<Integer> answer = new ArrayList<>();

            for (Map.Entry<Integer, Double> vo : result) {
                answer.add(vo.getKey());
            }
            return answer;
        }

    public int[] solution2(int N, int[] lastStages) {
        int nPlayers = lastStages.length;
        int[] nStagePlayers = new int[N + 2];
        for (int stage : lastStages) {
            nStagePlayers[stage] += 1;
        }

        int remainingPlayers = nPlayers;
        List<Stage> stages = new ArrayList<>();
        for (int id = 1 ; id <= N; id++) {
            double failure = (double) nStagePlayers[id] / remainingPlayers;
            remainingPlayers -= nStagePlayers[id];

            Stage s = new Stage(id, failure);
            stages.add(s);
        }
        Collections.sort(stages, Collections.reverseOrder());

        int[] answer = new int[N];
        for (int i = 0; i < N; i++) {
            answer[i] = stages.get(i).id;
        }
        return answer;
    }

    class Stage implements Comparable<Stage> {
        public int id;
        public double failure;

        public Stage(int id_, double failure_) {
            id = id_;
            failure = failure_;
        }

        @Override
        public int compareTo(Stage o) {
            if (failure < o.failure ) {
                return -1;
            }
            if (failure > o.failure ) {
                return 1;
            }
            return 0;
        }
    }
}
