package ir.mohaymen.iris.auth;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

@Service
public class SMSService {
    @Value("${application.security.sms.sender-number}")
    private String senderNumber;
    @Value("${application.security.sms.username}")
    private String username;
    @Value("${application.security.sms.password}")
    private String password;
    @Value("${application.security.sms.enabled}")
    private boolean isEnabled;
    private Logger logger= LoggerFactory.getLogger(SMSService.class);;
    public void sendSms(String number, String message)
    {
        logger.info(MessageFormat.format("sms {0} to phone number:{1} with message:{2}",(isEnabled?"sent":"not sent"),number,message));
        if(!isEnabled) return;
        try {
            sendSmsMessageWithPost(number, message, username, password, senderNumber);
        }
        catch (Exception e){
            System.err.println(e.getMessage());
        }
    }


    private int sendSmsMessageWithPost(String phone, String message, String username, String password,
                                       String senderPhoneNumber) throws Exception {
        int resultCode = -1;

        HttpClient client = HttpClientBuilder.create().build();
        List<NameValuePair> formParams = new ArrayList<>();
        formParams.add(new BasicNameValuePair("UserName", username));
        formParams.add(new BasicNameValuePair("Password", password));
        formParams.add(new BasicNameValuePair("PhoneNumber", senderPhoneNumber));
        formParams.add(new BasicNameValuePair("MessageBody", message));
        formParams.add(new BasicNameValuePair("RecNumber", phone));
        formParams.add(new BasicNameValuePair("SmsClass", "1"));

        String url = "https://RayganSMS.com/SendMessageWithPost.ashx";
        HttpPost postRequest = new HttpPost(url);
        postRequest.setEntity(new UrlEncodedFormEntity(formParams));

        HttpResponse response = client.execute(postRequest);

        if (response.getStatusLine().getStatusCode() == 200) {
            String result = EntityUtils.toString(response.getEntity());
            System.out.println("SMS with message: " + message + " sent to Number: " + phone + " successfully!");
            try {
                resultCode = Integer.parseInt(result);
            } catch (NumberFormatException e) {
                // Handle parsing error
            }
        } else {
            throw new Exception("خطایی در ارسال پیامک به وجود آمده است.");
        }
        return resultCode;
    }
}
