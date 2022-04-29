package com.assignment;

import java.util.ArrayList;
import java.util.Arrays;

public class Test {
    public static void main(String[] args) {
        ArrayList<Integer> arr = new ArrayList<>();
        arr.add(10);
        arr.add(20);
        arr.add(30);
        System.out.println(arr);
        arr.clear();
        System.out.println(arr.size());
        System.out.println(arr);
    }
}
