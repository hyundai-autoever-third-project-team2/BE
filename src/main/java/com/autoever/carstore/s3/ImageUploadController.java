//package com.autoever.carstore.s3;
//
//import java.io.IOException;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RestController;
//import org.springframework.web.multipart.MultipartFile;
//
//@RestController
//public class ImageUploadController {
//
//    private final ImageUploadService imageUploadService;
//
//    @Autowired
//    public ImageUploadController(ImageUploadService imageUploadService) {
//        this.imageUploadService = imageUploadService;
//    }
//
//
//    @PostMapping("/upload")
//    public String upload(MultipartFile image, Model model) throws IOException {
//        /* 이미지 업로드 로직 */
//        String imageUrl = imageUploadService.upload(image);
//
//        /* View의 이름을 반환하여 View를 렌더링하도록 함 */
//        return imageUrl;
//    }
//}