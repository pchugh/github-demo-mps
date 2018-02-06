package com.payGateway.beans;

import java.util.List;

import com.paypal.api.payments.FundingInstrument;
import com.paypal.api.payments.Transaction;

public class PaymentCreateBean {
	
	//Variables for credit card
	private int expiryMonth;
	private int expiryYear;
	private String firstName;
	private String lastName;
	private String cardNumber;
	private String cardType;
	private String cvvNumber;
	
	//Variables for detail amount
	private String shippingCharge;
	private String subTotalAmount;
	private String taxAmount;
	
	//Variable for amount
	private String currencyType;
	private String totalAmount;
	
	//Variables for transaction
	private String description;
	private List<Transaction> transactions;
	
	//Variables for funding instrument
	private List<FundingInstrument> fundingInstrumentList;
	
	//Variables for payer information
	private String payerEmail;
	
	

}
