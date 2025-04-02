package com.github.lltal.observer.service.front.base;

import com.github.lltal.filler.shared.ifc.Countable;
import com.github.lltal.observer.config.constant.enumeration.AdminActionObjectType;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Служба по работе с dto, связанным с действиями админа
 */
public interface PrivateFrontService extends FrontService<Countable> {
    @Autowired
    default void regService(PrivateFrontServices privateFrontServices) {
        if (isCreationService())
            privateFrontServices.putCreationService(
                    getActionObjectType(), this
            );
        else
            privateFrontServices.putDeletionService(
                    getActionObjectType(), this
            );
    }

    boolean isCreationService();

    AdminActionObjectType getActionObjectType();

    Countable createManageableDto();
}
