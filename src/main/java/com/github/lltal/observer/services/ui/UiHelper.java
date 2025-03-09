package com.github.lltal.observer.services.ui;

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
    public static final Pattern phoneNumber = Pattern.compile("^\\+79\\d{9}$");

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
            Collection<Supplier<String>> providers
    ) {
        CallbackDataSender[][] buttons =
                providers.stream()
                        .map(s -> new CallbackDataSender(
                                s.get(),
                                new CallbackData(
                                        s.get(), "")))
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
        Matcher matcher = phoneNumber.matcher(candidate);
        return matcher.find();
    }
}
