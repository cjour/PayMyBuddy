package com.cjour.PayMyBuddy.entity;

import javax.persistence.*;

@Entity
@Table(name="transactions")
public class Transaction {

	@Id
	@Column(name="id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;
	
	
	@Column(name="amount")
	private Integer amount;
	
	@Column(name="description")
	private String description;
	
	@Column(name="transmittor")
	private Integer transmittor;
	
	@Column(name="beneficiary")
	private Integer beneficiary;
	
	public Transaction(Integer id, Integer amount, String description, Integer transmittor, Integer beneficiary) {
		super();
		this.id = id;
		this.amount = amount;
		this.description = description;
		this.transmittor = transmittor;
		this.beneficiary = beneficiary;
	}

	public int getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(Integer amount) {
		this.amount = amount;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getTransmittor() {
		return transmittor;
	}

	public void setTransmittor(Integer transmittor) {
		this.transmittor = transmittor;
	}

	public int getBeneficiary() {
		return beneficiary;
	}

	public void setBeneficiary(Integer beneficiary) {
		this.beneficiary = beneficiary;
	}
	
}
