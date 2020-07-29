package org.mytest1;
import java.util.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class Writer {
	
	 public static void main(String[] args) {
		 	
		 	int data_size = 16384;
	        //推荐
	        int[] a = testC(data_size);
	        //网上方法获取不重复随机数
	        //testA(data_size);

	        for(int i = 0; i < data_size; i++) {
	        	System.out.printf("%d	", a[i]);
	        }
	    }
	 
	    /**
	     * 首先生成一个不重复的数集（0~9999），然后每次从这个集合中随机的取出一个数字作为生成的随机数（并且从集合中移除它）
	     *
	     * @param sz
	     */
	    public static int[] testC(int sz) {
	        long startTime = System.currentTimeMillis(); //开始测试时间
	        Random rd = new Random();
	        int[] rds = new int[sz];//随机数数组
	        List<Integer> lst = new ArrayList<Integer>();//存放有序数字集合
	        int index = 0;//随机索引
	        for (int i = 0; i < sz; i++) {
	            lst.add(i);
	        }
	        for (int i = 0; i < sz; i++) {
	            index = rd.nextInt(sz - i);
	            rds[i] = lst.get(index);
	            lst.remove(index);
	        }
	        long endTime = System.currentTimeMillis(); //获取结束时间
	        System.out.println("testC运行时间： " + (endTime - startTime) + "ms");
	        return rds;
	    }
	 
	 
	    /**
	     * 网上方法获取不重复随机数
	     *
	     * @param sz
	     */
	    public static int[] testA(int sz) {
	        long startTime = System.currentTimeMillis(); //开始测试时间
	        Random random = new Random();
	        int a[] = new int[sz];
	        for (int i = 0; i < a.length; i++) {
	            a[i] = random.nextInt(sz);
	            for (int j = 1; j < i; j++) {
	                while (a[i] == a[j]) {//如果重复，退回去重新生成随机数
	                    i--;
	                }
	            }
	        }
	        long endTime = System.currentTimeMillis(); //获取结束时间
	        System.out.println("网上思路代码运行时间： " + (endTime - startTime) + "ms");
	        return a;
	    }
}
