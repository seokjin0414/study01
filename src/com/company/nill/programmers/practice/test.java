package com.company.nill.programmers.practice;



import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// 코드 작성용
public class test {
    public static void main(String[] args) {
        test t = new test();

    }

    public String[] solution(String[] players, String[] callings) {
        Map<String,Integer> map = new HashMap<>();

        for (int i=0; i<players.length; i++) {
            map.put(players[i], i);
        }

        for (String s : callings) {
            int r = map.get(s);
            String front = players[r-1];

            players[r-1] = s;
            players[r] = front;

            map.put(s,r-1);
            map.put(front,r);
        }

        return players;
    }

    public List<String> solution2(String[] players, String[] callings) {
        Map<String, Integer> p = new HashMap<>();
        Map<Integer, String> r = new HashMap<>();

        for (int i=0; i<players.length; i++) {
            p.put(players[i], i);
            r.put(i, players[i]);
        }

        for (String s : callings) {
            int n = p.get(s);
            String f = r.get(n - 1);

            p.put(s, n - 1);
            p.put(f, n);
            r.put(n - 1, s);
            r.put(n, f);
        }

        List<String> answer = new ArrayList<>(r.values());

        return answer;
    }
}
