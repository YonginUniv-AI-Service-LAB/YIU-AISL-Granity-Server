package yiu.aisl.granity.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import yiu.aisl.granity.controller.FileController;
import yiu.aisl.granity.domain.File;
import yiu.aisl.granity.dto.Request.FileRequestDto;
import yiu.aisl.granity.dto.Response.FileResponseDto;
import yiu.aisl.granity.repository.FileRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class FileService {
    private final FileRepository fileRepository;
    private final FileController fileController;

    public boolean saveFiles(Integer type, Integer typeId, List<FileRequestDto> files) {
        System.out.println("saveFile 메소드 실행 진입");
        if(CollectionUtils.isEmpty(files)) {
            return true;
        }

        // FileRequestDto를 File 엔티티로 변환
        List<File> fileEntities = files.stream()
                .map(fileRequestDto -> {
                    fileRequestDto.setType(type);
                    fileRequestDto.setTypeId(typeId);
                    return fileRequestDto.toEntity(); // FileRequestDto를 File로 변환
                })
                .collect(Collectors.toList());
//        for(FileRequestDto file : files) {
//            file.setType(type);
//            file.setTypeId(typeId);
//        }
        try {
            List<File> savedFiles = fileRepository.saveAll(fileEntities);
            System.out.println("실행완료");
            System.out.println(savedFiles);
        } catch (Exception e) {
            e.printStackTrace(); // 예외를 콘솔에 출력
            throw e; // 예외를 다시 던져서 문제를 호출 스택에서 추적할 수 있게 함
        }

        return true;
    }

    public List<FileResponseDto> findAllFileByTypeAndTypeId(Integer type, Integer typeId) {
        List<File> files = fileRepository.findAllByTypeAndTypeId(type, typeId);
        return files.stream()
                .map(file -> new FileResponseDto(file.getId(), file.getType(), file.getTypeId(), file.getOriginName(), file.getSaveName(), file.getSize(), file.getDeleteOrNot(), file.getCreatedAt()))
                .collect(Collectors.toList());
    }

    // 파일 삭제(DB)
    public void deleteAllFileByTypeAndTypeId(Integer type, Integer typeId) {
        fileRepository.deleteByTypeAndTypeId(type, typeId);
    }

    public void deleteAllFileById(List<Integer> id) {
        if(CollectionUtils.isEmpty(id)) {
            return;
        }
        fileRepository.deleteAllById(id);
    }


}
