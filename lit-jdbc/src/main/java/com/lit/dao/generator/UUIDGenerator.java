package com.lit.dao.generator;

import com.lit.commons.util.UUIDUtils;

/**
 * User : liulu
 * Date : 2017-3-6 20:36
 * version $Id: UUIDGenerator.java, v 0.1 Exp $
 */
public class UUIDGenerator implements KeyGenerator {
    @Override
    public boolean isGenerateBySql() {
        return false;
    }

    @Override
    public String generateKey(String dbName) {
        return UUIDUtils.getUUID32();
    }
}
