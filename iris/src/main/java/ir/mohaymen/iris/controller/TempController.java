package ir.mohaymen.iris.controller;

import ir.mohaymen.iris.model.TempModel;
import ir.mohaymen.iris.service.TempService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TempController {
    private final TempService tempModel;

    public TempController(TempService tempModel) {
        this.tempModel = tempModel;
    }

    @GetMapping("/")
    public String haha() {
        return "brother this guy sucks!";
    }
}
