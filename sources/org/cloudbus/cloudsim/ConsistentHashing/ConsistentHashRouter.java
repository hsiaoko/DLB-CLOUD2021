/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.cloudbus.cloudsim.ConsistentHashing;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Collection;
import java.util.Iterator;
import java.util.SortedMap;
import java.util.TreeMap;
import org.cloudbus.cloudsim.ConsistentHashingWithBoundedLoad.ConsistentHashingWithBoundedLoad;
/**
 * @author linjunjie1103@gmail.com
 *
 * To hash Node objects to a hash ring with a certain amount of virtual node.
 * Method routeNode will return a Node instance which the object key should be allocated to according to consistent hash algorithm
 *
 * @param <T>
 */
public class ConsistentHashRouter<T extends Node> {
    private final SortedMap<Long, VirtualNode<T>> ring = new TreeMap<>();
    private final HashFunction hashFunction;

    public ConsistentHashRouter(Collection<T> pNodes, int vNodeCount) {
        //this(pNodes,vNodeCount, new MD5Hash());
        this(pNodes,vNodeCount, new MurmurHash());
    }

    /**
     *
     * @param pNodes collections of physical nodes
     * @param vNodeCount amounts of virtual nodes
     * @param hashFunction hash Function to hash Node instances
     */
    public ConsistentHashRouter(Collection<T> pNodes, int vNodeCount, HashFunction hashFunction) {
        if (hashFunction == null) {
            throw new NullPointerException("Hash Function is null");
        }
        this.hashFunction = hashFunction;
        if (pNodes != null) {
            for (T pNode : pNodes) {
                addNode(pNode, vNodeCount);
            }
        }
    }

    /**
     * add physic node to the hash ring with some virtual nodes
     * @param pNode physical node needs added to hash ring
     * @param vNodeCount the number of virtual node of the physical node. Value should be greater than or equals to 0
     */
    public void addNode(T pNode, int vNodeCount) {
        if (vNodeCount < 0) throw new IllegalArgumentException("illegal virtual node counts :" + vNodeCount);
        int existingReplicas = getExistingReplicas(pNode);
        for (int i = 0; i < vNodeCount; i++) {
            VirtualNode<T> vNode = new VirtualNode<>(pNode, i + existingReplicas);
            ring.put(hashFunction.Hash(vNode.getKey()), vNode);
        }
    }

    /**
     * remove the physical node from the hash ring
     * @param pNode
     */
    public void removeNode(T pNode) {
        Iterator<Long> it = ring.keySet().iterator();
        while (it.hasNext()) {
            Long key = it.next();
            VirtualNode<T> virtualNode = ring.get(key);
            if (virtualNode.isVirtualNodeOf(pNode)) {
                it.remove();
            }
        }
    }

    /**
     * with a specified key, route the nearest Node instance in the current hash ring
     * @param objectKey the object key to find a nearest Node
     * @return
     */
    public T routeNode(String objectKey) {
        if (ring.isEmpty()) {
            return null;
        }
        Long hashVal = hashFunction.Hash(objectKey);
        SortedMap<Long,VirtualNode<T>> tailMap = ring.tailMap(hashVal);
        Long nodeHashVal = !tailMap.isEmpty() ? tailMap.firstKey() : ring.firstKey();
        return ring.get(nodeHashVal).getPhysicalNode();
    }
    
    public T routeNodeCH(String objectKey) {
        if (ring.isEmpty()) {
            return null;
        }
        Long hashVal = hashFunction.Hash(objectKey);
        SortedMap<Long,VirtualNode<T>> tailMap = ring.tailMap(hashVal);
        Long nodeHashVal = !tailMap.isEmpty() ? tailMap.firstKey() : ring.firstKey();
        return ring.get(nodeHashVal).getPhysicalNode();
    }

    public int getExistingReplicas(T pNode) {
        int replicas = 0;
        for (VirtualNode<T> vNode : ring.values()) {
            if (vNode.isVirtualNodeOf(pNode)) {
                replicas++;
            }
        }
        return replicas;
    }

    
    //default hash function
    private static class MD5Hash implements HashFunction {
        MessageDigest instance;

        public MD5Hash() {
            try {
                instance = MessageDigest.getInstance("MD5");
            } catch (NoSuchAlgorithmException e) {
            }
        }

        @Override
        public long Hash(String key) {
            instance.reset();
            instance.update(key.getBytes());
            byte[] digest = instance.digest();

            long h = 0;
            for (int i = 0; i < 4; i++) {
                h <<= 8;
                h |= ((int) digest[i]) & 0xFF;
            }
            return h;
        }
    }
    
    private static class MurmurHash implements HashFunction{
    	MessageDigest instance;
        public MurmurHash() {
            try {
                instance = MessageDigest.getInstance("Murmur");
            } catch (NoSuchAlgorithmException e) {
            }
        }
        public static long Hash64( final byte[] data, int length, int seed) {
    		final long m = 0xc6a4a7935bd1e995L;
    		final int r = 47;

    		long h = (seed&0xffffffffl)^(length*m);

    		int length8 = length/8;

    		for (int i=0; i<length8; i++) {
    			final int i8 = i*8;
    			long k =  ((long)data[i8+0]&0xff)      +(((long)data[i8+1]&0xff)<<8)
    					+(((long)data[i8+2]&0xff)<<16) +(((long)data[i8+3]&0xff)<<24)
    					+(((long)data[i8+4]&0xff)<<32) +(((long)data[i8+5]&0xff)<<40)
    					+(((long)data[i8+6]&0xff)<<48) +(((long)data[i8+7]&0xff)<<56);
    			
    			k *= m;
    			k ^= k >>> r;
    			k *= m;
    			
    			h ^= k;
    			h *= m; 
    		}
    		switch (length%8) {
    		case 7: h ^= (long)(data[(length&~7)+6]&0xff) << 48;
    		case 6: h ^= (long)(data[(length&~7)+5]&0xff) << 40;
    		case 5: h ^= (long)(data[(length&~7)+4]&0xff) << 32;
    		case 4: h ^= (long)(data[(length&~7)+3]&0xff) << 24;
    		case 3: h ^= (long)(data[(length&~7)+2]&0xff) << 16;
    		case 2: h ^= (long)(data[(length&~7)+1]&0xff) << 8;
    		case 1: h ^= (long)(data[length&~7]&0xff);
    		        h *= m;
    		};
    	 
    		h ^= h >>> r;
    		h *= m;
    		h ^= h >>> r;

    		return h;
    	}
        public static long Hash64( final byte[] data, int length) {
        	return Hash64( data, length, 0xe17a1465);
        }
    	@Override
    	public long Hash(String key) {
    		final byte[] bytes = key.getBytes(); 
    		return Hash64( bytes, bytes.length);
    	}
    }

}
