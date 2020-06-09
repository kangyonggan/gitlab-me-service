package com.kangyonggan.gitlab;

import com.kangyonggan.gitlab.constants.AppConstants;
import com.kangyonggan.gitlab.util.Digests;
import com.kangyonggan.gitlab.util.Encodes;
import org.junit.Test;

/**
 * @author kyg
 */
public class AppTest extends AbstractTest {

    @Test
    public void testApp() {
        LOGGER.info("success");
    }

    @Test
    public void genPassword() {
        byte[] salt = Digests.generateSalt(AppConstants.SALT_SIZE);
        LOGGER.info("salt:{}", Encodes.encodeHex(salt));

        byte[] hashPassword = Digests.sha1("root2020".getBytes(), salt, AppConstants.HASH_INTERATIONS);
        LOGGER.info("password:{}", Encodes.encodeHex(hashPassword));
    }

}