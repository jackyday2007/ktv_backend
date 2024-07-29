package com.ispan.ktv.controller;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ispan.ktv.bean.Photos;
import com.ispan.ktv.service.PhotoService;

@RestController
@CrossOrigin
public class PhotosController {

    @Autowired
    private PhotoService photoService;

    @GetMapping("/photos/upload")
    public String upload() {
        return "photos/uploadPage"; // 确保这个模板文件在你的项目中存在
    }

    @PostMapping("/photos/uploadPost")
    public ResponseEntity<String> uploadPost(@RequestParam String photoName, @RequestParam MultipartFile photoFile)
            throws IOException {
        Photos newPhotos = new Photos();
        newPhotos.setPhotoName(photoName);
        newPhotos.setPhotoFile(photoFile.getBytes());
        photoService.insertPhoto(newPhotos);
        return ResponseEntity.ok("成功上傳");
    }

    @GetMapping("/photos/findImage/{id}")
    public ResponseEntity<byte[]> findImage(@PathVariable(name = "id") Integer id) {
        Photos photos = photoService.findById(id);
        if (photos != null) {
            byte[] file = photos.getPhotoFile();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_JPEG); // 如果有其他格式图片，需根据实际情况修改
            return new ResponseEntity<>(file, headers, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/photos/findAll")
    public ResponseEntity<List<Photos>> findAllPhotos() {
        List<Photos> photosList = photoService.findAll();
        if (photosList != null && !photosList.isEmpty()) {
            List<Photos> responseList = photosList.stream()
                    .map(photo -> {
                        Photos newPhoto = new Photos();
                        newPhoto.setId(photo.getId());
                        newPhoto.setPhotoName(photo.getPhotoName());
                        // 直接使用 Base64 编码的图像数据
                        newPhoto.setPhotoFile(photo.getPhotoFile()); // 假设 photoFile 是 Base64 编码字符串
                        // 复制其他属性
                        return newPhoto;
                    })
                    .collect(Collectors.toList());
            return new ResponseEntity<>(responseList, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/photos/delete/{id}")
    public String delete(@PathVariable(name = "id") Integer id) {
        photoService.findById(id);
        if (id == null) {
            return "查無ID";
        }
        photoService.delete(id);
        return "刪除成功";
    }
}
