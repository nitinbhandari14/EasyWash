package com.easywash.basic;

import javax.jms.ConnectionFactory;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.jms.DefaultJmsListenerContainerFactoryConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.config.JmsListenerContainerFactory;
import org.springframework.jms.support.converter.MappingJackson2MessageConverter;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.jms.support.converter.MessageType;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.util.ErrorHandler;
@EnableJms
//testCommit
//@EnableAutoConfiguration(exclude = {org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration.class})
//@SpringBootApplication(scanBasePackages = { "com.easywash"},exclude = { SecurityAutoConfiguration.class })
@SpringBootApplication(scanBasePackages = { "com.easywash"})
@ComponentScan({"com.easywash.controller"})
@EntityScan("com.easywash.model")
@EnableJpaRepositories("com.easywash.dao")
@EnableScheduling
//@ComponentScan({"com.easywash.security"})
//@EnableAutoConfiguration(exclude = [SecurityFilterAutoConfiguration, SpringBootWebSecurityConfiguration])
//@EnableAutoConfiguration(exclude = {WebSecurityConfiguration.class})
//
//@EnableWebSecurity

public class EComApplication {
	/*
	 * @Autowired private UserDetailsService userDetailsService;
	 */
	//private static final Logger LOGGER = LogManager.getLogger(EComApplication.class);

	public static void main(String[] args) {
		/*
		 * 
		 * ConfigurableApplicationContext
		 * context=SpringApplication.run(EComApplication.class, args); MyBean
		 * bean=context.getBean(MyBean.class); bean.test();
		 */
		SpringApplication.run(EComApplication.class, args);
		/*
		 * LOGGER.info("Info level log message");
		 * LOGGER.debug("Debug level log message");
		 * LOGGER.error("Error level log message");
		 */
		
	}
	
	
	  @Bean
	  public JmsListenerContainerFactory<?> myFactory(
	      ConnectionFactory connectionFactory,
	      DefaultJmsListenerContainerFactoryConfigurer configurer) {
	    DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();

	    // anonymous class
	    factory.setErrorHandler(
	        new ErrorHandler() {
	          @Override
	          public void handleError(Throwable t) {
	            System.err.println("An error has occurred in the transaction");
	          }
	        });

	    // lambda function
	    factory.setErrorHandler(t -> System.out.println("An error has occurred in the transaction"));

	    configurer.configure(factory, connectionFactory);
	    return factory;
	  }

	  // Serialize message content to json using TextMessage
	  @Bean
	  public MessageConverter jacksonJmsMessageConverter() {
	    MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();
	    converter.setTargetType(MessageType.TEXT);
	    converter.setTypeIdPropertyName("_type");
	    return converter;
	  }
	
		/*
		 * @Bean public BCryptPasswordEncoder bCryptPasswordEncoder() { return new
		 * BCryptPasswordEncoder(); }
		 */
	   
		
		/*
		 * @Bean public SpringTemplateEngine springTemplateEngine() {
		 * SpringTemplateEngine templateEngine = new SpringTemplateEngine();
		 * templateEngine.addTemplateResolver(htmlTemplateResolver()); return
		 * templateEngine; }
		 */
		/*
		 * @Bean public SpringResourceTemplateResolver htmlTemplateResolver(){
		 * SpringResourceTemplateResolver emailTemplateResolver = new
		 * SpringResourceTemplateResolver(); emailTemplateResolver.setPrefix("/");
		 * emailTemplateResolver.setSuffix(".html"); //
		 * 
		 * emailTemplateResolver.setTemplateMode(TemplateMode.HTML);
		 * emailTemplateResolver.setCharacterEncoding(StandardCharsets.UTF_8.name());
		 * return emailTemplateResolver; }
		 */
		 
		 
	  //6LdO1qcZAAAAACCOyNLq4qddToS0tlBwu1Z_FNh7 site
	  //6LdO1qcZAAAAAHPdXyDp2azN-HLFt7z-LkKi4_4m secret

}
