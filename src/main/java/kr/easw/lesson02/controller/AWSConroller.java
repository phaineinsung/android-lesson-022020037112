package kr.easw.lesson02.controller;

import kr.easw.lesson02.model.dto.AWSKeyDto;
import kr.easw.lesson02.service.AWSService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import com.amazonaws.services.s3.model.GetObjectRequest;

import java.util.List;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/rest/aws")
public class AWSConroller {
    private final AWSService awsController;




    @PostMapping("/auth")
    private ModelAndView onAuth(AWSKeyDto awsKey) {
        try {
            awsController.initAWSAPI(awsKey);
            return new ModelAndView("redirect:/");
        } catch (Exception ex) {
            ex.printStackTrace();
            return new ModelAndView("redirect:/server-error?errorStatus=" + ex.getMessage());
        }
    }

    @GetMapping("/list")
    private List<String> onFileList() {
        return awsController.getFileList();
    }

    @PostMapping("/upload")
    private ModelAndView onUpload(@RequestParam MultipartFile file) {
        try {
            awsController.upload(file);
            return new ModelAndView("redirect:/?success=true");
        } catch (Exception ex) {
            ex.printStackTrace();
            return new ModelAndView("redirect:/server-error?errorStatus=" + ex.getMessage());
        }
    }


    @PostMapping("/download")
    private ModelAndView onDownload(@RequestParam String fileName) {
        try {
            // AWS S3에서 파일 다운로드
            AWSService.onDownload(fileName);


            // 파일 다운로드 성공 시 리다이렉트 또는 적절한 응답 처리
            return new ModelAndView("redirect:/?success=true");
        } catch (Exception ex) {
            ex.printStackTrace();
            return new ModelAndView("redirect:/server-error?errorStatus=" + ex.getMessage());
        }
    }
    public void downloadFile(String fileName) throws IOException {
        // S3 버킷에서 파일 다운로드
        File file = new File("downloaded-file-path/" + fileName);
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        AWSService.getObject(new GetObjectRequest("your-bucket-name", fileName), fileOutputStream);
        fileOutputStream.close();
    }
}


















