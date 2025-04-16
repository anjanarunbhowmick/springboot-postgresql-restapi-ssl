package com.springboot.pg.ssl.postgresql.model;

import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="phone_numbers")
public class PhoneNumbers {

	@Id
	@Column(name="phone_id")
	@GeneratedValue(strategy = GenerationType.UUID)
	private UUID id;

	@Column(name="ph_number")
	private String phoneNumber;

	public PhoneNumbers() {
		super();
	}

	public PhoneNumbers(UUID id, String phoneNumber) {
		super();
		this.id = id;
		this.phoneNumber = phoneNumber;
	}

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	
}
