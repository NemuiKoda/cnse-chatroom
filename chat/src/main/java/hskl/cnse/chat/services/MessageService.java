package hskl.cnse.chat.services;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import hskl.cnse.chat.db.dto.MessageCreationData;
import hskl.cnse.chat.db.dto.MessageDTO;
import hskl.cnse.chat.db.model.*;
import hskl.cnse.chat.db.repositories.ChatRepository;
import hskl.cnse.chat.db.repositories.MessageRepository;
import hskl.cnse.chat.db.repositories.UserRepository;

@Service
public class MessageService {
    @Autowired
    private MessageRepository messageRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ChatRepository chatRepository;
    private final Logger logger = LoggerFactory.getLogger(MessageService.class);

    public List<MessageDTO> getMessagesByChatId(@NonNull Long chatId) {
        List<Message> messages = messageRepository.findByChat_Id(chatId);
        return messages.stream()
                .map(MessageDTO::new)
                .peek(messageDTO -> logger.info("MessageService.getMessagesByChatId: MessageDTO: " + messageDTO.toString()))
                .collect(Collectors.toList());
    }

    public Message sendMessage(MessageCreationData messageCreationData) {
        logger.info("*************************************************************");
        logger.info("*************************************************************");
        logger.info("MessageService.sendMessage() called");
        logger.info("MessageCreationData: " + messageCreationData.toString());
        logger.info("*************************************************************");
        logger.info("*************************************************************");
        Message message = new Message();
        message.setContent(messageCreationData.getContent());
        Chat chat = chatRepository.findById(messageCreationData.getChat_id()).orElse(null);
        message.setChat(chat);
        AuthUser user = userRepository.findById(messageCreationData.getUser_id()).orElse(null);
        message.setUser(user);
        logger.info("Message: " + message.toString());
        logger.info("*************************************************************");
        logger.info("*************************************************************");

        return messageRepository.save(message);
    }

    public Message editMessage(MessageCreationData messageCreationData, @NonNull Long message_id) {
        Message message = getMessageById(message_id);
        message.setContent(messageCreationData.getContent());

        return messageRepository.save(message);
    }

    public Message getMessageById(@NonNull Long message_id) {
        return messageRepository.findById(message_id).orElse(null);
    }

    @Transactional
    public void deleteMessage(@NonNull Long message_id) {
        messageRepository.deleteById(message_id);
    }

    @Transactional
    public void deleteChatHistoryForAllUsers(@NonNull Long chat_id) {
        // Nachrichtenverlauf für alle Nutzer löschen
        messageRepository.deleteByChat_Id(chat_id);
    }
}
