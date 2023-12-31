package baseline.version3.springboot.config.converter;

import baseline.version3.springboot.project.account.domain.AccountSub;
import baseline.version3.springboot.common.domain.subType.EnumBase;
import org.springframework.core.convert.converter.Converter;

public class StringToOAuthTypeConverter implements Converter<String, AccountSub.OAuthTypeEnum> {
    @Override
    public AccountSub.OAuthTypeEnum convert(String s) {
        return EnumBase.getEnumFromString(AccountSub.OAuthTypeEnum.class, s);
    }
}
