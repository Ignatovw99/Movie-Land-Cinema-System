package movieland.config.mappings;

import org.modelmapper.ModelMapper;

public interface CustomMappable {

    void configureMappings(ModelMapper modelMapper);
}
