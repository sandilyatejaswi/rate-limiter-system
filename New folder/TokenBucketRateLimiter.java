import java.util.*;

class TokenBucket {
    int tokens;
    long lastRefillTime;

    public TokenBucket(int capacity) {
        this.tokens = capacity;
        this.lastRefillTime = System.currentTimeMillis();
    }
}

public class TokenBucketRateLimiter implements RateLimiter {

    private final int capacity;
    private final int refillRate; // tokens per second
    private Map<String, TokenBucket> bucketMap;

    public TokenBucketRateLimiter(int capacity, int refillRate) {
        this.capacity = capacity;
        this.refillRate = refillRate;
        this.bucketMap = new HashMap<>();
    }

    @Override
    public boolean allowRequest(String userId) {
        long currentTime = System.currentTimeMillis();

        bucketMap.putIfAbsent(userId, new TokenBucket(capacity));
        TokenBucket bucket = bucketMap.get(userId);

        // 🔄 Refill tokens
        long timePassed = (currentTime - bucket.lastRefillTime) / 1000;
        if (timePassed > 0) {
            int tokensToAdd = (int) (timePassed * refillRate);
            bucket.tokens = Math.min(capacity, bucket.tokens + tokensToAdd);
            bucket.lastRefillTime = currentTime;
        }

        // 📊 PRINT BEFORE REQUEST
        System.out.println("Tokens before request: " + bucket.tokens);

        // ✅ Check & consume
        if (bucket.tokens > 0) {
            bucket.tokens--;

            // 📊 PRINT AFTER CONSUME
            System.out.println("Tokens left: " + bucket.tokens);

            return true; // allow
        }

        // ❌ BLOCK CASE
        System.out.println("Tokens left: 0 (Blocked)");
        return false;
    }
}