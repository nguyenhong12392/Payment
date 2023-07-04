package com.payment;

import com.payment.gateway.PaymentGateway;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        PaymentGateway paymentGateway = new PaymentGateway();
        System.out.println("Hi - This is Simple Payment System . To use me, Pls login first. Syntax: LOGIN username password (note : exist account (user1:123456))");
        System.out.println();
        Scanner in = new Scanner(System.in);
        String sessionId = "";
        while (true) {
            String s = in.nextLine();
            if (s.startsWith("LOGIN")) {
                String result = paymentGateway.handleLogin(s);
                if (!result.equals("-1")){
                    sessionId = result;
                }else {
                    System.err.println("Login failed");
                }
            }else if (s.startsWith("EXIST")) {
                System.exit(0);
            }
            else {
                s = sessionId + " " + s;
                System.out.println(paymentGateway.handleMessage(s)) ;
            }

        }
    }
}
