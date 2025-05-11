package kuke.board.articleread.consumer;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

import kuke.board.articleread.service.ArticleReadService;
import kuke.board.common.event.Event;
import kuke.board.common.event.EventPayload;
import kuke.board.common.event.EventType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class ArticleReadEventConsumer {
    private final ArticleReadService articleReadService;

    // 컨슈머는 쓰기 트래픽에 대해서 작업
    // API는 조회 트래픽에 대해서 작업
    // 애플리케이션이 20개 떠있고, 구독한 토픽의 파티션이 5개.
    // 5개의 컨슈머는 파티션을 처리하고 있지만, 15개의 컨슈머는 파티션을 처리안하고 놀고 있음.

    @KafkaListener(topics = {
        EventType.Topic.KUKE_BOARD_ARTICLE,
        EventType.Topic.KUKE_BOARD_COMMENT,
        EventType.Topic.KUKE_BOARD_LIKE
    })
    public void listen(String message, Acknowledgment ack) {
        log.info("[ArticleReadEventConsumer.listen] message: {}", message);
        Event<EventPayload> event = Event.fromJson(message);
        if (event != null) {
            articleReadService.handleEvent(event);
        }
        ack.acknowledge();
    }
}
