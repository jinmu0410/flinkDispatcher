package com.jm.flink.helper;

import org.apache.flink.configuration.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * @author tasher
 * @description
 * @date 2022/2/24
 */
public class SecurityHelper {

    private static final Logger logger = LoggerFactory.getLogger(SecurityHelper.class);

    public static boolean kerberosVerify(Configuration flinkConfig) throws IOException {
        // 进行kerberos验证 如果需要的话
//        KerberosInfo kerberosInfo = new KerberosInfo(launcherOptions.getKrb5conf(),
//                launcherOptions.getKeytab(), launcherOptions.getPrincipal(), flinkConfig);
//        kerberosInfo.verify();
//
//        final UserGroupInformation currentUser = UserGroupInformation.getCurrentUser();
//        if (HadoopUtils.isKerberosSecurityEnabled(currentUser)) {
//            boolean useTicketCache =
//
//                    flinkConfiguration.getBoolean(SecurityOptions.KERBEROS_LOGIN_USETICKETCACHE);
//
//            if (!HadoopUtils.areKerberosCredentialsValid(currentUser, useTicketCache)) {
//                throw new RuntimeException(
//                        "Hadoop security with Kerberos is enabled but the login user "
//                                + "does not have Kerberos credentials or delegation
//                        tokens !");
//            }
//        }
        return false;
    }
}
