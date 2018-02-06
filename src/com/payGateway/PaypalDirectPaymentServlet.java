package com.payGateway;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.payGateway.beans.ReceiptBean;
import com.paypal.api.payments.Address;
import com.paypal.api.payments.Amount;
import com.paypal.api.payments.CreditCard;
import com.paypal.api.payments.Details;
import com.paypal.api.payments.FundingInstrument;
import com.paypal.api.payments.Payer;
import com.paypal.api.payments.PayerInfo;
import com.paypal.api.payments.Payment;
import com.paypal.api.payments.RelatedResources;
import com.paypal.api.payments.Transaction;
import com.paypal.api.payments.util.GenerateAccessToken;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;
import com.paypal.base.rest.PayPalResource;

@SuppressWarnings("serial")
public class PaypalDirectPaymentServlet extends HttpServlet {
    private static final Logger LOGGER = Logger
            .getLogger(PaypalDirectPaymentServlet.class);
 
    public void init(ServletConfig servletConfig) throws ServletException {}
 
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        doPost(req, resp);
    }
    
	@Override
    protected void doPost(HttpServletRequest request, HttpServletResponse resp)
            throws ServletException, IOException {
    try{	
		String transcId=request.getParameter("tx");
		String authToken="Arl2RGWcPoF6eu9exVtCz0gSI1F39eVe8OrGkGi1QP5Nlkk8vCBf7vjeh4W";
		String res="";
		System.out.printltn("sssss");
		String query="cmd=_notify-synch&tx=" + transcId + "&at=" + authToken;
		String url="https://www.sandbox.paypal.com/cgi-bin/webscr";
		URL u=new URL(url);
		HttpsURLConnection req;
		
			req = (HttpsURLConnection)u.openConnection();
		
		req.setRequestMethod("POST");
		req.setDoOutput(true);
		req.setDoInput(true);
		req.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
		req.setFixedLengthStreamingMode(query.length());
		OutputStream outputStream=req.getOutputStream();
		outputStream.write(query.getBytes());
		outputStream.close();
		
		BufferedReader in= new BufferedReader(new InputStreamReader(req.getInputStream()));
		res=in.readLine();
		if(res.equals("SUCCESS")){
			while((res=in.readLine()) != null){
			
				System.out.println(res);
			}
			
		}
		
		
		} catch (Exception e) {
			System.out.println(e.getMessage());
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
	
    }
    
    //This method returns fully formed receipt bean 
    private ReceiptBean getReceiptData(Payment response) {
    	ReceiptBean receiptBean = new ReceiptBean();
    	for(Transaction tList : response.getTransactions()){
			receiptBean.setTotalAmount(tList.getAmount().getTotal());
			for(RelatedResources rr : tList.getRelatedResources()){
				receiptBean.setTransactionId(rr.getSale().getId());
			}
		}
    	receiptBean.setReceiptId("0001");
    	receiptBean.setAuthorName("We will get it from front end");
    	receiptBean.setGtSourceCode("We will get it from front end");
    	
		return receiptBean;
	}
    
    public Payment createPayment(HttpServletRequest req,
            HttpServletResponse resp) {
    	
        // address in a payment. [Optional]
        Address billingAddress = new Address();
        billingAddress.setCity(req.getParameter("city"));
        billingAddress.setCountryCode(req.getParameter("country"));
        billingAddress.setLine1(req.getParameter("address1"));
        billingAddress.setPostalCode(req.getParameter("postalCode"));
        billingAddress.setState(req.getParameter("statepostalCode"));
        
        // A resource representing a credit card that can be
        CreditCard creditCard = new CreditCard();
        creditCard.setBillingAddress(billingAddress);
        creditCard.setExpireMonth(Integer.parseInt(req.getParameter("exp_month")));
        creditCard.setExpireYear(Integer.parseInt(req.getParameter("exp_year")));
        creditCard.setFirstName(req.getParameter("first_name"));
        creditCard.setLastName(req.getParameter("last_name"));
        creditCard.setNumber(req.getParameter("card_number"));
        creditCard.setType(req.getParameter("card_type"));
        if(req.getParameter("card_type").equalsIgnoreCase("mastercard")){
        creditCard.setCvv2(Integer.parseInt(req.getParameter("cvv")));
        }
        
        //Detail of total amount(i.e. shipping+subtotal+tax)
        Details details = new Details();
        //details.setShipping("00");
        details.setSubtotal("81");
        //details.setTax("00");
        
        //Total amout with the currency details.
        Amount amount = new Amount();
        amount.setCurrency("USD");
        // Total must be equal to sum of shipping, tax and subtotal.
        amount.setTotal("81");
        amount.setDetails(details);
        
        //Adding amount detail to transaction
        Transaction transaction = new Transaction();
        transaction.setAmount(amount);
        transaction.setDescription("This is paypal REST API payment system");
        List<Transaction> transactions = new ArrayList<Transaction>();
        transactions.add(transaction);
        
        //Adding credit card to funding instrument
        FundingInstrument fundingInstrument = new FundingInstrument();
        fundingInstrument.setCreditCard(creditCard);
        List<FundingInstrument> fundingInstrumentList = new ArrayList<FundingInstrument>();
        fundingInstrumentList.add(fundingInstrument);
        
        //Adding payer information
        PayerInfo payerInfo = new PayerInfo();
        payerInfo.setBillingAddress(billingAddress);
        payerInfo.setEmail("testntest@mailinator.com");
        
        //Adding funding instrument and payment method to payer
        Payer payer = new Payer();
        payer.setFundingInstruments(fundingInstrumentList);
        payer.setPaymentMethod("credit_card");
        payer.setPayerInfo(payerInfo);
        
        //Adding payer and transaction details to payment
        Payment payment = new Payment();
        payment.setIntent("sale");
        payment.setPayer(payer);
        payment.setTransactions(transactions);
        
        Payment gatewayResponse = null;
        try{
            String accessToken = GenerateAccessToken.getAccessToken();
            APIContext apiContext = new APIContext(accessToken);
            gatewayResponse = payment.create(apiContext);
        } catch(PayPalRESTException e) {
        	return null;
        } catch (Exception e) {
        	e.printStackTrace();
        	return null;
        }
        return gatewayResponse;
    }
}