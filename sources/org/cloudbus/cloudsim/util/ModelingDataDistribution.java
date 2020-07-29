package org.cloudbus.cloudsim.util;

import java.util.*;
import org.cloudbus.cloudsim.distributions.UniformDistr;
import org.cloudbus.cloudsim.distributions.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class ModelingDataDistribution {
	public static void main(String[] args) {
		 
		String filePath = "/home/zhuxk/eclipse-workspace/DLB/outputs/distribution.csv";	
		int dataSize = 8192;
		int max = 80000;
		int min = 0;
		HashSet<Integer> a = new HashSet<Integer>();
		
	//	a = DataGeneratorUniform(max, min, dataSize);
		a = DataGeneratorLognormal(9, 0.2, dataSize);
	//	a = DataGeneratorExp(3000, dataSize);
	//	a = DataGeneratorNormal(40000, 110, dataSize);
		System.out.print(a);
        TreeSet<Integer> tree = new TreeSet<>(new MyComparator());
        tree.addAll(a);
        
        System.out.print("\n");

		System.out.print(tree);
		SaveSet(filePath, tree);
	}
	public static HashSet<Integer> DataGeneratorNormal(double mean, int dev, int dataSize) {
		HashSet<Integer> a = new HashSet<Integer>();
		NormalDistr normalDistr = new NormalDistr(mean, dev);
		while(a.size() < dataSize) {
			a.add(
				(int)normalDistr.sample()%90000);
		}
		return a;
	}
	public static HashSet<Integer> DataGeneratorZipf(double shape, int population, int dataSize) {
		HashSet<Integer> a = new HashSet<Integer>();
		ZipfDistr zipfDistr = new ZipfDistr(shape, population);
		while(a.size() < dataSize) {
			a.add(
				(int)zipfDistr.sample()%20000
					);
		}
		return a;
	}
	public static HashSet<Integer> DataGeneratorUniform(int max, int min, int dataSize) {
		HashSet<Integer> a = new HashSet<Integer>();
		UniformDistr uniformDistr = new UniformDistr(min, max);
		while(a.size() < dataSize) {
			a.add(
				(int)uniformDistr.sample()%20000
					);
		}
		return a;
	}
	public static HashSet<Integer> DataGeneratorExp(double mean,  int dataSize) {
		HashSet<Integer> a = new HashSet<Integer>();
		ExponentialDistr exponentialDistr = new ExponentialDistr(mean);
		while(a.size() < dataSize) {
			a.add(
				(int)exponentialDistr.sample()%20000
					);
		}
		return a;
	}
	public static HashSet<Integer> DataGeneratorLognormal(double mean, double dev, int dataSize) {
		HashSet<Integer> a = new HashSet<Integer>();
		LognormalDistr lognormalDistr =  new LognormalDistr(mean, dev);
		while(a.size() < dataSize) {
			a.add(
				(int)lognormalDistr.sample()%50000
					);
		}
		return a;
	}
	
	public static class MyComparator implements Comparator<Integer>{

	    @Override
	    public int compare(Integer o1, Integer o2) {
	        return o1 - o2;
	    }
	}
	public static void SaveSet(String filePath, HashSet<Integer> a) {
		try {
			File f = new File(filePath);
			if (!f.exists()) {
				f.createNewFile();
			}
			FileOutputStream fop = null;
			fop = new FileOutputStream(f);
			
			Iterator<Integer> iterator = a.iterator();
			while(iterator.hasNext()) {
				fop.write(
						new String(iterator.next()+",").getBytes()
						);
			}
			System.out.print("save() finished");
			fop.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void SaveSet(String filePath, TreeSet<Integer> a) {
		try {
			File f = new File(filePath);
			if (!f.exists()) {
				f.createNewFile();
			}
			FileOutputStream fop = null;
			fop = new FileOutputStream(f);
			
			Iterator<Integer> iterator = a.iterator();
			while(iterator.hasNext()) {
				fop.write(
						new String(iterator.next()+",").getBytes()
						);
			}
			System.out.print("save() finished");
			fop.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
