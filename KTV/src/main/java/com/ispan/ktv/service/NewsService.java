package com.ispan.ktv.service;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.expression.ParseException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.ispan.ktv.bean.News;
import com.ispan.ktv.bean.Staff;
import com.ispan.ktv.repository.NewsRepository;
import com.ispan.ktv.repository.StaffRepository;

@Service
@EnableScheduling
public class NewsService {

    @Autowired
    private NewsRepository newsRepo;
    @Autowired
    private StaffRepository staffRepo;
    @Autowired
    private StaffService staffService;

    /**
     * 新增最新消息
     */
    @Transactional
    public void insertNews(News news) {
        // 設置創建時間和更新時間為當前時間
        Date currentDateTime = new Date();
        news.setCreateTime(currentDateTime);
        news.setUpdateTime(currentDateTime);

        // 檢查結束時間不能比當前時間早
        LocalDate currentLocalDate = currentDateTime.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate endLocalDate = news.getEndDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        if (endLocalDate.isBefore(currentLocalDate) && !endLocalDate.equals(currentLocalDate)) {
            throw new IllegalArgumentException("結束日期不能比當前日期早");
        }

        // 檢查結束時間不能比開始時間早
        if (news.getStartDate() != null) {
            LocalDate startLocalDate = news.getStartDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            if (endLocalDate.isBefore(startLocalDate) && !endLocalDate.equals(startLocalDate)) {
                throw new IllegalArgumentException("結束日期不能比開始日期早");
            }
        }

        // 保存新聞
        newsRepo.save(news);
    }

    /**
     * 更新消息。
     */
    @Transactional
    public void updateNews(Integer newsId, News updatedNews) {

        Optional<News> optionalNews = newsRepo.findById(newsId);

        if (optionalNews.isPresent()) {
            News updateData = optionalNews.get();

            // 更新需要修改的屬性
            updateData.setTitle(updatedNews.getTitle());
            updateData.setContent(updatedNews.getContent());
            updateData.setStartDate(updatedNews.getStartDate());
            updateData.setEndDate(updatedNews.getEndDate());
            updateData.setStatus(updatedNews.getStatus());
            updateData.setActivityStartDate(updatedNews.getActivityStartDate());
            updateData.setUpdateBy(updatedNews.getUpdateBy());
            // 更新 staff 信息

            // 更新更新時間
            updateData.setUpdateTime(new Date());
            Date currentDateTime = new Date();
            // 檢查結束時間不能比當前時間早
            LocalDate currentLocalDate = currentDateTime.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            LocalDate endLocalDate = updatedNews.getEndDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            if (endLocalDate.isBefore(currentLocalDate) &&
                    !endLocalDate.equals(currentLocalDate)) {
                throw new IllegalArgumentException("結束日期不能比當前日期早");
            }

            // 檢查結束時間不能比開始時間早
            if (updatedNews.getStartDate() != null) {
                LocalDate startLocalDate = updatedNews.getStartDate().toInstant().atZone(ZoneId.systemDefault())
                        .toLocalDate();
                if (endLocalDate.isBefore(startLocalDate) &&
                        !endLocalDate.equals(startLocalDate)) {
                    throw new IllegalArgumentException("結束日期不能比開始日期早");
                }
            }

            // 保存更新
            newsRepo.save(updateData);
        } else {
            throw new RuntimeException("News with ID " + newsId + " not found");
        }
    }

    /**
     * 根據新聞ID查詢最新消息。
     * 
     * newsId 新聞ID。
     * return 如果找到則返回對應的最新消息，否則返回null。
     */
    @Transactional
    public News findNewsById(Integer newsId) {
        Optional<News> optional = newsRepo.findById(newsId);

        if (optional.isPresent()) {
            return optional.get();
        }
        return null;
    }

    /**
     * 檢索資料庫中所有最新消息。
     * 
     * return 所有新聞記錄的列表。
     */
    @Transactional
    public List<News> findAllNews() {
        return newsRepo.findAll();
    }

