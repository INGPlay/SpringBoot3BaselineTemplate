package baseline.version3.springboot.pageAdmin.controller;

import baseline.version3.springboot.exceptionHandler.exception.ServiceLayerException;
import baseline.version3.springboot.exceptionHandler.subType.ServiceException;
import baseline.version3.springboot.pageAdmin.domain.parentPage.ParentPageRequest;
import baseline.version3.springboot.pageAdmin.domain.parentPage.ParentPageResponse;
import baseline.version3.springboot.pageAdmin.service.ParentPageService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Controller
@RequestMapping("/page-admin")
@RequiredArgsConstructor
public class PageAdminController {

    private final ParentPageService parentPageService;

    @GetMapping
    public String pageAdminRoot(){
        return "redirect:/page-admin/index";
    }

    @GetMapping("/index")
    public String pageAdminIndex(){
        return "page-admin/index";
    }

    @GetMapping("/page/hierachy")
    public String hierachyPage(){
        return "page-admin/page/hierachy";
    }

    @GetMapping("/develop")
    public String developPage(){
        return "page-admin/develop";
    }
}
