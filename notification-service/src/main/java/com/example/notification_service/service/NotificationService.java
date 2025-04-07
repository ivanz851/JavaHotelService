package com.example.notification_service.service;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import com.example.book.event.BookCreateEvent;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationService {

    private final JavaMailSender javaMailSender;

    @KafkaListener(topics = "book")
    public void listen(BookCreateEvent bookCreateEvent) {
        MimeMessagePreparator messagePreparator = mimeMessage -> {
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);
            messageHelper.setFrom("ilya.sokurwork@gmail.com");
            messageHelper.setTo("ilya.sokurwork@gmail.com");
            messageHelper.setSubject("Your Booking");
            messageHelper.setText(String.format("""
                            Hi %s,%s

                            Your Booking is now placed successfully.
                            
                            Best Regards
                            """,
                    bookCreateEvent.getHotelId().toString(),
                    bookCreateEvent.getUserId().toString()
            ));
        };
        try {
            javaMailSender.send(messagePreparator);
        } catch (MailException e) {
            throw new RuntimeException("Exception occurred when sending mail to springshop@email.com", e);
        }

    }
}
