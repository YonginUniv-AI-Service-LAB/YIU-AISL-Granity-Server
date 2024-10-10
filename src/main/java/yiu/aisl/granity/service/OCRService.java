package yiu.aisl.granity.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import yiu.aisl.granity.dto.Request.OCRRequestDto;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class OCRService {
    @Value("${naver.ocr.url}")
    private String url;

    @Value("${naver.ocr.key}")
    private String key;

    private final ObjectMapper objectMapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT); // JSON 출력 시 가독성 향상

    public String callApi(String type, OCRRequestDto request, String ext) {
        String apiURL = url;
        String secretKey = key;
        List<MultipartFile> imageFiles = request.getFiles();
        String rawResponse = null;

        try {
            URL url = new URL(apiURL);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setUseCaches(false);
            con.setDoInput(true);
            con.setDoOutput(true);
            con.setReadTimeout(30000);
            con.setRequestMethod(type);
            String boundary = "----" + UUID.randomUUID().toString().replaceAll("-", "");
            con.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
            con.setRequestProperty("X-OCR-SECRET", secretKey);

            // JSON 메시지 생성
            Map<String, Object> jsonMap = new HashMap<>();
            jsonMap.put("version", "V2");
            jsonMap.put("requestId", "guide-demo");
            jsonMap.put("timestamp", System.currentTimeMillis());

            Map<String, String> imageMap = new HashMap<>();
            imageMap.put("format", ext);
            imageMap.put("name", "demo");
            jsonMap.put("images", List.of(imageMap)); // 리스트로 이미지 정보를 추가

            String postParams = objectMapper.writeValueAsString(jsonMap); // JSON 문자열로 변환

            // 연결 및 데이터 전송
            con.connect();
            DataOutputStream wr = new DataOutputStream(con.getOutputStream());

            // 첫 번째 파일 처리 (여러 파일이 있을 경우, 이를 확장 가능)
            MultipartFile file = imageFiles.get(0);
            writeMultiPart(wr, postParams, file, boundary); // MultipartFile을 처리하도록 수정
            wr.close();

            // 응답 처리
            int responseCode = con.getResponseCode();
            BufferedReader br;
            if (responseCode == 200) {
                br = new BufferedReader(new InputStreamReader(con.getInputStream()));
            } else {
                br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
            }

            StringBuilder response = new StringBuilder();
            String inputLine;
            while ((inputLine = br.readLine()) != null) {
                response.append(inputLine);
            }
            br.close();

            rawResponse = response.toString();

            // 응답이 JSON 형식일 경우, 파싱해서 포맷팅된 JSON으로 변환
            try {
                Object jsonResponse = objectMapper.readValue(rawResponse, Object.class); // JSON으로 변환
                rawResponse = objectMapper.writeValueAsString(jsonResponse); // 포맷된 JSON으로 변환
            } catch (Exception e) {
                System.out.println("Response is not valid JSON: " + rawResponse);
            }

        } catch (Exception e) {
            System.out.println("Error during API call: " + e);
        }

        return rawResponse;
    }

    private static void writeMultiPart(OutputStream out, String jsonMessage, MultipartFile file, String boundary) throws IOException {
        StringBuilder sb = new StringBuilder();
        sb.append("--").append(boundary).append("\r\n");
        sb.append("Content-Disposition: form-data; name=\"message\"\r\n\r\n");
        sb.append(jsonMessage);
        sb.append("\r\n");

        out.write(sb.toString().getBytes("UTF-8"));
        out.flush();

        if (file != null && !file.isEmpty()) { // MultipartFile을 사용하여 파일 처리
            out.write(("--" + boundary + "\r\n").getBytes("UTF-8"));
            StringBuilder fileString = new StringBuilder();
            fileString.append("Content-Disposition: form-data; name=\"file\"; filename=\"");
            fileString.append(file.getOriginalFilename()).append("\"\r\n");
            fileString.append("Content-Type: ").append(file.getContentType()).append("\r\n\r\n"); // 올바른 Content-Type 사용
            out.write(fileString.toString().getBytes("UTF-8"));
            out.flush();

            try (InputStream fis = file.getInputStream()) { // MultipartFile에서 InputStream 가져오기
                byte[] buffer = new byte[8192];
                int count;
                while ((count = fis.read(buffer)) != -1) {
                    out.write(buffer, 0, count);
                }
                out.write("\r\n".getBytes());
            }

            out.write(("--" + boundary + "--\r\n").getBytes("UTF-8"));
        }
        out.flush();
    }
}
