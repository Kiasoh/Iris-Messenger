package ir.mohaymen.iris.config;

import ir.mohaymen.iris.message.EditMessageDto;
import ir.mohaymen.iris.message.Message;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelMapperConfig {
    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper=new ModelMapper();
        configMapping(modelMapper);
        return modelMapper;
    }
    private void configMapping(ModelMapper modelMapper){
//        PropertyMap<EditMessageDto , Message> orderMap = new PropertyMap<EditMessageDto, Message>() {
//            @Override
//            protected void configure() {
//                map().setText(EditMessa);
//            }
//        }
//        modelMapper.typeMap(EditMessageDto.class , Message.class)
    }

}
