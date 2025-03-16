package com.github.lltal.observer.service.front.ui;

import com.github.lltal.filler.starter.callback.CallbackData;
import com.github.lltal.filler.starter.callback.CallbackDataSender;
import com.github.lltal.filler.starter.command.CommandContext;
import com.github.lltal.filler.starter.util.KeyboardUtil;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.util.Collection;
import java.util.function.Supplier;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class UiHelper {
    public static final String PHONE_NUMBER_PATTERN = "^\\+79\\d{9}$";
    public static final String CAR_NUMBER_PATTERN = "^[а-я][0-9]{3}[а-я]{2}[0-9]{3}$";
    public static final Pattern PHONE_NUMBER = Pattern.compile(PHONE_NUMBER_PATTERN);
    public static final Pattern CAR_NUMBER = Pattern.compile(CAR_NUMBER_PATTERN);

    public void closeCb(CommandContext context) {
        AnswerCallbackQuery answer = AnswerCallbackQuery
                .builder()
                .callbackQueryId(
                        context
                                .getUpdate()
                                .getCallbackQuery()
                                .getId())
                .build();
        context.getEngine().executeNotException(answer);
    }

    public BotApiMethod<?> createKeyboard(
            String text,
            Long chatId,
            Collection<String> buttonsMd
    ) {
        CallbackDataSender[][] buttons =
                buttonsMd.stream()
                        .map(md-> new CallbackDataSender(
                                    md,
                                    new CallbackData(md, "")
                                )
                        )
                        .map(sender -> new CallbackDataSender[]{sender})
                        .toArray((value -> new CallbackDataSender[value][1]));

        return SendMessage
                .builder()
                .text(text)
                .replyMarkup(KeyboardUtil.inline(buttons))
                .chatId(chatId)
                .build();
    }

    public SendMessage createMessage(Long chatId, String text) {
        return SendMessage
                .builder()
                .chatId(chatId)
                .text(text)
                .build();
    }

    public boolean matchPhoneNumber(String candidate) {
        Matcher matcher = PHONE_NUMBER.matcher(candidate);
        return matcher.find();
    }

    public boolean matchCarNumber(String candidate) {
        Matcher matcher = CAR_NUMBER.matcher(candidate);
        return matcher.find();
    }

    public void sendMessage(CommandContext context, BotApiMethod<?> message) {
        context.getEngine().executeNotException(message);
    }
}
