package com.springBootCoding.CodingTech.entity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.springBootCoding.CodingTech.repo.RoleRepository;

import lombok.Data;

@Data
public class CustomUserDetails
extends User

implements UserDetails {

	private String username;
    private String password;
    private Set<Role> roles;

//    private final User user;
    @Autowired
    RoleRepository roleRepository;
//	public CustomUserDetails(User user) {
//		this.user = user;
//	}
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();
//        for (Role role :
//        	roles
//        	) {
//        	if(role.getPrivilege() != null && role.getPrivilege().size()>0) {
//        	role.getPrivilege().forEach(privilege->{
//        		authorities.add(new SimpleGrantedAuthority(privilege.getName()));
//        	});
//        }
//            authorities.add(new SimpleGrantedAuthority(role.getName()));
//        }
        return authorities;
    }

    // Other methods omitted for brevity
	
    @Override
	  public String getPassword() {
	    return password;
	  }

	  @Override
	  public String getUsername() {
	    return username;
	  }

	  @Override
	  public boolean isAccountNonExpired() {
	    return true;
	  }

	  @Override
	  public boolean isAccountNonLocked() {
	    return true;
	  }

	  @Override
	  public boolean isCredentialsNonExpired() {
	    return true;
	  }

	  @Override
	  public boolean isEnabled() {
	    return true;
	  }


}
