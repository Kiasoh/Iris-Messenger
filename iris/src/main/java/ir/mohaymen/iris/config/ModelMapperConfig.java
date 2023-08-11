package ir.mohaymen.iris.config;

import ir.mohaymen.iris.user.User;
import ir.mohaymen.iris.user.UserCreationDto;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.print.attribute.standard.Destination;

@Configuration
public class ModelMapperConfig {
    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper=new ModelMapper();
        configMapping(modelMapper);
        return modelMapper;
    }
    private void configMapping(ModelMapper modelMapper){
    }

}
