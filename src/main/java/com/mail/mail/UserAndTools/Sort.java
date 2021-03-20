package com.mail.mail.UserAndTools;

public class Sort {
	public String[] sort(String[] arr, String criteria) {
		int index = 0;
		if(criteria.equals("date")){
            index=0;
        }else if(criteria.equals("priority")){
            index=1;
        }else if(criteria.equals("sender")){
            index=2;
        }else if(criteria.equals("receiver")){
            index=3;
        }else if(criteria.equals("topic")){
            index=4;
        }if(arr.length < 12) {
        	return arr;
        }else {
        	for(int i = 0; i < arr.length; i += 6) {
        		for(int j = i + 6; j < arr.length; j += 6) {
        			int cmp = arr[i + index].compareTo(arr[j + index]);
        			if(cmp > 0) {
        				for(int k = 0; k < 6; k++) {
        					String temp = arr[i + k];
        					arr[i + k] = arr[j + k];
        					arr[j + k] = temp;
        				}
        			}
        		}
    		}
        	return arr;
        }
	}public String[] sortContacts(String[] arr, String criteria) {
		int index = 1;
		if(criteria.equals("email")) {
			index = 2;
		}
		for(int i = 0; i < arr.length; i += 3) {
			for(int j = i + 3; j < arr.length; j += 3) {
				int cmp = arr[i + index].compareTo(arr[j + index]);
				if(cmp > 0) {
					for(int k = 0; k < 3; k++) {
						String temp = arr[i + k];
						arr[i + k] = arr[j + k];
    					arr[j + k] = temp;
					}
				}
			}
		}return arr;
	}
}
