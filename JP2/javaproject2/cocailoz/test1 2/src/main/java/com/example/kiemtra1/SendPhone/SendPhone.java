//package com.example.kiemtra1.SendPhone;
//
//import com.twilio.Twilio;
//import com.twilio.rest.api.v2010.account.Message;
//import com.twilio.type.PhoneNumber;
//import org.springframework.beans.factory.annotation.Value;
//
//public class SendPhone {
//    @Value("${ACCount_SId}")
//    private String ACCount_SId1;
//    @Value("${Auth_Token}")
//    private String Auth_Token1;
//
//    public void Sendphone(String phone,String body) {
//        Twilio.init(ACCount_SId1, Auth_Token1);
//        String myphone = "0338010536";
//        String tophone = phone;
//        String messageBody = body;
//        Message message = Message.creator(
//                new PhoneNumber(tophone),new PhoneNumber(myphone),messageBody
//        ).create();
//        System.out.println("Tin nhắn đã được gửi: " + message.getSid());
//    }
//
//}
