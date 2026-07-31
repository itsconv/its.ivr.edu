package com.itsconv.util;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * OnDisconnect 이벤트가 "실제 중간 이탈"인지, 아니면 IVR이 이미 정상적으로
 * (등록 성공/실패 안내 후, 혹은 재시도초과로) 콜을 끝낸 뒤에 뒤따라오는
 * 부수적인 이벤트인지 구분하기 위한 세션 단위 표시자.
 *
 * CallEndLog / RetryLog에서 콜이 정상 종료 절차로 들어갔다고 표시(mark)해두면,
 * OnDisconnectLog에서 그 표시를 확인해서 로그 문구를 다르게 남길 수 있다.
 */
public final class CallCompletionTracker {

    private static final Set<String> COMPLETED_SESSION_IDS = ConcurrentHashMap.newKeySet();

    private CallCompletionTracker() {
    }

    /**
     * 이 세션이 정상 종료 절차(등록 성공/실패 안내, 재시도초과 안내)로 들어갔음을 표시한다.
     */
    public static void markCompleted(String sessionId) {
        if (sessionId != null && !sessionId.isEmpty()) {
            COMPLETED_SESSION_IDS.add(sessionId);
        }
    }

    /**
     * 이 세션이 이미 정상 종료 절차를 탔었는지 확인하고, 확인과 동시에 표시를 제거한다
     * (세션이 재사용되거나 메모리에 계속 쌓이는 것을 방지).
     */
    public static boolean wasCompleted(String sessionId) {
        if (sessionId == null || sessionId.isEmpty()) {
            return false;
        }
        return COMPLETED_SESSION_IDS.remove(sessionId);
    }
}
