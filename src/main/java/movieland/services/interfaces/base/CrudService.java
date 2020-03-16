package movieland.services.interfaces.base;

import movieland.domain.models.service.BaseServiceModel;

import java.util.List;

public interface CrudService<T extends BaseServiceModel, I> {

    T create(T serviceModel);

    T update(T serviceModel);

    T delete(I id);

    T findById(T id);

    List<T> findAll();
}