    /**
     * 根據標題關鍵字模糊查詢最新消息。
     * 
     * keyword 關鍵字。
     * return 符合關鍵字條件的最新消息列表。
     */
    @Transactional
    public List<News> findNewsByTitle(String keyword) {
        return newsRepo.findByTitleContaining(keyword);
    }

    /**
     * 根據頁碼進行分頁查詢最新消息。
     * 
     * pageNumber 頁碼，從1開始。
     * 返回指定頁碼的News分頁結果。
     */
    public Page<News> findByPage(Integer pageNumber) {
        // 設置分頁參數，每頁顯示15條數據，按照startDate降序排序
        Pageable pageable = PageRequest.of(pageNumber - 1, 10, Sort.Direction.DESC, "startDate");

        // 使用NewsRepository查詢分頁數據
        Page<News> page = newsRepo.findAll(pageable);

        return page; // 返回分頁結果
    }

    @Transactional
    public void uploadImageForNews(MultipartFile file) throws IOException {
        // 讀取文件內容
        byte[] imageData = file.getBytes();

        // 創建一個 News 對象並設置圖片數據
        News news = new News();
        news.setImage(imageData);

        // 保存 News 對象到資料庫
        newsRepo.save(news);
    }

    @Transactional
    public void removeNews(Integer newsId, String status) {
        Optional<News> optionalNews = newsRepo.findById(newsId);
        if (optionalNews.isPresent()) {
            News news = optionalNews.get();
            news.setStatus("notuse"); // 將狀態設置為 "notuse"
            news.setUpdateTime(new Date()); // 更新更新時間為當前時間
            newsRepo.save(news); // 保存更新後的新聞對象到資料庫
        } else {
            throw new RuntimeException("News with ID " + newsId + " not found");
        }
    }

    @Transactional
    public void uploadImage(Integer newsId, MultipartFile imageFile) throws IOException {
        // 根據newsId查找新聞
        News news = newsRepo.findById(newsId)
                .orElseThrow(() -> new RuntimeException("找不到新聞，ID: " + newsId));

        // 讀取文件內容
        byte[] imageData = imageFile.getBytes();

        // 設置圖片數據到新聞對象
        news.setImage(imageData);

        // 保存新聞對象到資料庫
        newsRepo.save(news);
    }

    public byte[] getImageByNewsId(Integer newsId) {
        News news = newsRepo.findById(newsId).orElse(null);
        if (news != null) {
            return news.getImage();
        }
        return null;
    }

    // @Scheduled(cron = "0 0/30 * * * *") // 每30分鐘點執行一次

    @Scheduled(cron = "0 * * * * *") // 每分鐘執行一次
    public void checkNewsExpiration() {
        System.out.println("定時任務執行時間: " + new Date());

        List<News> allNews = newsRepo.findAll();
        Date currentDateTime = new Date();

        for (News news : allNews) {
            System.out.println("處理新聞 ID: " + news.getNewsId());

            LocalDate currentLocalDate = currentDateTime.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            LocalDate startDate = news.getStartDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            LocalDate endDate = news.getEndDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

            System.out.println("當前日期: " + currentLocalDate);
            System.out.println("開始日期: " + startDate);
            System.out.println("結束日期: " + endDate);

            if (currentLocalDate.isBefore(startDate) && !"notuse".equals(news.getStatus())) {
                // 當前日期在開始日期之前，狀態設置為 stop或其他適當狀態
                System.out.println("當前日期在開始日期之前，狀態設置為 stop");
                news.setStatus("stop");
            } else if (currentLocalDate.isAfter(endDate) && !"notuse".equals(news.getStatus())) {
                // 當前日期在結束日期之後，狀態設置為 stop
                System.out.println("當前日期在結束日期之後，狀態設置為 stop");
                news.setStatus("stop");
            } else if (!"notuse".equals(news.getStatus())) {
                // 當前日期在開始日期和結束日期之間（包括兩者），狀態設置為 active
                System.out.println("當前日期在開始日期和結束日期之間（包括兩者），狀態設置為 active");
                news.setStatus("active");
            }

            news.setUpdateTime(currentDateTime);
            newsRepo.save(news);

            System.out.println("處理完畢，更新後狀態為: " + news.getStatus());
        }
    }

}
