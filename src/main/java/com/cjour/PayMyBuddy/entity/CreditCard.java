package com.cjour.PayMyBuddy.entity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

	@Entity
	@Table(name="credit_cards")
	public class CreditCard {

		@Id
		@Column(name="id")
		@GeneratedValue(strategy = GenerationType.AUTO)
		private Integer id;
		
		
		@Column(name="deposit")
		private Integer deposit;
		
		@Column(name="withdrawal")
		private String description;
		
		@Column(name="user")
		private Integer user;

		
		public CreditCard(Integer id, Integer deposit, String description, Integer user) {
			super();
			this.id = id;
			this.deposit = deposit;
			this.description = description;
			this.user = user;
		}

		public int getId() {
			return id;
		}

		public void setId(Integer id) {
			this.id = id;
		}

		public int getDeposit() {
			return deposit;
		}

		public void setDeposit(Integer deposit) {
			this.deposit = deposit;
		}

		public String getDescription() {
			return description;
		}

		public void setDescription(String description) {
			this.description = description;
		}

		public int getUser() {
			return user;
		}

		public void setUser(Integer user) {
			this.user = user;
		}
	
}
