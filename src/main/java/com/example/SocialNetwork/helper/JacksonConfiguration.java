package com.example.SocialNetwork.helper;

import org.springframework.context.annotation.Configuration;

@Configuration
public class JacksonConfiguration {

//    @Bean
//    public ObjectMapper objectMapper(){
//        ObjectMapper objectMapper = new ObjectMapper();
//        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
//
//        objectMapper.registerModule(new ParameterNamesModule());
//        objectMapper.registerModule(new Jdk8Module());
//        objectMapper.registerModule(new JavaTimeModule());
//
//        return objectMapper;
//    }

//    private static final String dateFormat = "yyyy-MM-dd";
//    private static final String dateTimeFormat = "yyyy-MM-dd HH:mm:ss";
//
//    @Bean
//    public Jackson2ObjectMapperBuilderCustomizer jsonCustomizer() {
//        return builder -> {
//            builder.simpleDateFormat(dateTimeFormat);
//            builder.serializers(new LocalDateSerializer(DateTimeFormatter.ofPattern(dateFormat)));
//            builder.serializers(new LocalDateTimeSerializer(DateTimeFormatter.ofPattern(dateTimeFormat)));
//        };
//    }

}
