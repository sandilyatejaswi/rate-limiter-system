import java.util.*;

public class SlidingWindowRateLimiter implements RateLimiter {

    private final int maxRequests;
    private final long windowSizeInMillis;
    private Map<String, Deque<Long>> userRequestMap;

    public SlidingWindowRateLimiter(int maxRequests, int windowSizeInSeconds) {
        this.maxRequests = maxRequests;
        this.windowSizeInMillis = windowSizeInSeconds * 1000L;
        this.userRequestMap = new HashMap<>();
    }

    @Override
    public boolean allowRequest(String userId) {
        long currentTime = System.currentTimeMillis();

        userRequestMap.putIfAbsent(userId, new LinkedList<>());
        Deque<Long> timestamps = userRequestMap.get(userId);

        while (!timestamps.isEmpty() &&
               currentTime - timestamps.peekFirst() > windowSizeInMillis) {
            timestamps.pollFirst();
        }

        if (timestamps.size() < maxRequests) {
            timestamps.addLast(currentTime);
            return true;
        }

        return false;
    }
}