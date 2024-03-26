package com.gls.gemini.gateway.boot.constants;

import com.gls.gemini.common.core.base.BaseProperties;
import com.gls.gemini.common.core.constant.CommonConstants;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;

/**
 * 网关配置
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ConfigurationProperties(prefix = CommonConstants.BASE_PROPERTIES_PREFIX + ".gateway")
public class GatewayBootProperties extends BaseProperties {
    /**
     * jwt签名密钥
     */
    private String jwkSetUri = "http://localhost:8082/oauth2/jwks";
    /**
     * 忽略的url
     */
    private List<String> ignoreUrls = new ArrayList<>();
}
