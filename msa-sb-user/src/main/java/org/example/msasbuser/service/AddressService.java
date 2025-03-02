package org.example.msasbuser.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class AddressService {
    // 도로명주소 API 기본 URL 및 승인키 설정
    private static final String JUSO_API_URL = "https://business.juso.go.kr/addrlink/addrLinkApi.do";
    private static final String CONFIRM_KEY = "devU01TX0FVVEgyMDI1MDIxMjIzMDYzMjExNTQ2NTY="; // 실제 운영 승인키 입력

    public String searchAddress(String keyword) {
        try {
            // 특수문자 제거 (한글, 숫자, 공백만 허용)
            keyword = keyword.replaceAll("[^가-힣0-9 ]", "").trim();
            // API 요청 URL 구성
            String apiUrl = JUSO_API_URL + "?currentPage=1&countPerPage=5&resultType=json&confmKey=" + CONFIRM_KEY + "&keyword=" + keyword;
            // 👉 요청 URL 확인 (디버깅)
            System.out.println("요청 URL: " + apiUrl);

            // RestTemplate을 사용하여 API 호출 수행
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<String> response = restTemplate.getForEntity(apiUrl, String.class);

            // 👉 API 응답 확인 (디버깅)
            System.out.println("API 응답: " + response.getBody());

            // API 응답에서 주소 정보 추출 및 반환
            return extractAddress(response.getBody());
        } catch (Exception e) {
            throw new RuntimeException("주소 검색 중 오류 발생: " + e.getMessage(), e);
        }
    }

    private String extractAddress(String jsonResponse) {
        try {
            // JSON 파싱을 위한 ObjectMapper 생성
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode root = objectMapper.readTree(jsonResponse);

            // "results" 객체에서 "juso" 배열 추출
            JsonNode jusoArray = root.path("results").path("juso");

            // 검색된 주소가 존재하면 첫 번째 주소의 도로명주소(roadAddr) 반환
            if (jusoArray.isArray() && jusoArray.size() > 0) {
                return jusoArray.get(0).path("roadAddr").asText();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "주소 검색 실패";
    }
}
