package kuke.board.articleread.cache;

import java.time.Duration;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;

import kuke.board.common.dataserializer.DataSerializer;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class OptimizedCache {
    private String data;
    private LocalDateTime expireAt;

    public static OptimizedCache of(Object data, Duration ttl) {
        OptimizedCache optimizedCache = new OptimizedCache();
        optimizedCache.data = DataSerializer.serialize(data);
        optimizedCache.expireAt = LocalDateTime.now().plus(ttl);
        return optimizedCache;
    }

    @JsonIgnore
    public boolean isExpired() {
        return LocalDateTime.now().isAfter(expireAt);
    }

    public <T> T parseData(Class<T> dataType) {
        return DataSerializer.deserialize(data, dataType);
    }
}
