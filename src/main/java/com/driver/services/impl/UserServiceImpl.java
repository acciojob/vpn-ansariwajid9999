package com.driver.services.impl;

import com.driver.model.Country;
import com.driver.model.CountryName;
import com.driver.model.ServiceProvider;
import com.driver.model.User;
import com.driver.repository.CountryRepository;
import com.driver.repository.ServiceProviderRepository;
import com.driver.repository.UserRepository;
import com.driver.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository3;
    @Autowired
    ServiceProviderRepository serviceProviderRepository3;
    @Autowired
    CountryRepository countryRepository3;

    @Override
    public User register(String username, String password, String countryName) throws Exception{
        //create a user of given country. The originalIp of the user should be "countryCode.userId" and return the user.
        // Note that right now user is not connected and thus connected would be false and maskedIp would be null
        //Note that the userId is created automatically by the repository layer
        User user=new User();

        StringBuilder sb=new StringBuilder();
        for(int i=0;i<countryName.length();i++){
            char c=countryName.charAt(i);
            if(c>='a'&&c<='z'){
                c=(char)(c-32);
            }
            sb.append(c);
        }
        String cname= sb.toString();
        if(cname.equals("IND")||cname.equals("USA")||cname.equals("JPN")||cname.equals("CHI")||cname.equals("AUS")){
            user.setUsername(username);
            user.setPassword(password);
            Country country=new Country();

            if(cname.equals("IND")){
                country.setCountryName(CountryName.IND);
                country.setCode(CountryName.IND.toCode());
            }
            if(cname.equals("USA")){
                country.setCountryName(CountryName.USA);
                country.setCode(CountryName.USA.toCode());
            }
            if(cname.equals("JPN")){
                country.setCountryName(CountryName.JPN);
                country.setCode(CountryName.JPN.toCode());
            }
            if(cname.equals("CHI")){
                country.setCountryName(CountryName.CHI);
                country.setCode(CountryName.CHI.toCode());
            }
            if(cname.equals("AUS")){
                country.setCountryName(CountryName.AUS);
                country.setCode(CountryName.AUS.toCode());
            }
            country.setUser(user);
            user.setOriginalCountry(country);
            user.setConnected(false);

            String code=country.getCode()+"."+userRepository3.save(user).getId();
            user.setOriginalIp(code);
            userRepository3.save(user);

        }else{
            throw new Exception("Country not found");
        }
        return user;
    }

    @Override
    public User subscribe(Integer userId, Integer serviceProviderId) {
        //subscribe to the serviceProvider by adding it to the list of providers and return updated User
        ServiceProvider serviceProvider=serviceProviderRepository3.findById(serviceProviderId).get();
        User user=userRepository3.findById(userId).get();
        user.getServiceProviderList().add(serviceProvider);
        serviceProvider.getUsers().add(user);
        serviceProviderRepository3.save(serviceProvider);
        userRepository3.save(user);
        return user;
    }
}