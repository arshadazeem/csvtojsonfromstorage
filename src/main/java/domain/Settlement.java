package domain;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Settlement implements Serializable {
	
	/**	default serialUid */
	private static final long serialVersionUID = 1L;	
	
	@JsonProperty("SETTLEMENT DATE")
	public String settlementDate;
	
	@JsonProperty("CUSIP")
	public String cusip;
	
	@JsonProperty("SYMBOL")
	public String symbol;
	
	@JsonProperty("QUANTITY")
	public int quantity;
	
	@JsonProperty("DESCRIPTION")
	public String description;
	
	@JsonProperty("PRICE")
	public String price;
	
	
	
	public String getSettlementDate() {
		return settlementDate;
	}
	public void setSettlementDate(String settlementDate) {
		this.settlementDate = settlementDate;
	}
	public String getCusip() {
		return cusip;
	}
	public void setCusip(String cusip) {
		this.cusip = cusip;
	}
	public String getSymbol() {
		return symbol;
	}
	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	
	@Override
	public String toString() {

		StringBuilder sb = new StringBuilder();

		sb.append("Date: " + this.settlementDate + ", CUSIP: " + this.cusip + ", Symbol: " + this.symbol + ", Quantity: " + this.quantity + ", Description: " + this.description+ ", Price: " + this.price);

		return sb.toString();

	}

}
