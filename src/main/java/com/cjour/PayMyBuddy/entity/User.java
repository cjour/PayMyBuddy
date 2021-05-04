package com.cjour.PayMyBuddy.Entity;

import java.util.List;
import java.util.Set;

import javax.persistence.*;

@Entity
@Table(name = "users")
public class User{

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@OneToMany(targetEntity=Transaction.class, mappedBy= "user")
	private List<Transaction> transactions;
	
	@OneToMany(targetEntity=Transaction.class, mappedBy= "transmittor")
	private List<Transaction> transactionsAsTransmittor;
	
	@ManyToMany
	@JoinTable(
			name="contacts",
			joinColumns = { @JoinColumn(name="first_user") },
			inverseJoinColumns = { @JoinColumn(name = "second_user") }
			)
	private Set<User> contacts;
	
	@ManyToMany
	@JoinTable(
			name="contacts",
			joinColumns = { @JoinColumn(name="second_user") },
			inverseJoinColumns = { @JoinColumn(name = "first_user") }
			)
	private Set<User> contactOf;
	
	@OneToOne
	@PrimaryKeyJoinColumn
	private CreditCard creditCard;
		
	@Column(name = "user_name")
	private String userName;

	@Column(name = "password")
	private String password;



	
	public User() {
		super();
	}

	public User(Integer id, List<Transaction> transactions, List<Transaction> transactionsAsTransmittor,Set<User> contacts, Set<User> contactOf,
			CreditCard creditCard, String userName, String password) {
		super();
		this.id = id;
		this.transactions = transactions;
		this.transactionsAsTransmittor = transactionsAsTransmittor;
		this.contacts = contacts;
		this.contactOf = contactOf;
		this.creditCard = creditCard;
		this.userName = userName;
		this.password = password;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public List<Transaction> getTransactions() {
		return transactions;
	}

	public void setTransactions(List<Transaction> transactions) {
		this.transactions = transactions;
	}
	
	public List<Transaction> getTransactionsAsTransmittor() {
		return transactionsAsTransmittor;
	}

	public void setTransactionsAsTransmittor(List<Transaction> transactionsAsTransmittor) {
		this.transactionsAsTransmittor = transactionsAsTransmittor;
	}

	public Set<User> getContacts() {
		return contacts;
	}

	public void setContacts(Set<User> contacts) {
		this.contacts = contacts;
	}

	public Set<User> getContactOf() {
		return contactOf;
	}

	public void setContactOf(Set<User> contactOf) {
		this.contactOf = contactOf;
	}

	public CreditCard getCreditCard() {
		return creditCard;
	}

	public void setCreditCard(CreditCard creditCard) {
		this.creditCard = creditCard;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
