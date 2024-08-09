// package unical.enterprise.jokibackend.Utility.CustomContextManager;

// public class UserContextHolder {
//     private static final ThreadLocal<UserContext> contextHolder = ThreadLocal.withInitial(UserContext::new);

//     public static UserContext getContext() {
//         return contextHolder.get();
//     }

//     public static void setContext(UserContext context) {
//         contextHolder.set(context);
//     }

//     public static void clearContext() {
//         contextHolder.remove();
//     }
// }