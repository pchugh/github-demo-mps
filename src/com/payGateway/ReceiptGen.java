package com.payGateway;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.payGateway.beans.ReceiptBean;
import com.paypal.api.payments.Payment;
import com.paypal.api.payments.RelatedResources;
import com.paypal.api.payments.Transaction;

@SuppressWarnings("serial")
public class ReceiptGen extends HttpServlet {
     
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        doPost(req, resp);
    }
    
	@Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
    	
		String uri = req.getRequestURI();
		
		String url = req.getRequestURL().toString();
    	
    	ReceiptBean receiptBean = null;
    	
    	if(!(receiptBean == null)){
    		receiptBean = getReceiptData(receiptBean);
    		req.setAttribute("receiptBean", receiptBean);
            req.getRequestDispatcher("Order.jsp").forward(req, resp);
    	}else{
    		req.getRequestDispatcher("MainPage.html").forward(req, resp);
    	}
    }
    
    //This method returns fully formed receipt bean 
    private ReceiptBean getReceiptData(ReceiptBean receiptBean2) {
    	ReceiptBean receiptBean = new ReceiptBean();
    	
    	receiptBean.setReceiptId("0001");
    	receiptBean.setAuthorName("We will get it from front end");
    	receiptBean.setGtSourceCode("We will get it from front end");
    	
		return receiptBean;
	}
}