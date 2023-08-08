package ir.mohaymen.iris.service;

import ir.mohaymen.iris.model.TempModel;
import ir.mohaymen.iris.reposetory.PhotozRepository;
import org.springframework.stereotype.Service;

@Service
public class TempService {
    private final PhotozRepository tempModel;

    public TempService(PhotozRepository tempModel) {
        this.tempModel = tempModel;
    }
}
