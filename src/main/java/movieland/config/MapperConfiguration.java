package movieland.config;

import movieland.config.mappings.MappingsInitializer;
import org.modelmapper.Conditions;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MapperConfiguration {

    private static ModelMapper modelMapper;

    static {
        modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setPropertyCondition(Conditions.isNotNull());
        MappingsInitializer.initMappings(modelMapper);
//        modelMapper.validate();
    }

    @Bean
    public ModelMapper modelMapper() {
        return modelMapper;
    }
}
