package com.payGateway.beans;

import java.util.List;

import com.paypal.api.payments.BillingInfo;
import com.paypal.api.payments.Item;

public class ReceiptBean {
	
	private String transactionId;
	private String receiptId;
	private String authorName;
	private String gtSourceCode;
	private List<Item> itemList;
	private String totalAmount;
	private BillingInfo chargedto;
	
	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}
	public String getTransactionId() {
		return transactionId;
	}
	public void setReceiptId(String receiptId) {
		this.receiptId = receiptId;
	}
	public String getReceiptId() {
		return receiptId;
	}
	public void setAuthorName(String authorName) {
		this.authorName = authorName;
	}
	public String getAuthorName() {
		return authorName;
	}
	public void setGtSourceCode(String gtSourceCode) {
		this.gtSourceCode = gtSourceCode;
	}
	public String getGtSourceCode() {
		return gtSourceCode;
	}
	public void setItem(List<Item> itemList) {
		this.itemList = itemList;
	}
	public List<Item> getItem() {
		return itemList;
	}
	public void setTotalAmount(String totalAmount) {
		this.totalAmount = totalAmount;
	}
	public String getTotalAmount() {
		return totalAmount;
	}
	public void setChargedto(BillingInfo chargedto) {
		this.chargedto = chargedto;
	}
	public BillingInfo getChargedto() {
		return chargedto;
	}
}
