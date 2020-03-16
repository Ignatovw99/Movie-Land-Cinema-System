package movieland.services.validation;

import movieland.domain.models.service.BaseServiceModel;

public interface Validateable<T extends BaseServiceModel> {

    boolean isValid(T serviceModel);
}
