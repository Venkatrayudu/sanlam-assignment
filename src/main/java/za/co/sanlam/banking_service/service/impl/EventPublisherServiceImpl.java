package za.co.sanlam.banking_service.service.impl;

import org.springframework.stereotype.Service;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.PublishRequest;
import software.amazon.awssdk.services.sns.model.PublishResponse;
import za.co.sanlam.banking_service.model.WithdrawalEvent;
import za.co.sanlam.banking_service.service.EventPublisherService;

@Service
public class EventPublisherServiceImpl implements EventPublisherService {

    private SnsClient snsClient;

    public EventPublisherServiceImpl() {
        this.snsClient = SnsClient.builder()
                                    .region(Region.EU_WEST_1) // Specify your region
                                    .build();
    }

    @Override
    public String publish(WithdrawalEvent event) {
        String eventJson = event.toJson(); // Convert event to JSON
        String snsTopicArn = "arn:aws:sns:eu-west-1:069724177548:sanlam-banking-withdrawal-events";
        PublishRequest publishRequest = PublishRequest.builder()
                .message(eventJson)
                .topicArn(snsTopicArn)
                .build();
        PublishResponse publishResponse = snsClient.publish(publishRequest);
        return publishResponse.messageId();
    }
}
