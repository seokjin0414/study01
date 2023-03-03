package com.company.nill.myTool.utill;

import com.fasterxml.uuid.EthernetAddress;
import com.fasterxml.uuid.Generators;
import com.fasterxml.uuid.impl.TimeBasedGenerator;
import com.withdraw.batch.comm.util.Camflake;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class UIDGenerator {

    /**
     * 10자리의 Integer범위내에서 UID를 생성한다.
     * @return uid
     */
    private	static TimeBasedGenerator gen = Generators.timeBasedGenerator(EthernetAddress.fromInterface());

    final Camflake camflake = new Camflake();

    public static String getId(){
    	return getId("");
    }

    public static String getIdDouble(){
    	return getId()+getId();
    }

	/**
	* @return
	*/
	public static String getId(String group){
		UUID uuid = gen.generate();
		if(StringUtils.isEmpty(group)){
			return uuid.toString();
		}else{
			return group+"-"+uuid.toString();
		}
	}

    public static void main(String[] a){
    	for(int i=0;i<10;i++){
    		//문자형 UID 생성
    		System.out.println(getIdDouble());
    	}
    }

    public long getUid(){
        return camflake.next();
    }
}
