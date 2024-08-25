package com.example.addressbook.controller;

import java.util.List;
import java.util.Optional;

import com.example.addressbook.securityconfig.jwt.JwtProvider;
import com.example.addressbook.securityconfig.repository.UserRepository;
import com.example.addressbook.securityconfig.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

import com.example.addressbook.entity.AddressBook;
import com.example.addressbook.repository.AddressBookRepository;
import org.springframework.web.client.HttpServerErrorException;

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

	@Autowired
	UserService userService;

	@Autowired
	UserRepository userRepository;

	@Autowired
	JwtProvider jwtProvider;

	@GetMapping("/signin")
	@ResponseStatus(HttpStatus.OK)
	public String login() {
		UsernamePasswordAuthenticationToken auth = (UsernamePasswordAuthenticationToken)SecurityContextHolder.getContext().getAuthentication();
		org.springframework.security.core.userdetails.User user = (org.springframework.security.core.userdetails.User)auth.getPrincipal();
		Optional<com.example.addressbook.securityconfig.entity.User> appUser = userRepository.findByEmail(user.getUsername());
		return jwtProvider.createToken(user.getUsername(), appUser.get().getRoles());
		//return userService.signin(loginDto.getUsername(), loginDto.getPassword()).orElseThrow(()->
		//		new HttpServerErrorException(HttpStatus.FORBIDDEN, "Login Failed"));
	}

	@PostMapping("/signin")
	public String login(@RequestBody LoginDto loginDto) {
		return userService.signin(loginDto.getUsername(), loginDto.getPassword()).orElseThrow(()->
				new HttpServerErrorException(HttpStatus.FORBIDDEN, "Login Failed"));
	}

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
		AddressBook address = repo.save(create("A","12345","Some address of A"));
		address = repo.save(create("B","12345","Some address of B"));
		address = repo.save(create("C","12345","Some address of C"));
		address = repo.save(create("D","12345","Some address of D"));
		address = repo.save(create("E","12345","Some address of E"));
	}

	private AddressBook create(String name, String phone, String address) {
		AddressBook ad = new AddressBook();
		ad.setName(name);
		ad.setPhone(phone);
		ad.setAddress(address);

		return ad;
	}
}
