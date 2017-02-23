package controller;

import api.library.LibraryData;
import com.loc.material.api.LocalClassificationService;
import com.loc.material.api.Material;
import com.loc.material.api.MaterialType;
import domain.core.ClassificationApiFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import util.DateUtil;

import java.util.Date;

@RestController
public class LibraryController {
    @PostMapping(value = "/clear")
    public void clear() {
        LibraryData.deleteAll();
    }

    @PostMapping(value = "/use_local_classification")
    public void useLocalClassificationService() {
        ClassificationApiFactory.setService(new LocalClassificationService());
    }

    @PostMapping(value = "/reset_current_date")
    public void resetCurrentDate() {
        DateUtil.resetClock();
    }

    @PostMapping(value = "/current_date")
    public void setCurrentDate(@RequestBody Date date) {
        DateUtil.fixClockAt(date);
    }

    @PostMapping(value = "/materials")
    public void addMaterial(@RequestBody MaterialRequest materialRequest) {
        LocalClassificationService service = (LocalClassificationService) ClassificationApiFactory.getService();
        service.addBook(createMaterial(materialRequest));
    }

    private Material createMaterial(MaterialRequest materialRequest) {
        Material material = new Material();
        material.setSourceId(materialRequest.getSourceId());
        material.setClassification(materialRequest.getClassification());
        material.setTitle(materialRequest.getTitle());
        material.setYear(materialRequest.getYear());
        material.setFormat(MaterialType.valueOf(materialRequest.getFormat()));
        material.setAuthor(materialRequest.getAuthor());
        return material;
    }
}
