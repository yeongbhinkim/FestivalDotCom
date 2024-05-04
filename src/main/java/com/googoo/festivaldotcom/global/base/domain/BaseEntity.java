package com.googoo.festivaldotcom.global.base.domain;


import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public abstract class BaseEntity {

	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
	private boolean deleted;

	// 생성 시간과 수정 시간을 설정하는 메소드를 직접 구현
	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public void setUpdatedAt(LocalDateTime updatedAt) {
		this.updatedAt = updatedAt;
	}

	public void markDeleted() {
		this.deleted = true;
	}
}