package com.zfsoft.certificate;

import com.zfsoft.certificate.pojo.base.Constant;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

/**
 * @author
 */
@EnableConfigurationProperties({Constant.class})
@SpringBootApplication(scanBasePackages = {"com.zfsoft.certificate"})
public class CertificateApplication {

    public static void main(String[] args) {
        SpringApplication.run(CertificateApplication.class, args);
    }

}
