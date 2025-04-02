package com.github.lltal.observer.service.front.base;

import com.github.lltal.observer.config.constant.enumeration.AdminActionObjectType;
import com.github.lltal.observer.config.constant.enumeration.AdminActionType;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * Мапа служб по работе с dto, связанными с действиями админа
 */
@Service
public class PrivateFrontServices {
    private final Map<
            AdminActionObjectType,
            PrivateFrontService
            > creationServices = new HashMap<>();

    private final Map<
            AdminActionObjectType,
            PrivateFrontService
            > deletionServices = new HashMap<>();

    public void putCreationService(
            AdminActionObjectType type,
            PrivateFrontService service
    ) {
        creationServices.put(type, service);
    }

    public void putDeletionService(
            AdminActionObjectType type,
            PrivateFrontService service
    ) {
        deletionServices.put(type, service);
    }

    public PrivateFrontService getService(
            AdminActionType actionType,
            AdminActionObjectType objectType
    ) {
        return actionType == AdminActionType.ADD
                ?
                    creationServices.get(objectType)
                :
                    deletionServices.get(objectType);

    }
}
