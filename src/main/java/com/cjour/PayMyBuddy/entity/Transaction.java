package com.cjour.PayMyBuddy.Entity;

import javax.persistence.*;

@Entity
@Table(name="transactions")
public class Transaction {

	@ManyToOne
	@JoinColumn(name="id", nullable=false)
	private User user;
	
	@Id
	@Column(name="idTransactions")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer idTransactions;
	
	@Column(name="amount")
	private double amount;
	
	@Column(name="description")
	private String description;
	
	@ManyToOne
	@JoinColumn(name="transmittor_id", nullable=false)
	private User transmittor;

	public Transaction() {
		super();
	}

	public Transaction(User user, Integer idTransactions, double amount, String description, User transmittor) {
		super();
		this.user = user;
		this.idTransactions = idTransactions;
		this.amount = amount;
		this.description = description;
		this.transmittor = transmittor;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Integer getIdTransactions() {
		return idTransactions;
	}

	public void setIdTransactions(Integer idTransactions) {
		this.idTransactions = idTransactions;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public User getTransmittor() {
		return transmittor;
	}

	public void setTransmittor(User transmittor) {
		this.transmittor = transmittor;
	}
}
