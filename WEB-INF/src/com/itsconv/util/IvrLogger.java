package com.itsconv.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 콜플로우 각 단계 진입 및 DB/API 호출 결과를 타임스탬프와 함께 콘솔에 출력하기 위한 유틸리티.
 *
 * AAOD(Orchestration Designer)가 생성하는 flow 클래스 중 BasicServlet의
 * servletImplementation() 메서드만 코드 재생성 시에도 덮어써지지 않는다.
 * 따라서 이 로거는 그 위치(현재는 LookupCustomer, RegistCallback)에서만 호출한다.
 * Form/Menu/Data 노드는 커스텀 코드 삽입 지점이 없어 여기서 로그를 남길 수 없다.
 */
public final class IvrLogger {

    private static final DateTimeFormatter FORMAT =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");

    private IvrLogger() {
    }

    /**
     * @param step    로그를 남기는 단계(노드) 이름, 예: "LookupCustomer"
     * @param message 남길 메시지
     */
    public static void log(String step, String message) {
        System.out.println(
                "[" + LocalDateTime.now().format(FORMAT) + "] "
                        + "[" + step + "] "
                        + message
        );
    }
}
