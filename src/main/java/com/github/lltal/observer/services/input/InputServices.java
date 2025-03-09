package com.github.lltal.observer.services.input;

import com.github.lltal.observer.input.enumeration.AdminActionObjectType;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class InputServices {
    private final Map<AdminActionObjectType, InputService> servicesMap = new HashMap<>();

    public void putService(AdminActionObjectType type, InputService service) {
        servicesMap.put(type, service);
    }

    public InputService getService(AdminActionObjectType type) {
        return servicesMap.get(type);
    }
}
