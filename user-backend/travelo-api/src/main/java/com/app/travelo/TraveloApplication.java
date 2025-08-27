package com.app.travelo;

import com.app.travelo.config.TwilioConfig;
import com.twilio.Twilio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

import javax.annotation.PostConstruct;

@SpringBootApplication
//@OpenAPIDefinition( info = @Info(title = "Spring boot otp authentication",version = "1.0.0"))
public class TraveloApplication extends SpringBootServletInitializer {
//	@Autowired
//	private UserRepository repository;
	@Autowired
	private TwilioConfig twilioConfig;

	public static void main(String[] args) {
		SpringApplication.run(TraveloApplication.class, args);
	}


	@PostConstruct
	public void initTwilio(){
		System.out.println("Account SID: " + twilioConfig.getAccountSid());
		System.out.println("Account SID: " + twilioConfig.getAuthToken());
		

		Twilio.init(twilioConfig.getAccountSid(),twilioConfig.getAuthToken());
	}

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		return builder.sources(TraveloApplication.class);
	}



//	@PostConstruct
//	public void initUsers() {
//		User user = new User();
//		user.setUserName( "7022752477");
//		List<User> users = Stream.of(user
//		).collect(Collectors.toList());
//		repository.saveAll(users);
//	}
}
