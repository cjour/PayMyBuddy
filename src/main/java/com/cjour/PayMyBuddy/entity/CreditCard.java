package com.cjour.PayMyBuddy.Entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
import javax.persistence.Table;

	@Entity
	@Table(name="creditcards")
	public class CreditCard {

		@Id
		@Column(name="id_credit_card")
		@GeneratedValue(strategy = GenerationType.AUTO)
		private Integer id_credit_card;
		
		@Column(name="deposit")
		private double deposit;
		
		
		@OneToOne
	    @MapsId
	    @JoinColumn(name = "user_id")
	    private User user;

		public CreditCard() {
			super();
		}
		
		public CreditCard(Integer id_credit_card, double deposit, User user) {
			super();
			this.id_credit_card = id_credit_card;
			this.deposit = deposit;
			this.user = user;
		}

		public Integer getId_credit_card() {
			return id_credit_card;
		}

		public void setId_credit_card(Integer id_credit_card) {
			this.id_credit_card = id_credit_card;
		}

		public double getDeposit() {
			return deposit;
		}

		public void setDeposit(double d) {
			this.deposit = d;
		}

		public User getUser() {
			return user;
		}

		public void setUser(User user) {
			this.user = user;
		}
	}

