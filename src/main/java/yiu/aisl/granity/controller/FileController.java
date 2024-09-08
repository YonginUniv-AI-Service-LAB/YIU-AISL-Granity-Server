package yiu.aisl.granity.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import yiu.aisl.granity.dto.Request.FileRequestDto;
import yiu.aisl.granity.dto.Response.FileResponseDto;
import yiu.aisl.granity.repository.FileRepository;
import yiu.aisl.granity.service.FileService;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class FileController {
    private final FileRepository fileRepository;
    private final Path uploadPath = Paths.get("C:", "develop", "granity", "upload-files");

    // 다중 파일 업로드
    public List<FileRequestDto> uploadFiles(List<MultipartFile> multipartFiles) {
        List<FileRequestDto> files = new ArrayList<>();
        for (MultipartFile multipartFile : multipartFiles) {
            if (!multipartFile.isEmpty()) {
                FileRequestDto fileDto = uploadFile(multipartFile);
                if (fileDto != null) {
                    files.add(fileDto);
                }
            }
        }
        return files;
    }

    // 단일 파일 업로드
    public FileRequestDto uploadFile(MultipartFile multipartFile) {
        if (multipartFile.isEmpty()) {
            return null;
        }

        String saveName = generateSaveFilename(multipartFile.getOriginalFilename());
        String today = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyMMdd"));
        Path directoryPath = uploadPath.resolve(today);
        Path filePath = uploadPath.resolve(today).resolve(saveName);  // Correctly build the path

        makeDirectories(directoryPath);
        File uploadFile = filePath.toFile();
        try {
            multipartFile.transferTo(uploadFile);
        } catch (IOException e) {
            throw new RuntimeException("File upload failed", e);
        }

        return FileRequestDto.builder()
                .originName(multipartFile.getOriginalFilename())
                .saveName(saveName)
                .size(multipartFile.getSize())
                .build();
    }

    // 저장 파일명 생성
    private String generateSaveFilename(String filename) {
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        String extension = StringUtils.getFilenameExtension(filename);
        return uuid + (extension != null ? "." + extension : "");
    }

    // 업로드 폴더 생성
    private void makeDirectories(Path path) {
        File dir = path.toFile();
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }

    // 다중 파일 삭제(Disk)
    public void deleteFiles(List<FileResponseDto> files) {
        if (CollectionUtils.isEmpty(files)) return;
        for (FileResponseDto file : files) {
            String uploadedDate = file.getCreatedAt().toLocalDate().format(DateTimeFormatter.ofPattern("yyMMdd"));
            deleteFile(uploadedDate, file.getSaveName());
        }
    }

    // 단일 파일 삭제(Disk)
    private void deleteFile(String datePath, String filename) {
        Path filePath = uploadPath.resolve(datePath).resolve(filename);  // Correctly build the path
        File file = filePath.toFile();
        if (file.exists()) {
            if (!file.delete()) {
                System.err.println("Failed to delete file: " + filePath);
            }
        } else {
            System.out.println("File not found: " + filePath);
        }
    }

    public Resource readFileAsResource(yiu.aisl.granity.domain.File file) {
        String uploadedDate = file.getCreatedAt().toLocalDate().format(DateTimeFormatter.ofPattern("yyMMdd"));
        String filename = file.getSaveName();
        Path filePath = uploadPath.resolve(uploadedDate).resolve(filename);
        System.out.println(filePath);
        try {
            Resource resource = new UrlResource(filePath.toUri());
            if(resource.exists() == false || resource.isFile() == false) {
                throw new RuntimeException("file not found: " +filePath.toString());
            }
            return resource;
        } catch (MalformedURLException e) {
            throw new RuntimeException("file not found: " +filePath.toString());
        }
    }

    @GetMapping(value = "/files/download")
    public ResponseEntity<Resource> downloadFile(@RequestParam(value = "id") Integer fileId) {
        yiu.aisl.granity.domain.File file = fileRepository.findById(fileId).orElseThrow();
        Resource resource = readFileAsResource(file);

        try {
            // 파일명 인코딩 처리
            String encodedFilename = URLEncoder.encode(file.getOriginName(), "UTF-8").replaceAll("\\+", "%20");

            // 파일 MIME 타입 동적 처리
            Path filePath = Paths.get(file.getSaveName());
            String contentType = Files.probeContentType(filePath);
            if (contentType == null) {
                contentType = "application/octet-stream";
            }

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + encodedFilename + "\"")
                    .header(HttpHeaders.CONTENT_LENGTH, String.valueOf(file.getSize()))
                    .body(resource);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Filename encoding failed: " + file.getOriginName(), e);
        } catch (IOException e) {
            throw new RuntimeException("Could not determine file content type", e);
        }


    }

    @GetMapping(value = "/files/show")
    public ResponseEntity<Resource> showFile(@RequestParam(value = "id") Integer fileId) {
        yiu.aisl.granity.domain.File file = fileRepository.findById(fileId).orElseThrow();
        Resource resource = readFileAsResource(file);

        try {
            // 파일명 인코딩 처리
            String encodedFilename = URLEncoder.encode(file.getOriginName(), "UTF-8").replaceAll("\\+", "%20");

            // 파일 MIME 타입 동적 처리
            Path filePath = Paths.get(file.getSaveName());
            String contentType = Files.probeContentType(filePath);
            if (contentType == null) {
                contentType = "application/octet-stream";
            }

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + encodedFilename + "\"")
                    .body(resource);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Filename encoding failed: " + file.getOriginName(), e);
        } catch (IOException e) {
            throw new RuntimeException("Could not determine file content type", e);
        }
    }
}
