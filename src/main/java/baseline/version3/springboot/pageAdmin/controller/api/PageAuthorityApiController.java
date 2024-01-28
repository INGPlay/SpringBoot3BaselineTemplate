package baseline.version3.springboot.pageAdmin.controller.api;

import baseline.version3.springboot.common.util.ResponseUtil;
import baseline.version3.springboot.common.domain.ResponseForm;
import baseline.version3.springboot.controllerAdvice.exception.CustomValidationException;
import baseline.version3.springboot.pageAdmin.domain.pageAuthority.PageAuthorityRequest;
import baseline.version3.springboot.pageAdmin.domain.pageAuthority.PageAuthorityResponse;
import baseline.version3.springboot.pageAdmin.service.PageAuthorityService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/page-admin/auth")
@RequiredArgsConstructor
public class PageAuthorityApiController {

    private final PageAuthorityService pageAuthorityService;

    @GetMapping
    public ResponseEntity<ResponseForm> findList(){
        List<PageAuthorityResponse.Response> list = pageAuthorityService.findList();
        return ResponseUtil.makeResponseEntity(list);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseForm> findOne(@PathVariable Long id){
        PageAuthorityResponse.Response response = pageAuthorityService.findById(id);
        return ResponseUtil.makeResponseEntity(response);
    }

    @PostMapping
    public ResponseEntity<ResponseForm> createAuth(@Valid @RequestBody PageAuthorityRequest.RequestInsert requestInsert,
                                                   BindingResult bindingResult){
        log.info("bindingResult : {}", bindingResult);
        checkErrors(bindingResult);
        validateDuplicatePageAuthorityCode(requestInsert, bindingResult);

        pageAuthorityService.createAuth(requestInsert);

        return ResponseUtil.makeResponseEntity();
    }

    private void validateDuplicatePageAuthorityCode(PageAuthorityRequest.RequestInsert requestInsert, BindingResult bindingResult) {
        PageAuthorityRequest.RequestDynamicQueryOne requestDynamicQueryOne = new PageAuthorityRequest.RequestDynamicQueryOne(
                requestInsert.getPageAuthorityCode()
        );
        if (pageAuthorityService.findOne(requestDynamicQueryOne).isPresent()){
            FieldError fieldError = new FieldError("duplicated", "pageAuthorityCode", "중복되는 코드가 존재합니다.");
            bindingResult.addError(fieldError);
            throw new CustomValidationException(bindingResult);
        }
    }

    private static void checkErrors(BindingResult bindingResult) {
        if (bindingResult.hasErrors()){
            throw new CustomValidationException(bindingResult);
        }
    }

    @PutMapping
    public ResponseEntity<ResponseForm> updateAuth(@Valid @RequestBody PageAuthorityRequest.RequestUpdate requestUpdate,
                                                   BindingResult bindingResult){
        log.info("bindingResult : {}", bindingResult);
        checkErrors(bindingResult);

        pageAuthorityService.updateAuth(requestUpdate);

        return ResponseUtil.makeResponseEntity();
    }

    @DeleteMapping
    public ResponseEntity<ResponseForm> removeAuth(@RequestBody PageAuthorityRequest.RequestDelete requestDelete){
        pageAuthorityService.delete(requestDelete);
        return ResponseUtil.makeResponseEntity();
    }
}
