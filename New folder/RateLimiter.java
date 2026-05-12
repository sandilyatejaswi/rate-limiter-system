public interface RateLimiter {
    boolean allowRequest(String userId);
}