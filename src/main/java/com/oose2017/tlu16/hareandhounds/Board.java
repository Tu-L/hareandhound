package com.oose2017.tlu16.hareandhounds;

import java.util.*;

public class Board {
    public static final Map<Integer, Set<Integer>> HOUNDMOVES;
    public static final Map<Integer, Set<Integer>> HAREMOVES;
    static {
        Hashtable<Integer, Set<Integer>> tmp = new Hashtable<>();
        for (int j = 0; j < 3; j++) {
            for (int i = 0; i <= 4; i++) {
                if ((j == 0 || j == 2) && (i == 0 && i == 4)) {
                    continue;
                }
                tmp.put(i * 10 + j, new HashSet<>());
            }

        }
        tmp.get(1).addAll(Arrays.asList(10, 11, 12));
        tmp.get(10).addAll(Arrays.asList(11, 21, 20));
        tmp.get(11).addAll(Arrays.asList(10, 12, 21));
        tmp.get(12).addAll(Arrays.asList(11, 21, 22));
        tmp.get(20).addAll(Arrays.asList(21, 30));
        tmp.get(21).addAll(Arrays.asList(20, 30, 31, 32, 22));
        tmp.get(22).addAll(Arrays.asList(21, 32));
        tmp.get(30).addAll(Arrays.asList(31, 41));
        tmp.get(31).addAll(Arrays.asList(41, 32, 30));
        tmp.get(32).addAll(Arrays.asList(31, 41));
        HOUNDMOVES = Collections.unmodifiableMap(tmp);

        Hashtable<Integer, Set<Integer>> tmp2 = new Hashtable<>();
        for (int j = 0; j < 3; j++) {
            for (int i = 0; i <= 4; i++) {
                if ((j == 0 || j == 2) && (i == 0 && i == 4)) {
                    continue;
                }
                tmp2.put(i * 10 + j, new HashSet<>());
            }

        }
        tmp2.get(1).addAll(Arrays.asList(10, 11, 12));
        tmp2.get(10).addAll(Arrays.asList(11, 21, 20, 1));
        tmp2.get(11).addAll(Arrays.asList(1, 12, 21, 10));
        tmp2.get(12).addAll(Arrays.asList(11, 21, 22, 1));
        tmp2.get(20).addAll(Arrays.asList(21, 30, 10));
        tmp2.get(21).addAll(Arrays.asList(20, 30, 31, 32, 22, 12, 11, 10));
        tmp2.get(22).addAll(Arrays.asList(21, 32, 12));
        tmp2.get(30).addAll(Arrays.asList(31, 41, 21, 20));
        tmp2.get(31).addAll(Arrays.asList(41, 32, 30, 21));
        tmp2.get(32).addAll(Arrays.asList(31, 41, 21, 22));
        tmp2.get(41). addAll(Arrays.asList(30, 31, 32));
        HAREMOVES = Collections.unmodifiableMap(tmp2);
    }
}
