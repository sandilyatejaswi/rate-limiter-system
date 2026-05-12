public class Main {
    public static void main(String[] args) throws InterruptedException {

        // 🔹 Sliding Window
        RateLimiter sliding = new SlidingWindowRateLimiter(3, 10);

        // 🔹 Token Bucket
        RateLimiter token = new TokenBucketRateLimiter(5, 1);

        String user = "user1";

        System.out.println("---- Sliding Window ----");
        for (int i = 1; i <= 5; i++) {
            System.out.println("Request " + i + ": " +
                (sliding.allowRequest(user) ? "Allowed" : "Blocked"));
            Thread.sleep(1000);
        }

        System.out.println("\n---- Token Bucket ----");
        for (int i = 1; i <= 7; i++) {
            System.out.println("Request " + i + ": " +
                (token.allowRequest(user) ? "Allowed" : "Blocked"));
            Thread.sleep(100);
        }
    }
}