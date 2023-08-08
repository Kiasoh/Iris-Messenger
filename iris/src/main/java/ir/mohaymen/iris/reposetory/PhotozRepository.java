package ir.mohaymen.iris.reposetory;

import ir.mohaymen.iris.model.TempModel;
import org.springframework.data.repository.CrudRepository;

public interface PhotozRepository extends CrudRepository<TempModel, Integer> {
}
