package com.github.lltal.observer.service.front.base.internal;

import com.github.lltal.filler.shared.ifc.AbstractSender;
import com.github.lltal.filler.shared.ifc.Countable;
import com.github.lltal.filler.starter.command.CommandContext;
import com.github.lltal.observer.input.enumeration.YesNo;
import com.github.lltal.observer.input.enumeration.converter.AdminActionObjectTypeConverter;
import com.github.lltal.observer.input.enumeration.converter.AdminActionTypeConverter;
import com.github.lltal.observer.input.enumeration.converter.YesNoConverter;
import com.github.lltal.observer.input.dto.AdminDto;
import com.github.lltal.observer.service.front.base.FrontService;
import com.github.lltal.observer.service.front.base.PrivateFrontService;
import com.github.lltal.observer.service.front.base.PrivateFrontServices;
import com.github.lltal.observer.service.front.ui.ContextParser;
import com.github.lltal.observer.service.front.ui.UiHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import static com.github.lltal.observer.config.constant.SenderName.ADMIN_SENDER_NAME;

@Service
@RequiredArgsConstructor
public class AdminFrontService implements FrontService<AdminDto> {
    private final ContextParser parser;
    private final UiHelper helper;
    private final YesNoConverter yesNoConverter;
    private final AdminActionObjectTypeConverter adminActionObjectTypeConverter;
    private final AdminActionTypeConverter adminActionTypeConverter;
    private final PrivateFrontServices privateFrontServices;
    @Qualifier(ADMIN_SENDER_NAME)
    @Autowired
    private AbstractSender sender;


    @Override
    public boolean isFullFill(AdminDto dto) {
        return dto.getCount() > 2;
    }

    @Override
    public void sendNextMessage(AdminDto dto, CommandContext context) {
        helper.sendMessage(
                context,
                sender.getNextMessage(dto, parser.getChatId(context))
        );
    }

    @Override
    public void fillDto(AdminDto dto, CommandContext context) {
        switch (dto.getCount()) {
            case 0 -> execYesNo(dto, context);
            case 1 -> execObjectType(dto, context);
            case 2 -> execActionType(dto, context);
            default -> throw new IllegalStateException("count out of range during dto filling");
        }
    }

    private void execYesNo(AdminDto dto, CommandContext context) {
        helper.closeCb(context);
        dto.setYesNo(
                yesNoConverter.convertToEnum(
                        context.getName()
                )
        );
        dto.setCount(dto.getCount() + 1);

        if (dto.getYesNo() == YesNo.NO) {
            dto.setCount(3);
            helper.sendMessage(
                    context,
                    helper.createMessage(
                            parser.getChatId(context),
                            "Выполнение команды /admin завершено"
                    )
            );

            context.getUserBotSession().stop();
        }
    }

    private void execObjectType(AdminDto dto, CommandContext context) {
        helper.closeCb(context);
        dto.setObjectType(
                adminActionObjectTypeConverter.convertToEnum(
                        context.getName()
                )
        );
        dto.setCount(dto.getCount() + 1);
    }

    private void execActionType(AdminDto adminDto, CommandContext context) {
        helper.closeCb(context);
        adminDto.setActionType(
                adminActionTypeConverter.convertToEnum(
                        context.getName()
                )
        );
        adminDto.setCount(adminDto.getCount() + 1);

        PrivateFrontService service =
                privateFrontServices.getService(
                        adminDto.getActionType(),
                        adminDto.getObjectType()
                );

        Countable manageableDto = service.createManageableDto();

        adminDto.setNewValue(manageableDto);
        adminDto.setCount(adminDto.getCount() + 1);

        service.sendNextMessage(manageableDto, context);
    }
}
