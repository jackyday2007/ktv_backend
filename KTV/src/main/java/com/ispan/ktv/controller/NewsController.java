package com.ispan.ktv.controller;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeParseException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import net.coobird.thumbnailator.Thumbnails;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.expression.ParseException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ispan.ktv.bean.News;
import com.ispan.ktv.bean.Problems;
import com.ispan.ktv.bean.Staff;
import com.ispan.ktv.repository.NewsRepository;
import com.ispan.ktv.service.NewsService;
import com.ispan.ktv.service.StaffService;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

//控制器類，處理與最新消息相關的請求。

@CrossOrigin
@RestController
@RequestMapping("/news")
public class NewsController {

    @Autowired
    private NewsService newsService;
    @Autowired
    private StaffService staffService;

    /**
     * 顯示News頁面。
     */
    @GetMapping("/")
    public String News() {
        return "News"; // 返回News頁面的視圖名稱
    }

    /**
     * 處理新增News的請求。
     */
    @PostMapping("/addNews")
    public String insertNews(@RequestBody News news) {
        newsService.insertNews(news);
        return "新增成功！";
    }

    /**
     * PUT映射，處理更新News的請求。
     */
    @PutMapping("/updateNews/{id}")
    public String updateNews(@PathVariable Integer id, @RequestBody News news) {

        news.setNewsId(id);
        newsService.updateNews(id, news);

        return "更新成功！";
    }

    /**
     * 根據ID查找News。
     */
    @GetMapping("/find/{id}")
    public ResponseEntity<String> findNewsById(@PathVariable Integer id) {
        News news = newsService.findNewsById(id);
        JSONObject responseBody = new JSONObject();
        if (news != null) {
            JSONObject item = new JSONObject()
                    .put("title", news.getTitle())
                    .put("content", news.getContent())
                    .put("status", news.getStatus())
                    .put("startDate", news.getStartDate())
                    .put("endDate", news.getEndDate())
                    .put("url", news.getUrl())
                    .put("activityStartDate", news.getActivityStartDate())
                    .put("image", news.getImage())
                    .put("createTime", news.getCreateTime())
                    .put("createBy", news.getCreateBy() != null ? news.getCreateBy().getAccountName() : null)
                    .put("updateTime", news.getUpdateTime())
                    .put("updateBy", news.getUpdateBy() != null ? news.getUpdateBy().getAccountName() : null);
            responseBody.put("news", item);
            return ResponseEntity.ok(responseBody.toString());
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{}"); // 返回空的 JSON 对象表示未找到
    }

    /**
     * 顯示所有News。
     */
    @GetMapping("/news")
    public String showAllNews() {
        List<News> allNews = newsService.findAllNews();
        JSONObject responseBody = new JSONObject();
        JSONArray array = new JSONArray();
        if (allNews != null && !allNews.isEmpty()) {
            for (News news : allNews) {
                JSONObject item = new JSONObject()
                        .put("newsId", news.getNewsId())
                        .put("title", news.getTitle())
                        .put("content", news.getContent())
                        .put("status", news.getStatus())
                        .put("startDate", news.getStartDate())
                        .put("endDate", news.getEndDate())
                        .put("url", news.getUrl())
                        .put("activityStartDate", news.getActivityStartDate())
                        .put("image", news.getImage())
                        .put("createTime", news.getCreateTime())
                        .put("createBy", news.getCreateBy() != null ? news.getCreateBy().getAccountId() : null)
                        .put("updateTime", news.getUpdateTime())
                        .put("updateBy", news.getUpdateBy() != null ? news.getUpdateBy().getAccountId() : null);
                array.put(item);
            }
            responseBody.put("list", array);
            return responseBody.toString();
        }
        return "[]"; // 返回一个空数组表示没有新闻
    }

    /**
     * 根據頁碼顯示分頁News列表。
     */
    @GetMapping("/news/page")
    public Page<News> showPage(@RequestParam(defaultValue = "1") Integer pageNumber) {
        return newsService.findByPage(pageNumber);
    }

    /**
     * 根據標題關鍵字進行模糊查詢News的映射。
     */
    @GetMapping("/news/searchByTitle")
    public String searchByTitle(
            @RequestParam(value = "keyword", required = false) String keyword,
            Model model) {

        if (keyword != null && !keyword.isEmpty()) {
            // 如果提供了關鍵字，則使用NewsService根據標題進行模糊查詢
            List<News> newsList = newsService.findNewsByTitle(keyword);
            model.addAttribute("searchResults", newsList); // 將查詢結果添加到模型中
        }

        return "News/searchResults"; // 返回顯示模糊查詢結果的視圖名稱
    }

    @PutMapping("/news/remove/{id}")
    public ResponseEntity<String> removeNews(@PathVariable Integer id) {
        try {
            newsService.removeNews(id, "notuse");
            return ResponseEntity.ok("下架成功！");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("下架失败！");
        }
    }

    @PostMapping("/news/upload/{id}")
    public String uploadImage(@PathVariable Integer id, @RequestParam("imageFile") MultipartFile imageFile)
            throws IOException {
        newsService.uploadImage(id, imageFile);
        return "圖片上傳成功！";
    }

    /**
     * 根據 newsId 查詢圖片。
     */
    @GetMapping("/news/image/{newsId}")
    public ResponseEntity<byte[]> getImage(@PathVariable("newsId") Integer newsId) {
        byte[] imageData = newsService.getImageByNewsId(newsId); // 使用NewsService來取得圖片的二進位數據
        if (imageData != null) {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_JPEG); // 設置圖片類型，這裡假設是JPEG格式
            return new ResponseEntity<>(imageData, headers, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/news/{newsId}/smallImage")
    public ResponseEntity<byte[]> getNewsThumbnail(@PathVariable Integer newsId) {
        // 從資料庫中讀取 News 實體
        News news = newsService.findNewsById(newsId);

        if (news == null || news.getImage() == null) {
            return ResponseEntity.notFound().build();
        }

        try {
            // 將二進制數據轉換為 BufferedImage
            ByteArrayInputStream bis = new ByteArrayInputStream(news.getImage());
            BufferedImage originalImage = ImageIO.read(bis);

            // 生成縮圖
            BufferedImage thumbnail = Thumbnails.of(originalImage)
                    .size(100, 100)
                    .asBufferedImage();

            // 將 BufferedImage 轉換為 byte[]
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(thumbnail, "jpg", baos);
            byte[] thumbnailBytes = baos.toByteArray();

            // 返回縮圖數據
            return ResponseEntity.ok().contentType(org.springframework.http.MediaType.IMAGE_JPEG).body(thumbnailBytes);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}
