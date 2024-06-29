package com.example.addressbook.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.addressbook.entity.AddressBook;
import com.example.addressbook.repository.AddressBookRepository;

@RestController
public class AddressBookController {

	@Autowired
	private AddressBookRepository repo;
	
//	@RequestMapping(method=RequestMethod.GET)
//	public List<AddressBook> _default() {
//		List<AddressBook> records = (List<AddressBook>)repo.findAll();
//		if(records.isEmpty()) {
//			loadFewAddressess();
//		}
//		
//		return (List<AddressBook>)repo.findAll();
//	}
	
	@GetMapping("/list")
	public List<AddressBook> getAddressess() {
		List<AddressBook> records = (List<AddressBook>)repo.findAll();
		if(records.isEmpty()) {
			loadFewAddressess();
		}
		
		return (List<AddressBook>)repo.findAll();
	}
	
	@PutMapping
	public AddressBook putAddress(@RequestBody AddressBook address) {
		System.out.println("New Address: " +address);
		return (AddressBook)repo.save(address);
	}

	
	private void loadFewAddressess() {
		@SuppressWarnings("unused")
		AddressBook address = repo.save(AddressBook.builder().name("A").phone("12345").address("Some address of A").build());
		address = repo.save(AddressBook.builder().name("B").phone("12345").address("Some address of B").build());
		address = repo.save(AddressBook.builder().name("C").phone("12345").address("Some address of C").build());
		address = repo.save(AddressBook.builder().name("D").phone("12345").address("Some address of D").build());
		address = repo.save(AddressBook.builder().name("E").phone("12345").address("Some address of E").build());
	}
}
