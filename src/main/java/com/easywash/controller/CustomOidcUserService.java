package com.easywash.controller;

//@Service
public class CustomOidcUserService// extends OidcUserService
{

	/*
	 * @Autowired private UserDAO userRepository;
	 */

	/*
	 * @Override public OidcUser loadUser(OidcUserRequest userRequest) throws
	 * OAuth2AuthenticationException { OidcUser oidcUser =
	 * super.loadUser(userRequest); Map attributes = oidcUser.getAttributes();
	 * GoogleOAuth2UserInfo userInfo = new GoogleOAuth2UserInfo();
	 * userInfo.setEmail((String) attributes.get("email")); userInfo.setId((String)
	 * attributes.get("sub")); userInfo.setImageUrl((String)
	 * attributes.get("picture")); userInfo.setName((String)
	 * attributes.get("name")); updateUser(userInfo); return oidcUser; }
	 */

	/*
	 * private void updateUser(GoogleOAuth2UserInfo userInfo) { User user =
	 * userRepository.findByEmail(userInfo.getEmail()); if(user == null) { user =
	 * new User(); } user.setEmail(userInfo.getEmail());
	 * user.setImageUrl(userInfo.getImageUrl()); user.setName(userInfo.getName());
	 * // user.setUserType(UserType.google); userRepository.save(user); }
	 */
}
