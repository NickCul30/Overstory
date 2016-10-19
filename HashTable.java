//HashTable.java
//Nicholas Culmone
//The class that contains all of the methods for my HashTable.

import java.util.*;

public class HashTable<T>{
	private ArrayList<LinkedList<T>>table;
	private int size;
	private double maxLoad;
	
	public HashTable(){
		size = 0;
		table = blank(10);
		maxLoad = 0.7;
	}
	
	//returns an empty ArrayList with size n
	public ArrayList<LinkedList<T>> blank(int n){
		ArrayList<LinkedList<T>> tmp = new ArrayList<LinkedList<T>>();
		for(int i=0;i<n;i++){
			tmp.add(null);
		}
		return tmp;
	}
	
	//makes the table twice the size of the current one (of null values), then adds all of the
	//values to the new table
	public void resize(){
		ArrayList<LinkedList<T>>tmp = table;
		table = blank(table.size()*2);
		size = 0;
		for(LinkedList<T>lst : tmp){
			if(lst != null){
				for(T val : lst){
					add(val);
				}
			}
		}
	}
	
	//mults the table's size by the user input and readds the values
	public void setLoad(double percent){
		if(percent > 0.1 && percent <= 0.8 && percent < maxLoad){
			ArrayList<LinkedList<T>>tmp = table;
			table = blank((int)(size/percent));
			size = 0;
			for(LinkedList<T>lst : tmp){
				if(lst != null){
					for(T val : lst){
						add(val);
					}
				}
			}
			if(percent < (double)(size)/(double)(table.size())){
				table.add(null);
			}
		}
	}
	
	//adds a value to the hashTable, puts the value in the correct position based on its
	//hashCode and the size of the HashTable
	public void add(T val){
		int h = Math.abs(val.hashCode());
		int pos = h % table.size();
		if(table.get(pos) == null){
			table.set(pos,new LinkedList<T>());
		}
		table.get(pos).add(val);
		size ++;
		if(size/table.size() > maxLoad){
			resize();
		}
	}
	
	//gets the position of the value in the HashTable by using the hashCode and the size of
	//the hashTable, and removes it from the LinkedList that holds the value
	public void remove(T val){
		int h = Math.abs(val.hashCode());
		int pos = h % table.size();
		if(table.get(pos) != null){
			table.get(pos).remove(val);
		}
	}
	
	//takes in a hashCode and returns the value in the table with that hashCode
	public T get(int code){
		code = Math.abs(code);
		int pos = code % table.size(); //get position of the value in the table
		if(table.get(pos) == null){
			return null;
		}
		//goes through the LList to find the value that matches the hashCode
		else{
			for(int i=0;i<table.get(pos).size();i++){
				int h = Math.abs(table.get(pos).get(i).hashCode());
				if(h == code){
					return table.get(pos).get(i);
				}
			}
			return null;
		}
	}
	
	public double getLoad(){
		return (double)(size)/(double)(table.size());
	}
	public int getSize(){
		return size;
	}
	
	public void setMaxLoad(double max){
		if(max > 0.1 && max <= 0.8){
			maxLoad = max;
		}
	}
	
	public String toString(){
		String ans = "{";
		for(LinkedList<T> lst : table){
			if(lst != null){
				for(T val : lst){
					ans += val + ",";
				}
			}
		}
		return ans + "}";
	}
	
	//makes an array with no empty spots out of the values in the HashTable
	public ArrayList toArray(){
		ArrayList<LinkedList<T>> tmp = new ArrayList<LinkedList<T>>();
		for(int i=0;i<table.size();i++){
			if(table.get(i) != null){
				tmp.add(table.get(i));
			}
		}
		return tmp;
	}
}