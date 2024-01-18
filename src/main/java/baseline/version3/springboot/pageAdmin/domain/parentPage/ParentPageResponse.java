package baseline.version3.springboot.pageAdmin.domain.parentPage;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Date;

public class ParentPageResponse {

    @Getter
    @AllArgsConstructor
    public static class Response {
        private Long parentPageId;
        private String parentPageTitle;
        private String parentPageDescription;
        private String parentPageRootPath;
        private String parentPageIndexPath;

        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
        private Date registerDate;
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
        private Date lastModifyDate;
    }
}