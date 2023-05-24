package edu.spring.ex06.controller;

import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import edu.spring.ex06.service.FollowService;
import edu.spring.ex06.service.UserInfoService;

@RestController
@RequestMapping(value = "/users")
public class UserRESTController {
	private static final Logger logger = 
			LoggerFactory.getLogger(UserRESTController.class);
	
	@Autowired
	private UserInfoService userinfoService;
	
	@Autowired
	private FollowService followService;
	
	@Autowired
	private JavaMailSenderImpl mailSender;
	
	private int authNumber;
	
	@GetMapping("/userId/{userId}")
	public ResponseEntity<Integer> readUserId(
			@PathVariable("userId") String userId){
		int result = userinfoService.readUserId(userId);
		return new ResponseEntity<Integer>(result,HttpStatus.OK);
	} 
	//@GetMapping("/userEmail/{userEmail:.+}")  //
	@GetMapping("/userEmail/{email1}/{email2}")
	public ResponseEntity<Integer> readUserEmail(
			@PathVariable("email1") String email1, @PathVariable("email2") String email2){
		String userEmail = email1+"."+email2;
		int result = userinfoService.readUserEmail(userEmail);
		return new ResponseEntity<Integer>(result,HttpStatus.OK);
	}

	@PostMapping("/emailVerif")
	public ResponseEntity<Integer>emailVerif(@RequestBody String userEmail){
		logger.info("emailVerif()");
		logger.info(userEmail);
		joinEmail(userEmail);
		return new ResponseEntity<Integer>(authNumber,HttpStatus.OK);

	}
	@GetMapping("/followCheck/{userinfoUserId}")
	public ResponseEntity<Integer> readFollowing(@PathVariable("userinfoUserId") String userinfoUserId){
		int result=1;
		return new ResponseEntity<Integer>(result,HttpStatus.OK);
	}
	
	@PostMapping("/follow")
	public ResponseEntity<Integer>follow(@RequestBody Map<String,Object> param){
		logger.info("follow() ");
		logger.info("userinfoUserId : "+param.get("userinfoUserId").toString());
		logger.info("userId"+param.get("userId").toString());
	
		String userinfoUserId = (String) param.get("userinfoUserId");
		String userId = (String) param.get("userId");
		int result = followService.create(userId, userinfoUserId);
		return new ResponseEntity<Integer>(result,HttpStatus.OK);
	}
		

	public void makeRandomNumber() {
		// 난수의 범위 111111 ~ 999999 (6자리 난수)
		Random r = new Random();
		int checkNum = r.nextInt(888888) + 111111;
		System.out.println("인증번호 : " + checkNum);
		authNumber = checkNum;
	}
	
	
			//이메일 보낼 양식! 
	public void joinEmail(String userEmail) {
		makeRandomNumber();
		String setFrom = "hennei@naver.com"; // email-config에 설정한 자신의 이메일 주소를 입력 
		String toMail = userEmail;
		String title = "회원 가입 인증 이메일 입니다."; // 이메일 제목 
		String content = 
				"홈페이지를 방문해주셔서 감사합니다." + 	//html 형식으로 작성 ! 
                "<br><br>" + 
			    "인증 번호는 " + authNumber + "입니다." + 
			    "<br>" + 
			    "해당 인증번호를 인증번호 확인란에 기입하여 주세요."; //이메일 내용 삽입
		mailSend(setFrom, toMail, title, content);
		
	}
	
	//이메일 전송 메소드
	public void mailSend(String setFrom, String toMail, String title, String content) { 
		MimeMessage message = mailSender.createMimeMessage();
		// true 매개값을 전달하면 multipart 형식의 메세지 전달이 가능.문자 인코딩 설정도 가능하다.
		try {
			MimeMessageHelper helper = new MimeMessageHelper(message,true,"utf-8");
			helper.setFrom(setFrom);
			helper.setTo(toMail);
			helper.setSubject(title);
			// true 전달 > html 형식으로 전송 , 작성하지 않으면 단순 텍스트로 전달.
			helper.setText(content,true);
			mailSender.send(message);
		} catch (MessagingException e) {
			e.printStackTrace();
		}
	}
}
