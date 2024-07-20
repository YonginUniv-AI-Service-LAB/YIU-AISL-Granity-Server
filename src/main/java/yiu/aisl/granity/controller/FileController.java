package yiu.aisl.granity.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import yiu.aisl.granity.dto.Request.FileRequestDto;
import yiu.aisl.granity.dto.Response.FileResponseDto;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class FileController {
    private final String uploadPath = Paths.get("C:", "develop", "granity", "upload-files").toString();

    // 다중 파일 업로드
    public List<FileRequestDto> uploadFiles(List<MultipartFile> multipartFiles) {
        List<FileRequestDto> files = new ArrayList<>();
        for(MultipartFile multipartFile : multipartFiles) {
            if(multipartFile.isEmpty()) {
                continue;
            }
            files.add(uploadFile(multipartFile));
        }
        return files;
    }

    // 단일 파일 업로드
    public FileRequestDto uploadFile(MultipartFile multipartFile) {
        if(multipartFile.isEmpty()) {
            return null;
        }

        String saveName = generateSaveFilename(multipartFile.getOriginalFilename());
        String today = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyMMdd")).toString();
        String uploadPath = getUploadPath(today) + File.separator + saveName;
        File uploadFile = new File(uploadPath);

        try {
            multipartFile.transferTo(uploadFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
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

        return uuid + "." + extension;
    }

    // 업로드 경로 반환
    private String getUploadPath() {
        return makeDirectories(uploadPath);
    }

    private String getUploadPath(final String addPath) {
        return makeDirectories(uploadPath + File.separator + addPath);
    }

    // 업로드 폴더 생성
    private String makeDirectories(final String path) {
        File dir = new File(path);
        if (dir.exists() == false) {
            dir.mkdirs();
        }
        return dir.getPath();
    }

    // 다중 파일 삭제(Disk)
    public void deleteFiles(List<FileResponseDto> files) {
        if(CollectionUtils.isEmpty(files)) return;
        for(FileResponseDto file : files) {
            String uploadedDate = file.getCreatedAt().toLocalDate().format(DateTimeFormatter.ofPattern("yyMMdd"));
            deleteFile(uploadedDate, file.getSaveName());
        }
    }

    // 단일 파일 삭제(Disk)
    private void deleteFile(String addPath, String filename) {
        String filePath = Paths.get(uploadPath, addPath, filename).toString();
        deleteFile(filePath);
    }

    private void deleteFile(String filePath) {
        File file = new File(filePath);
        if(file.exists()) {
            file.delete();
        }
    }
}
