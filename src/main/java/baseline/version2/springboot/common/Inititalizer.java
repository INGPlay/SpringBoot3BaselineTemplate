package baseline.version2.springboot.common;

import baseline.version2.springboot.account.domain.RegisterAccountDTO;
import baseline.version2.springboot.account.domain.subType.RoleEnum;
import baseline.version2.springboot.account.repository.AccountRepository;
import baseline.version2.springboot.account.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class Inititalizer implements InitializingBean {

    private final AccountRepository accountRepository;
    private final AccountService accountService;

    @Override
    public void afterPropertiesSet() throws Exception {

        if (isInAccountName("admin")) {
            RegisterAccountDTO managerDTO = new RegisterAccountDTO();
            managerDTO.setAccountRole(RoleEnum.MANAGER);
            managerDTO.setAccountName("admin");
            managerDTO.setAccountPassword("admin");
            managerDTO.setAccountDisplayName("ADMIN");
            accountService.insertLocalAccount(managerDTO);
        }
    }

    private boolean isInAccountName(String accountName) {
        return accountRepository.findByAccountName(accountName).isEmpty();
    }
}
