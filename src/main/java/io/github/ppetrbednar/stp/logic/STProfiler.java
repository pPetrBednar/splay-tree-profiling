package io.github.ppetrbednar.stp.logic;

import io.github.ppetrbednar.stp.logic.structures.SplayTreeLegacy;

import java.util.UUID;

public class STProfiler {
    public record User(String uid, String username) {
    }

    public static void test() {
        SplayTreeLegacy<String, User> splayTree = new SplayTreeLegacy<>();

     /*   User user30 = new User("30", "30");
        User user20 = new User("20", "20");
        User user40 = new User("40", "40");
        User user10 = new User("10", "10");
        User user28 = new User("28", "28");
        User user35 = new User("35", "35");
        User user50 = new User("50", "50");
        User user25 = new User("25", "25");
        User user22 = new User("22", "22");
        User user23 = new User("23", "23");

        splayTree.add(user30.uid, user30);
        splayTree.add(user20.uid, user20);
        splayTree.add(user40.uid, user40);
        splayTree.add(user10.uid, user10);
        splayTree.add(user28.uid, user28);
        splayTree.add(user35.uid, user35);
        splayTree.add(user50.uid, user50);
        splayTree.add(user25.uid, user25);
        splayTree.add(user22.uid, user22);
        splayTree.add(user23.uid, user23);*/

        for (int i = 1; i < 100; i++) {
            User user = new User(UUID.randomUUID().toString(), "Name " + i);
            splayTree.add(user.uid, user);
        }


    }
}
