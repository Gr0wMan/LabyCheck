package com.holymoderation.addon.events;

import net.labymod.api.event.Subscribe;
import net.labymod.api.event.events.client.chat.MessageSendEvent;

import com.holymoderation.addon.ChatUtils.ChatManager;
import com.holymoderation.addon.ChatUtils.Colors;
import com.holymoderation.addon.ChatUtils.PunishmentsManager;

public class Settings {

    @Subscribe
    public void OnUpdate(MessageSendEvent event) {
        String message = event.getMessage();
        String command = message.split(" ")[0];
        if (command.equals(".textlist")) {
            event.setCancelled(true);
            ChatManager.ClientMessage(Colors.AQUA + "Список ваших текстов:");
            if (Freezer.GetTexts() == null)
                ChatManager.ClientMessage(Colors.RED + "У вас нет настроенных текстов");
            else
                for (int i = 0; i < Freezer.GetSplitTexts().length; i++)
                {
                    ChatManager.ClientMessage((i+1) + ". " + Freezer.GetSplitTexts()[i]);
                }
        }

        else if (command.equals(".dupeip")) {
            event.setCancelled(true);
            Freezer.SetDupeIp(!Freezer.GetDupeIp());
            if (Freezer.GetDupeIp())
                ChatManager.ClientMessage(Colors.YELLOW + "Автоматический /dupeip" + Colors.GREEN + " ВКЛЮЧЁН");
            else
                ChatManager.ClientMessage(Colors.YELLOW + "Автоматический /dupeip" + Colors.RED + " ВЫКЛЮЧЕН");
        }

        else if (command.equals(".getvk")) {
            event.setCancelled(true);
            if (PunishmentsManager.GetVkUrl() == null) {
                ChatManager.ClientMessage(Colors.RED + "У вас не установлена ссылка на вк!");
                return;
            }
            ChatManager.ClientMessage(Colors.AQUA + "Ваша ссылка на вк: " + PunishmentsManager.GetVkUrl());
        }

        else if (command.equals(".setvk")) {
            event.setCancelled(true);
            if (message.split(" ").length == 1) {
                ChatManager.ClientMessage(Colors.RED + "Вы не ввели ссылку на вк!");
                return;
            }
            String value = message.split(" ", 2)[1];
            boolean hasSpaces = false;
            for (int i = 0; i < value.length(); i++)
                if (value.charAt(i) == ' ')
                    hasSpaces = true;
            if (hasSpaces) {
                ChatManager.ClientMessage(Colors.RED + "В ссылке обнаружены пробелы, пожалуйста, " +
                        "указывайте ссылку на вк в формате 'vk.com/id'");
                return;
            }
            PunishmentsManager.SetVkUrl(value);
            ChatManager.ClientMessage(Colors.GREEN + "Теперь ваша ссылка на вк: " + PunishmentsManager.GetVkUrl());
        }

        else if (command.equals(".textadd")) {
            event.setCancelled(true);
            if (message.split(" ").length == 1) {
                ChatManager.ClientMessage(Colors.RED + "Вы не указали текст!");
                return;
            }
            String value = message.split(" ", 2)[1];
            Freezer.AddText(value);
            ChatManager.ClientMessage(Colors.GREEN + "Вы добавили новый текст!");
        }

        else if (command.equals(".textremove")) {
            event.setCancelled(true);
            if (Freezer.GetTexts() == null) {
                ChatManager.ClientMessage(Colors.RED + "У вас нет настроенных текстов");
                return;
            }
            if (message.split(" ").length == 1) {
                ChatManager.ClientMessage(Colors.RED + "Вы не указали номер текста!");
                return;
            }
            String indexText = message.split(" ", 2)[1];
            if (PunishmentsManager.CheckIncorrectInt(indexText)) {
                ChatManager.ClientMessage(Colors.RED + "Некорректный номер текста!");
                return;
            }
            int index = Integer.parseInt(indexText) - 1;
            if (index >= Freezer.GetSplitTexts().length || index < 0) {
                ChatManager.ClientMessage(Colors.RED
                        + "Элемента с таким номером в списке ваших текстов не существует!");
                return;
            }
            Freezer.RemoveText(index);
            ChatManager.ClientMessage(Colors.RED + "Вы удалили текст номер "
                    + Colors.GREEN + message.split(" ", 0)[1] + "!");
        }

        else if (command.equals(".textedit")) {
            event.setCancelled(true);
            if (Freezer.GetTexts() == null) {
                ChatManager.ClientMessage(Colors.RED + "У вас нет настроенных текстов");
                return;
            }
            if (message.split(" ").length == 1) {
                ChatManager.ClientMessage(Colors.RED + "Вы не указали номер текста и новый текст!");
                return;
            }
            String indexText = message.split(" ")[1];
            if (PunishmentsManager.CheckIncorrectInt(indexText)) {
                ChatManager.ClientMessage(Colors.RED + "Некорректный номер текста!");
                return;
            }
            int index = Integer.parseInt(indexText) - 1;
            if (message.split(" ").length == 2) {
                ChatManager.ClientMessage(Colors.RED + "Вы не указали новый текст!");
                return;
            }
            if (index >= Freezer.GetSplitTexts().length || index < 0) {
                ChatManager.ClientMessage(Colors.RED
                        + "Элемента с таким номером в списке ваших текстов не существует!");
                return;
            }
            String text = message.split(" ", 3)[2];
            Freezer.EditText(index, text);
            ChatManager.ClientMessage(Colors.YELLOW + "Вы изменили текст номер " + Colors.GREEN + (index + 1));
        }

        else if (message.equals(".textclear")) {
            event.setCancelled(true);
            Freezer.ClearTexts();
            ChatManager.ClientMessage(Colors.GREEN + "Вы успешно очистили все тексты!");
        }

        else if (command.equals(".setcords")) {
            event.setCancelled(true);
            if (message.split(" ").length == 1) {
                ChatManager.ClientMessage(Colors.RED + "Вы не указали X и Y координаты!");
                return;
            }
            else if (message.split(" ").length == 2) {
                ChatManager.ClientMessage(Colors.RED + "Вы не указали Y координаты!");
                return;
            }
            String xText = message.split(" ", 3)[1];
            String yText = message.split(" ", 3)[2];
            boolean incorrectX = PunishmentsManager.CheckIncorrectInt(xText);
            boolean incorrectY = PunishmentsManager.CheckIncorrectInt(yText);

            if (incorrectX || incorrectY) {
                if (incorrectX && incorrectY)
                    ChatManager.ClientMessage(Colors.RED + "Некорректные X и Y координаты!");
                else if (incorrectX && !incorrectY)
                    ChatManager.ClientMessage(Colors.RED + "Некорректная X координата!");
                else if (!incorrectX && incorrectY)
                    ChatManager.ClientMessage(Colors.RED + "Некорректная Y координата!");
                return;
            }

            Render.SetxCoords(Integer.parseInt(xText));
            Render.SetyCoords(Integer.parseInt(yText));
            ChatManager.ClientMessage(Colors.GREEN + "Успешно применено!");
        }
    }
}