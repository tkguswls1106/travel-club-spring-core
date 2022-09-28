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
		this.id = UUID.randomUUID().toString();
	}

	protected Entity(String id) {
		//
		this.id = id;
	}
}
