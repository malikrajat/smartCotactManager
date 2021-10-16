package com.smart.entities;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sun.istack.NotNull;

@Entity
@Table(name = "contacts")
public class Contact {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(unique = true)
	@NotNull
	private int id;

	private String name;
	
	private String lastName;

	private String work;

	private String email;

	private String phone;
	
	private boolean enabled;

	private String image;

	@Column(length = 5000)
	private String description;
	
	@ManyToOne
	@JsonIgnore
	private User user;

	@CreationTimestamp
	private Date created;

	@UpdateTimestamp
	private Date updated;

	public Contact() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Contact(int id, String name, String lastName, String work, String email, String phone, boolean enabled,
			String image, String description, Date created, Date updated) {
		super();
		this.id = id;
		this.name = name;
		this.lastName = lastName;
		this.work = work;
		this.email = email;
		this.phone = phone;
		this.enabled = enabled;
		this.image = image;
		this.description = description;
		this.created = created;
		this.updated = updated;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getWork() {
		return work;
	}

	public void setWork(String work) {
		this.work = work;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public Date getUpdated() {
		return updated;
	}

	public void setUpdated(Date updated) {
		this.updated = updated;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@Override
	public String toString() {
		return "Contact [id=" + id + ", name=" + name + ", lastName=" + lastName + ", work=" + work + ", email=" + email
				+ ", phone=" + phone + ", enabled=" + enabled + ", image=" + image + ", description=" + description
				+ ", created=" + created + ", updated=" + updated + "]";
	}
	
	@Override
	public boolean equals(Object obj) {
		// TODO Auto-generated method stub
		return this.id == ((Contact) obj).getId();
	}


}
