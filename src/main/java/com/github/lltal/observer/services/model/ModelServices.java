package com.github.lltal.observer.services.model;

import com.github.lltal.observer.input.enumeration.AdminActionObjectType;
import com.github.lltal.observer.services.base.ModelService;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class ModelServices {
    private final Map<AdminActionObjectType, ModelService<?, ?>> servicesMap = new HashMap<>();

    public void putService(AdminActionObjectType type, ModelService<?, ?> service) {
        servicesMap.put(type, service);
    }

    public ModelService<?, ?> getService(AdminActionObjectType type) {
        return servicesMap.get(type);
    }
}
