package org.cloudbus.cloudsim.ConsistentHashingWithBoundedLoad;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class BKDRHash {
	public static int seed = 31;
	public static int Hash(String str){
		int hash = 0;
		for(int i = 0;i!= str.length();++i){
			hash =  seed * hash + str.charAt(i);
		}
		return hash;
	}
	private static int SystemHash(String key) {
		int _hashCode = 0;
		_hashCode = key.hashCode();
		return _hashCode;
	}
}