package com.smart.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.smart.entities.Contact;

public interface ContactRepositiry extends JpaRepository<Contact, Integer>{
	
	@Query("select c from Contact as c where c.user.id =:userId")
	public Page<Contact> findContactByUser(@Param("userId") int userId, Pageable pageable);
//	public List<Contact> findContactByUser(@Param("userId") int userId);

}
