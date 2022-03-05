package com.ezypay.restservices;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;

@RestController
@RequestMapping("invoiceService")
public class InvoiceController {
    InvoiceService invoiceService = new InvoiceService();

    @PostMapping(value = "/process")
    public String processInvoice(@RequestBody String jsonInput) throws ParseException{
        if (jsonInput == null){
            return ("Expecting Json data");
        }
        String invoice_statement = invoiceService.create(jsonInput);
        return invoice_statement;
    }

}
