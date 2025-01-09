package com.ums.util;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import com.ums.entity.User;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class CacheHelper {

	@Cacheable(cacheNames = "otps", key = "#username")
	public  Integer otpCache(int otp,String username){

		log.info("-----------------------------------------otpcache : " + otp );
		return  otp;
	}

	@CacheEvict(cacheNames = "otps", key = "#username")
	public  Integer removeOtpCache(String username){

		log.info("-----------------------------------------removecache : "  );
		return  0;
	}
	
	@Cacheable(cacheNames = "otps", key = "#username")
	public int  getOtpCache(String username){

		log.info("-----------------------------------------getOtp ");
		return  0;
	}

	@Cacheable(cacheNames = "nonverifiedusers", key = "#user.username")
	public  User userCache(User user){
		
		log.info("-----------------------------------------usercache : " + user );
		return  user;
	}
	
	@CacheEvict(cacheNames = "nonverifiedusers", key = "#username")
	public  String removeUserCache(String username){
		log.info("-----------------------------------------removeUser ");
		return  username;
	}

	@Cacheable(cacheNames = "nonverifiedusers", key = "#username")
	public User getUserCache(String username){
		
		log.info("-----------------------------------------getusercache : ");
		return new User();
	}

}
