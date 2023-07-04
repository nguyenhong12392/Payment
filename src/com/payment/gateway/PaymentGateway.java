package com.payment.gateway;

import com.payment.api.PaymentAPI;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
public class PaymentGateway {

    private final PaymentAPI paymentAPI = new PaymentAPI();

    public String handleLogin(String message){
        Command command = translate(message);
        return String.valueOf(handleLogin(command.getParams()));
    }

    public String handleMessage(String message) {
        Command command = translate(message);
        if (command.getSessionId() == 0) {
            System.out.println("YOU MUST LOGIN FIRST");
            return "";
        }

        switch (command.getCommand()) {
            case "CASH_IN":
                 return handleCashIn(command.getSessionId(),command.getParams());
            case "LIST_BILL":
                return handleListBill(command.getSessionId());
            case "PAY":
                return handlePay(command.getSessionId(),command.getParams());
            case "DUE_DATE":
                return handleDueDate(command.getSessionId());
            case "SCHEDULE":
                return handleSchedule(command.getSessionId(),command.getParams());
            case "LIST_SCHEDULE":
                return handleListSchedule(command.getSessionId());
            case "LIST_PAYMENT":
                 return handleListPayment(command.getSessionId());
            case "SEARCH_BILL_BY_PROVIDER":
                 return handleSearchBillByProvider(command.getSessionId(),command.getParams());
            default:
                System.out.println("COMMAND IS NOT SUPPORTED");
        }
        return "";
    }

    private String handleListSchedule(long sessionId) {
        return paymentAPI.listSchedule(sessionId);
    }

    private long handleLogin(List<String> params) {
        if (params.size()< 2)
            return  -1;
        else {
            String username = params.get(0);
            String password = params.get(1);
            return paymentAPI.login(username,password);
        }
    }

    private String handleSearchBillByProvider(long sessionId,List<String> params) {
        if (params.size() < 1)
            return  "U MUST ADD PROVIDER NAME";
        else {
            String providerName = params.get(0);
            return paymentAPI.listBillByProvider(sessionId,providerName);
        }
    }

    private String handleListPayment(long sessionId) {
        return paymentAPI.listPayment(sessionId);
    }

    private String handleSchedule(long sessionId,List<String> params) {
        if (params.size() < 2)
            return  "U MUST ADD BILL.NO AND SCHEDULE DATE";
        else {
            long billNoId ;
            try {
                billNoId = Long.parseLong(params.get(0));
                if (billNoId == 0)  return  "BILL NO. MUST BE > 0";
            }catch (Exception e) {
                return  "BILL NO. MUST BE A NUMBER";
            }
            LocalDate scheduleDate ;
            try {
                String param = params.get(1) ;
                scheduleDate =  LocalDate.parse(param, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                if (scheduleDate.isBefore(LocalDate.now()))
                    return  "SCHEDULE DATE MUST BE AFTER NOW";
            }catch (Exception e) {
                return  "PLEASE CORRECT SCHEDULE DATE ( format dd/MM/yyyy)";
            }
            return paymentAPI.schedule(sessionId,billNoId, scheduleDate);
        }
    }

    private String handleDueDate(long sessionId) {
        return paymentAPI.listBillByDueDate(sessionId);
    }

    private String handlePay(long sessionId,List<String> params) {
        if (params.isEmpty())
            return  "BILL NO. MUST HAVE ONE OR MORE VALUE";
        else {
            List<Long> payments = new ArrayList<>();
            for (String param : params) {
                try {
                    payments.add(Long.parseLong(param));
                }catch (Exception e) {
                    return  "BILL NO. MUST BE A NUMBER";
                }
            }
            return paymentAPI.payment(sessionId,payments);
        }
    }

    private String handleListBill(long sessionId) {
        return paymentAPI.listBill(sessionId);
    }

    private String handleCashIn(long sessionId,List<String> params) {
        if (params.isEmpty())
            return  "BALANCE MUST NOT BE EMPTY";
        else {
            String balance = params.get(0);
            double balanceNum ;
            try {
                balanceNum = Double.parseDouble(balance);
                return paymentAPI.addBalance(sessionId,balanceNum);
            }catch (Exception e) {
                return  "BALANCE MUST BE NUMBER";
            }

        }
    }

    public static class Command {
        private final long sessionId;
        private final String command;
        private final List<String> params;

        public Command(long sessionId, String command, List<String> params) {
            this.sessionId = sessionId;
            this.command = command;
            this.params = params;
        }

        public long getSessionId() {
            return sessionId;
        }

        public String getCommand() {
            return command;
        }

        public List<String> getParams() {
            return params;
        }
    }

    public Command translate(String message) {
        long sessionId = 0 ;
        String command = "";
        List<String> param = new ArrayList<>();;
        StringTokenizer tokenizer = new StringTokenizer(message, " ");
        if (!message.startsWith("LOGIN")) {
            try {
                sessionId =  Long.parseLong(tokenizer.nextToken());
            } catch (NumberFormatException ignored) {

            }
        }
        while (tokenizer.hasMoreTokens()) {
            if (command.equals(""))
                command = tokenizer.nextToken();
            else
                param.add(tokenizer.nextToken());
        }
        return new Command(sessionId, command, param);
    }
}
