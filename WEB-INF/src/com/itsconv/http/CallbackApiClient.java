package com.itsconv.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import com.google.gson.Gson;

//콜백 등록 API에 POST 요청을 보내고, 성공 여부와 callbackId를 CallbackApiResult로 돌려주는 클래스
public final class CallbackApiClient {

	private static final String API_URL = "http://192.168.40.200:18080/api/v1/callbacks";
    private static final int CONNECT_TIMEOUT_MILLIS = 3000;
    private static final int READ_TIMEOUT_MILLIS = 3000;
    private static final Gson GSON = new Gson();//Java 객체와 JSON을 서로 바꿀수 있게

    private CallbackApiClient() {
    }

    //싱글톤 인스턴스를 지연 생성한다.
    private static class Holder {
    	private static final CallbackApiClient INSTANCE = new CallbackApiClient();
    }

    public static CallbackApiClient getInstance() {
    	return Holder.INSTANCE;//이때 INSTANCE가 생성됨(지연 초기화)
    }
    //콜백 등록 API를 호출하고, 등록 결과(성공 또는 실패)를 반환한다.
    public CallbackApiResult registerCallback(String callbackNumber,String ani,String custName) {
        HttpURLConnection connection = null;//HTTP 연결 객체를 선언(초기엔 연결전이니까 null)

        //요청 객체를 Gson으로 UTF-8 JSON 데이터로 변환한다.
        try {
            CallbackRequest request = new CallbackRequest(callbackNumber, ani, custName); //API에 보낼 데이터를 CallbackRequest 객체로 묶음

            byte[] requestBody = GSON.toJson(request).getBytes(StandardCharsets.UTF_8);

            //API URL 객체 생성 및 연결
            connection = (HttpURLConnection) new URL(API_URL).openConnection();
            connection.setRequestMethod("POST");
            connection.setConnectTimeout(CONNECT_TIMEOUT_MILLIS);
            connection.setReadTimeout(READ_TIMEOUT_MILLIS);
            connection.setDoOutput(true);//요청 본문을 전송하겠다는 뜻(POST JSON 보내려면 필요)
            connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");//내가 보내는 데이터 형식
            connection.setRequestProperty("Accept", "application/json");//내가 받고 싶은 응답 형식
            connection.setFixedLengthStreamingMode(requestBody.length);//전송할 요청 본문의 크기 지정(바이트)

            // JSON 요청 데이터를 전송한다.
            try(OutputStream outputstream = connection.getOutputStream()){
            	outputstream.write(requestBody);
            	outputstream.flush();//남은 데이터까지 모두 보냄. close()안할땐 필수지만 지금은 없애도됨.
            }

            // 4.서버가 반환한 HTTP 상태 코드 받기
            int httpStatus = connection.getResponseCode();
            String responseJson = readResponse(connection, httpStatus); // 응답 본문을 responseJson에 저장

            // 5. 응답 JSON을 CallbackResponse 객체로 변환
            CallbackResponse response = responseJson.isEmpty() ? null : GSON.fromJson(responseJson, CallbackResponse.class);
            String code = response == null? "" : valueOrEmpty(response.code);
            String callbackId = response == null? "" : valueOrEmpty(response.callbackId);

            /*
             * HTTP 상태가 200이고 응답 code가 0000인 경우만 성공이다.
             */
            if (httpStatus == HttpURLConnection.HTTP_OK
                    && "0000".equals(code)) {
                return CallbackApiResult.success(callbackId);
            }

            return CallbackApiResult.failure(
                    "HTTP 상태: " + httpStatus
                            + ", 응답 code: " + code
            );

        } catch (Exception e) {
            /*
             * 연결, 타임아웃 또는 JSON 처리 오류를 호출자에게 던지지 않고
             * 실패 결과로 변환한다.
             */
            String message = e.getMessage();

            if (message == null || message.isEmpty()) {
                message = e.getClass().getSimpleName();
            }

            return CallbackApiResult.failure(message);

        } finally {
            /*
             * 성공 또는 실패 여부와 관계없이 HTTP 연결을 해제한다.
             */
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    /**
     * HTTP 응답 본문을 UTF-8 문자열로 반환한다.
     */
    private String readResponse(
            HttpURLConnection connection,
            int httpStatus
    ) throws IOException {
        InputStream inputStream;

        if (httpStatus >= 200 && httpStatus < 400) {
            inputStream = connection.getInputStream();
        } else {
            inputStream = connection.getErrorStream();
        }

        if (inputStream == null) {
            return "";
        }

        StringBuilder response = new StringBuilder();

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(
                        inputStream,
                        StandardCharsets.UTF_8
                )
        )) {
            String line;

            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
        }

        return response.toString();
    }

    private static String valueOrEmpty(String value) {
        return value == null ? "" : value;
    }

    /**
     * Gson이 요청 JSON으로 변환할 내부 데이터 객체다.
     */
    private static final class CallbackRequest {

        private final String callbackNumber;
        private final String ani;
        private final String custName;

        private CallbackRequest(
                String callbackNumber,
                String ani,
                String custName
        ) {
            this.callbackNumber = valueOrEmpty(callbackNumber);
            this.ani = valueOrEmpty(ani);
            this.custName = valueOrEmpty(custName);
        }
    }

    /**
     * Gson이 응답 JSON에서 읽을 내부 데이터 객체다.
     */
    private static final class CallbackResponse {

        private String code;
        private String callbackId;
    }
}
