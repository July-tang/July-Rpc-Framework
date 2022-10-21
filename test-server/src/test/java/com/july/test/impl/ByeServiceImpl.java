package com.july.test.impl;

import com.july.rpc.api.ByeService;

/**
 * @author july
 */
public class ByeServiceImpl implements ByeService {
    @Override
    public String bye(String name) {
        return "Bye bye " + name;
    }
}
