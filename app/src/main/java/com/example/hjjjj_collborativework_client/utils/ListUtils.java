package com.example.hjjjj_collborativework_client.utils;

import java.util.List;

public class ListUtils {
    /**
     * 按照数字排序
     **/
    public static List<String> ByNumberSort(List<String> strList) {
        try {
            String temp = "";
            for (int i = 0; i < strList.size() - 1; i++) {
                for (int j = 0; j < strList.size() - 1 - i; j++) {
                    int current = split(strList.get(j));
                    int next = split(strList.get(j + 1));
                    if (current >next ) {
                        temp = strList.get(j);
                        strList.set(j,strList.get(j+1));
                        strList.set(j+1,temp);
                    }
                }
            }
            return  strList;
        }catch (Exception ex){
            return null;
        }

    }

    private static int split(String str) {
        try {
            String[] tempArr = str.split("/");
            String result = tempArr[tempArr.length - 1];
           int a = Integer.parseInt(result);
            return a;
        }catch (Exception ex){
            return -1;
        }

    }
}
