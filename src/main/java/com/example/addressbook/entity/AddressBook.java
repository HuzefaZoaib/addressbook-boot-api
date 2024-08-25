package com.example.addressbook.entity;

import jakarta.persistence.*;
import java.text.MessageFormat;

@Entity
public class AddressBook {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	private String name;
	private String phone;
	private String address;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	@Override
	public String toString() {
		return MessageFormat.format("Id:{0}, Name:{1}, Phone:{2}, Address:{3}", id, name, phone, address);
	}
}
