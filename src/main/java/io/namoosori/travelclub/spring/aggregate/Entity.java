package io.namoosori.travelclub.spring.aggregate;

import lombok.Getter;
import lombok.Setter;

import java.util.*;

@Getter
@Setter
public abstract class Entity {
	//
	protected String id;  // 어노테이션 Getter과 Setter 덕분에 이 추상클래스 Entity를 상속받는 클래스에서는 getId 메소드 등등을 사용할수있다.

	protected Entity() {
		//
		this.id = UUID.randomUUID().toString();  // 이거때문에 id값이 일반적인 숫자 자료형이 아니라, 암호화된 복잡한 문자열처럼 문자열 자료형을 가진 id 값이 되는것이다.
	}

	protected Entity(String id) {
		//
		this.id = id;
	}
}
